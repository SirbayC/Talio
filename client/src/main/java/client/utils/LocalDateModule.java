package client.utils;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.PackageVersion;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateModule extends SimpleModule {

    /**
     * Local Date Module
     *
     * @param format date time format
     */
    public LocalDateModule(String format) {
        super(PackageVersion.VERSION);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        addSerializer(LocalDate.class, new LocalDateSerializer(formatter));
        addDeserializer(LocalDate.class, new LocalDateDeserializer(formatter));
    }

}
