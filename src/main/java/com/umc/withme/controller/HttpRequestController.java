package com.umc.withme.controller;

import com.umc.withme.exception.auth.KakaoOAuthUnauthorizedException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@RestController
public class HttpRequestController {

    public ResponseEntity<String> sendHttpRequest(String requestUrl, HttpHeaders headers) {
        try {
            HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
            return new RestTemplate().exchange(
                    requestUrl,
                    HttpMethod.GET,
                    kakaoUserInfoRequest,
                    String.class
            );
        } catch (HttpClientErrorException.Unauthorized e) {
            throw new KakaoOAuthUnauthorizedException();
        }
    }
}
