package com.kkoz.sadogorod.services;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServiceTest {

    public static JsonNode mail() throws UnirestException {
        HttpResponse<JsonNode> request = Unirest.post(System.getenv("MAIL_GUN_DOMAIN") + "/messages")
                .basicAuth("api", System.getenv("MAIL_GUN_KEY"))
                .queryString("from", "sad-ogorod <System@kkoz.sadogorod.com>")
                .queryString("to", "kostya_superstar@mail.ru")
                .queryString("subject", "validation account")
                .queryString("text", "testing")
                .asJson();
        return request.getBody();
    }
}
