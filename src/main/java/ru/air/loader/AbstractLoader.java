package ru.air.loader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import ru.air.entity.FlightAD;

import java.io.IOException;

public class AbstractLoader {
    private BaseLoader loader;

    public BaseLoader getLoader() {
        return loader;
    }

    public void setLoader(BaseLoader loader) {
        this.loader = loader;
    }

    public String parse() {
        FlightAD flight = loader.load();
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
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
