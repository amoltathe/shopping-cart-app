package com.at.shopping.cart.datasource;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpProductDataSource implements ProductDataSource {

    private HttpClient client;
    /**
     * BUILDER ENTRY POINT
     */
    public static Builder builder() {
        return new Builder();
    }
    /**
     * RESPONSIBILITY:
     * Fetch product JSON using modern HTTP Client (JDK 11+ standard, preferred in JDK 21)
     * DESIGN:
     * - Non-deprecated API (no URL class)
     * - Immutable HttpClient
     * - Simple synchronous request (can be upgraded to async later)
     */
    @Override
    public String fetch(String endpoint) {
        try {
            var request = HttpRequest.newBuilder()
                    .uri(URI.create(endpoint))
                    .GET()
                    .build();

            var response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return response.body();

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch data", e);
        }
    }

    /**
     * BUILDER-STYLE REQUEST CREATION
     */
    private HttpRequest buildRequest(String endpoint) {
        return HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .GET()
                .build();
    }

    // =========================
    // BUILDER CLASS
    // =========================
    public static class Builder {

        private final HttpProductDataSource instance = new HttpProductDataSource();

        public Builder client(HttpClient client) {
            instance.client = client;
            return this;
        }

        public HttpProductDataSource build() {
            if (instance.client == null) {
                instance.client = HttpClient.newHttpClient(); // default fallback
            }
            return instance;
        }
    }
}