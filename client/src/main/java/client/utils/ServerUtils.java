/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client.utils;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.MediaType.TEXT_PLAIN;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import commons.dto.BoardDto;
import commons.entities.Board;
import commons.mappers.BoardMapper;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.UriBuilder;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.util.Pair;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

public class ServerUtils {

    private final String host;
    private final Client client;

    private StompSession session;

    /**
     * Initializes a new server with the provided string host
     *
     * @param host - the address the user enters
     */
    @Inject
    public ServerUtils(String host) {
        this.host = host;

        ObjectMapper mapper = new ObjectMapper().registerModule(new LocalDateModule("dd-MM-yyyy"));
        JacksonJaxbJsonProvider provider = new JacksonJaxbJsonProvider();
        provider.setMapper(mapper);
        this.client = ClientBuilder.newClient(new ClientConfig(provider));
    }

    /**
     * Long-poll server
     *
     * @return future list of boards
     */
    public RepeatingFuture<List<Board>> longPoll() {
        RepeatingFuture<List<Board>> future = new RepeatingFuture<>();
        loopPoll(future);

        return future;
    }

    /**
     * Poll server and keep repeating until future is cancelled
     *
     * @param future future
     */
    private void loopPoll(RepeatingFuture<List<Board>> future) {
        System.out.println("Poll!");
        TaskWrapper.wrap(
            () -> this.client.target(host)
                .path("boards/poll")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<List<BoardDto>>() {
                })
        ).thenApply(boards -> boards.stream()
            .map(BoardMapper.INSTANCE::toEntity)
            .toList()
        ).thenAccept(boards -> {
            if(future.isCancelled())
                return;

            future.complete(boards);
            loopPoll(future);
        }).exceptionally(e -> {
            if(!future.isCancelled())
                loopPoll(future);

            return null;
        });
    }

    /**
     * Get board by identifier
     *
     * @param boardId identifier
     * @return board
     */
    public CompletableFuture<Board> getBoard(long boardId) {
        return TaskWrapper.wrap(
            () -> this.client.target(host)
                .path("boards/" + boardId)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(BoardDto.class)
        ).thenApply(BoardMapper.INSTANCE::toEntity);
    }

    /**
     * @param boardDto boardDto
     * @return a board with the specified name
     */
    public CompletableFuture<Board> createBoard(BoardDto boardDto) {
        return TaskWrapper.wrap(
            () -> this.client.target(host)
                .path("boards/")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(Entity.entity(boardDto, APPLICATION_JSON), BoardDto.class)
        ).thenApply(BoardMapper.INSTANCE::toEntity);
    }

    /**
     * @return all the boards stored in the database (basic REST request)
     */
    public CompletableFuture<List<Board>> getBoards() {
        return TaskWrapper.wrap(
            () -> this.client.target(host)
                .path("boards/")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(new GenericType<List<BoardDto>>(){})
        ).thenApply(boards -> boards.stream()
            .map(BoardMapper.INSTANCE::toEntity)
            .toList()
        ).exceptionally(e -> {
            e.printStackTrace();
            return null;
        });
    }

    /**
     * Checks if the provided server is valid
     *
     * @param host server address
     * @return server exists enum
     */
    public static CompletableFuture<ServerExists> checkServerExists(String host) {
        if(host == null || host.isBlank())
            return CompletableFuture.completedFuture(ServerExists.EMPTY_ADDRESS);

        if(!host.startsWith("http://") && !host.startsWith("https://"))
            return CompletableFuture.completedFuture(ServerExists.INVALID_ADDRESS);

        return TaskWrapper.wrap(() -> {
            try(Client c = ClientBuilder.newClient(new ClientConfig())) {
                String res = c.target(host)
                    .path("")
                    .request(TEXT_PLAIN)
                    .accept(TEXT_PLAIN)
                    .get(String.class);

                return switch(res) {
                    case "talio-g65" -> ServerExists.VALID;
                    case "talio-g65/adminAccessGranted" -> ServerExists.ADMIN;
                    case "talio-g65/adminAccessRejected" -> ServerExists.INVALID_PASSWORD;
                    default -> ServerExists.INVALID_SERVER;
                };
            } catch (Exception e) {
                return ServerExists.REQUEST_EXCEPTION;
            }
        });
    }

    /**
     * "Registers" the client to receive update messages when they enter the server
     *
     * @param destination - the destination to subscribe operations of this session to
     * @param type        - class of the payload type
     * @param consumer    the consumer function that handles the packages
     * @param <T>         - the payload type of the communication
     */
    public <T> void registerForMessages(String destination, Class<T> type, Consumer<T> consumer) {
        Optional.ofNullable(session).ifPresent(session -> session.subscribe(
            destination,
            new StompSessionHandlerAdapter() {
                @Override
                public Type getPayloadType(StompHeaders headers) {
                    return type;
                }

                @Override
                public void handleFrame(StompHeaders headers, Object payload) {
                    //noinspection unchecked
                    TaskWrapper.wrap(() -> (T) payload).thenAccept(consumer);// Make consumer run in FX application thread
                }
            }
        ));
    }

    /**
     * Sends the payload to the specified destination
     *
     * @param destination - the destination to be sent to
     * @param payload     - the payload to send
     */
    public void send(String destination, Object payload) {
        Optional.ofNullable(session).ifPresent(session -> session.send(destination, payload));
    }

    /**
     * Connects the client asynchronously and once the connection is established the session is set
     *
     * @return a completable future that once completed can execute a callback
     */
    public CompletableFuture<Void> connect() {
        WebSocketStompClient client = new WebSocketStompClient(new StandardWebSocketClient());

        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.getObjectMapper().registerModule(new LocalDateModule("dd-MM-yyyy"));
        client.setMessageConverter(converter);
        try {
            return client.connectAsync(
                    createWebSocketAddress(),
                    new StompSessionHandlerAdapter() {
                    }
                ).thenAccept(session -> this.session = session)
                .exceptionally(e -> {
                    throw new RuntimeException("Session not returned");
                });
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Something went wrong with websockets");
        }
    }

    /**
     * Disconnect websocket
     */
    public void disconnect() {
        if(session == null)
            return;

        session.disconnect();
    }

    /**
     * Creates the websocket address
     *
     * @return the configured websocket address
     */
    private String createWebSocketAddress() {
        Pair<String, Integer> hostPort = extractHostPort();
        return UriBuilder
            .newInstance()
            .scheme("ws")
            .host(hostPort.getKey())
            .port(hostPort.getValue())
            .path("/websocket")
            .build()
            .toString();
    }

    /**
     * By the given name of the server extracts the port and the host from it
     *
     * @return a pair of host and port
     */
    private Pair<String, Integer> extractHostPort() {
        Pattern pattern = Pattern.compile("http://(.+):([0-9]{4})");
        Matcher patternMatcher = pattern.matcher(this.host);
        if(patternMatcher.find()) {
            return new Pair<>(patternMatcher.group(1), Integer.parseInt(patternMatcher.group(2)));
        } else
            throw new RuntimeException("Regex does not match the host name");
    }


}