package br.com.alura.comex.auth.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class AuthClient {

    private final RestClient client;

    public AuthClient(RestClient.Builder builder,
            @Value("${gateway.base.url}") String baseUrl) {
        this.client = builder.baseUrl(baseUrl).build();
    }

    public String validateToken(String bearerToken) {
        return client.post()
                .uri("/usuario-ms/login/validate")
                .header(HttpHeaders.AUTHORIZATION, bearerToken)
                .retrieve()
                .body(String.class);
    }

}