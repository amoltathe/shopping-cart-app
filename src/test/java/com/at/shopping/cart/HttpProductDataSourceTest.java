package com.at.shopping.cart;


import com.at.shopping.cart.datasource.HttpProductDataSource;
import org.junit.jupiter.api.Test;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class HttpProductDataSourceTest {



        // =========================
        // SUCCESS SCENARIO
        // =========================
        @Test
        void shouldFetchProductJsonSuccessfully() throws Exception {

            // MOCK HttpClient + Response
            HttpClient client = mock(HttpClient.class);
            HttpResponse<String> response = mock(HttpResponse.class);

            String json = """
                {
                  "title": "Cheerios",
                  "price": 8.43
                }
                """;

            when(response.body()).thenReturn(json);

            when(client.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                    .thenReturn(response);

            // BUILD OBJECT USING BUILDER
            HttpProductDataSource dataSource =
                    HttpProductDataSource.builder()
                            .client(client)
                            .build();

            // EXECUTE
            String result = dataSource.fetch("https://test.com/product.json");

            // ASSERT
            assertNotNull(result);
            assertTrue(result.contains("Cheerios"));

            verify(client, times(1))
                    .send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
        }

        // =========================
        // FAILURE SCENARIO
        // =========================
        @Test
        void shouldThrowRuntimeExceptionWhenHttpFails() throws Exception {

            HttpClient client = mock(HttpClient.class);

            when(client.send(any(), any()))
                    .thenThrow(new RuntimeException("Network failure"));

            HttpProductDataSource dataSource =
                    HttpProductDataSource.builder()
                            .client(client)
                            .build();

            RuntimeException ex = assertThrows(
                    RuntimeException.class,
                    () -> dataSource.fetch("https://test.com")
            );

            assertTrue(ex.getMessage().contains("Failed to fetch data"));
        }

        // =========================
        // DEFAULT BUILDER TEST
        // =========================
        @Test
        void shouldCreateInstanceWithDefaultHttpClient() {

            HttpProductDataSource dataSource =
                    HttpProductDataSource.builder()
                            .build();

            assertNotNull(dataSource);
        }

}
