package main.utils;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import main.entity.User;

public class HttpHeadersUtils {
    @Autowired
    private User user;

    public HttpHeaders createHeaders(MediaType mediaType) {
        HttpHeaders httpHeaders = new HttpHeaders();
        if (user.getToken() != null) {
            httpHeaders.setBearerAuth(user.getToken());
        }
        httpHeaders.setAccept(Collections.singletonList(mediaType));
        return httpHeaders;
    }
}
