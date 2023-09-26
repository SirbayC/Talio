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
package server.config;

import commons.mappers.BoardMapper;
import commons.mappers.CardListMapper;
import commons.mappers.CardMapper;
import commons.mappers.TagMapper;
import commons.mappers.TaskMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    /**
     * Get board mapper instance
     *
     * @return board mapper
     */
    @Bean
    public BoardMapper boardMapper() {
        return BoardMapper.INSTANCE;
    }

    /**
     * Get card list mapper instance
     *
     * @return card list mapper
     */
    @Bean
    public CardListMapper cardListMapper() {
        return CardListMapper.INSTANCE;
    }

    /**
     * Get card mapper instance
     *
     * @return card mapper
     */
    @Bean
    public CardMapper cardMapper() {
        return CardMapper.INSTANCE;
    }

    /**
     * Get tag mapper instance
     *
     * @return tag mapper
     */
    @Bean
    public TagMapper tagMapper() {
        return TagMapper.INSTANCE;
    }

    /**
     * Get task mapper instance
     *
     * @return task mapper
     */
    @Bean
    public TaskMapper taskMapper() {
        return TaskMapper.INSTANCE;
    }

}