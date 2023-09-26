package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UpdateModelTest {

    @Test
    void setGreeting() {
        UpdateModel updateModel = new UpdateModel();
        updateModel.setGreeting("gr");
        assertEquals("gr", updateModel.getGreeting());
    }
}