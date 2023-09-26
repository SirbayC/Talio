package server.encryption;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import server.services.EncryptionService;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class EncryptionServiceTest {

    @Test
    public void test1() throws FileNotFoundException {
        EncryptionService encryptionService = new EncryptionService(new Scanner("660989539"));
        String password = "adminPassword";
        assertTrue(encryptionService.hashing(password));
    }

    @Test
    public void test2() throws FileNotFoundException {
        EncryptionService encryptionService = new EncryptionService(new Scanner("-1827545373"));
        String password = "123testingMyCode!";
        assertTrue(encryptionService.hashing(password));
    }

}
