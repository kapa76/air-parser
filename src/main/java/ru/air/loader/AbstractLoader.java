package ru.air.loader;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import ru.air.entity.FlightAD;

import java.io.IOException;

public class AbstractLoader {
    private BaseLoader loader;

    private ObjectMapper objectMapper;

    public AbstractLoader(){
        objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public BaseLoader getLoader() {
        return loader;
    }

    public void setLoader(BaseLoader loader) {
        this.loader = loader;
    }

    public String parse() {
        FlightAD flight = loader.load();
        ObjectWriter ow = objectMapper.writer().withDefaultPrettyPrinter();
        String json = "";
        try {
            json = ow.writeValueAsString(flight);
            System.out.println(json);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }
}
