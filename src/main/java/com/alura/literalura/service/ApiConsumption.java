package com.alura.literalura.service;

import com.alura.literalura.model.DatosLibro;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class ApiConsumption {

    private static final String URL_BASE = "https://gutendex.com/books?search=";
    private final ObjectMapper objectMapper = new ObjectMapper();


    public String obtainData(String url) {
        HttpClient client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL_BASE + url))
                .build();
        HttpResponse<String> response;
        try {
            response = client
                    .send(request, HttpResponse.BodyHandlers.ofString());
            JsonNode root = objectMapper.readTree(response.body());
            JsonNode results = root.get("results");
            if (!results.isEmpty()) {
                JsonNode book = results.get(0);
                return book.toPrettyString();
            } return null;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public DatosLibro getBookData(String title) {
        var converter = new DataConverter();
        try {
            var json = obtainData(title.replace(" ", "+"));
            return converter.obtainData(json, DatosLibro.class);
        } catch (Exception e) {
            String mensajeOpcionInvalida = "Opción inválida. Intente nuevamente.";
            System.out.println(mensajeOpcionInvalida);
        }
        return null;
    }
}
