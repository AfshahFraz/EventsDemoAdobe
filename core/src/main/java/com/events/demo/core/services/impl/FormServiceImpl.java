package com.events.demo.core.services.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.json.Json;
import javax.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.osgi.service.component.annotations.Component;

import com.events.demo.core.services.FormService;

@Component(service = FormService.class)
public class FormServiceImpl implements FormService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FormServiceImpl.class);
    private static final String EXTERNAL_API_URL = "https://66ae7fa2b05db47acc57bb6b.mockapi.io/FormEvent";

    @Override
    public void processForm(String id, String firstName, String lastName, String email) {
        HttpURLConnection connection = null;
        try {
            // Create JSON object to send to the external API
            JsonObject jsonObject = Json.createObjectBuilder()
                .add("id", id)
                .add("firstName", firstName)
                .add("lastName", lastName)
                .add("email", email)
                .build();

            // Create a connection to the external API
            URL url = new URL(EXTERNAL_API_URL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // Send the JSON data
            try (OutputStream os = connection.getOutputStream()) {
                os.write(jsonObject.toString().getBytes());
                os.flush();
            }

            // Check the response code
            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK && responseCode != HttpURLConnection.HTTP_CREATED) {
                // Handle non-OK responses
                LOGGER.error("Failed to send data to external API. HTTP error code: {}", responseCode);
                throw new RuntimeException("Failed : HTTP error code : " + responseCode);
            }

        } catch (IOException e) {
            LOGGER.error("IOException occurred while processing the form: {}", e.getMessage(), e);
            throw new RuntimeException("IOException occurred while processing the form", e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
