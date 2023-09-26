package server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Scanner;

@Service
public class EncryptionService {

    private final int key;

    /**
     * Creates an instance of an encryption service, first being injected with the scanner
     * provided by the spring application context
     *
     * @param scanner already provided with the file - should contain the hash for the password
     */
    @Autowired
    public EncryptionService(Scanner scanner){
        key = scanner.nextInt();
    }

    /**
     * Check password against key
     *
     * @param password password
     * @return whether they match
     */
    public boolean hashing(String password) {
        int hash = 7;
        for(int i = 0; i < password.length(); i++)
            hash = hash * 31 + password.charAt(i);

        return hash == key;
    }

}
