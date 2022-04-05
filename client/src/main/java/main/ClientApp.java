package main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import main.entity.User;
import main.utils.HttpHeadersUtils;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class ClientApp {
    public static void main(String[] args) {
        SpringApplication.run(ClientApp.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public User createUser() {
        return new User();
    }

    @Bean
    public HttpHeadersUtils createHttpHeadersUtils() {
        return new HttpHeadersUtils();
    }
}

