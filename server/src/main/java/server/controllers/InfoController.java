package server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import server.services.EncryptionService;

@RestController
@RequestMapping("/")
public class InfoController {

    private final EncryptionService encryptionService;

    /**
     * Injects the controller with the encryption service
     *
     * @param encryptionService for the admin password
     */
    @Autowired
    public InfoController(EncryptionService encryptionService){
        this.encryptionService = encryptionService;
    }

    /**
     * Index
     *
     * @return talio-g65
     */
    @GetMapping("/")
    public String index() {
        return "talio-g65";
    }

    /**
     *
     * @param password given by the user
     * @return a string indicating whether access to the admin mode is granted
     */
    @GetMapping("/admin")
    public String adminIndex(@RequestParam String password) {
        if(password!=null && encryptionService.hashing(password)) {
            return "talio-g65/adminAccessGranted";
        }
        else {
            return "talio-g65/adminAccessRejected";
        }
    }

}
