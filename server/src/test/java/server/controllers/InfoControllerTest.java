package server.controllers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import server.services.EncryptionService;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

class InfoControllerTest {

    InfoController sut = new InfoController(new EncryptionService(new Scanner("660989539")));

    InfoControllerTest() throws FileNotFoundException {
    }

    @Test
    void index() {
        Assertions.assertEquals(sut.index(), "talio-g65");
    }

    @Test
    void adminIndex() {
        Assertions.assertEquals("talio-g65/adminAccessGranted", sut.adminIndex("adminPassword"));
        Assertions.assertEquals("talio-g65/adminAccessRejected", sut.adminIndex("notAdminPassword"));
    }
}