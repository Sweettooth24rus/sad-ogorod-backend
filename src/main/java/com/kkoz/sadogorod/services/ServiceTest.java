package com.kkoz.sadogorod.services;

import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServiceTest {

    public static JsonNode mail() throws UnirestException {

        String url = "https://api.stormglass.io/v2/weather/point";
        String api = "/////";
        String lat = String.valueOf(55.982848);
        String lng = String.valueOf(92.863500);
        String params = "airTemperature";

        /*HttpResponse<JsonNode> request = Unirest.get(url)
                .header("Authorization", api)
                .queryString("lat", lat)
                .queryString("lng", lng)
                .queryString("params", params)
                .asJson();
        String data = request.getBody().toString();
        return request.getBody();*/



        /*HttpResponse<JsonNode> request = Unirest.post(System.getenv("MAIL_GUN_DOMAIN") + "/messages")
                .basicAuth("api", System.getenv("MAIL_GUN_KEY"))
                .queryString("from", "sad-ogorod <System@kkoz.sadogorod.com>")
                .queryString("to", "kostya_superstar@mail.ru")
                .queryString("subject", "validation account")
                .queryString("text", "testing")
                .asJson();
        return request.getBody();*/
        return new JsonNode("heh");
    }
}
