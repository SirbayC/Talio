package server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;
import java.io.FileNotFoundException;
import java.util.Scanner;

@Configuration
public class EncryptionConfig {

    /**
     * Used to get the scanner to the file
     *
     * @param filename name of the file (not path!)
     * @return new Scanner
     * @throws FileNotFoundException should not throw
     */
    @Bean
    public Scanner passwordscanner(@Value("passwordhash.txt") String filename) throws FileNotFoundException {
        return new Scanner(ResourceUtils.getFile("classpath:" + filename));
    }
}
