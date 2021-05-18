package it.paspaola.quarkus.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class JsonDateOfBirthDeserializer extends JsonDeserializer<LocalDate> {
    private static final DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    @Override
    public LocalDate deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        return LocalDate.parse(jsonParser.getText(), format);
    }
}
