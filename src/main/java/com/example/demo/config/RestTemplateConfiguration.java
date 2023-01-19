package com.example.demo.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class RestTemplateConfiguration {
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder
                .setConnectTimeout(Duration.ofSeconds(5L))
                .setReadTimeout(Duration.ofSeconds(5L))
                .build();
    }

//    @Bean
//    public ObjectMapper objectMapper() {
//        return new ObjectMapper();
//    }
//
//    static class RestTemplateResponseErrorHandler implements ResponseErrorHandler {
//
//        @Override
//        public boolean hasError(ClientHttpResponse response) throws IOException {
//            System.out.println("AAAAAAAAAAAAAAAAAAAAA");
//            return !response.getStatusCode().equals(HttpStatus.OK);
//        }
//
//        @Override
//        public void handleError(ClientHttpResponse response) throws IOException {
//            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@");
//            throw new HttpServerErrorException(response.getStatusCode(), readBody(response));
//        }
//
//        private String readBody(ClientHttpResponse response) throws IOException {
//            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getBody()));
//            return bufferedReader.lines().collect(Collectors.joining("\n"));
//        }
//    }

}
