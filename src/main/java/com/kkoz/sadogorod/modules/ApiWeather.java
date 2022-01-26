package com.kkoz.sadogorod.modules;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/weather")
public class ApiWeather {

    @GetMapping("/get")
    public ResponseEntity<String> getWeather() throws UnirestException {
        log.info("-> GET: WEATHER");
        JsonNode tmp = ServiceWeather.mail();
        log.info("<- GET: WEATHER");
        return ResponseEntity.ok(tmp.toString());
    }
}

@Service
@RequiredArgsConstructor
class ServiceWeather {

    public static JsonNode mail() throws UnirestException {

        String url = "https://api.stormglass.io/v2/weather/point";
        String lat = String.valueOf(55.982848);
        String lng = String.valueOf(92.863500);
        String params = "airTemperature";

        HttpResponse<JsonNode> request = Unirest.get(url)
                .header("Authorization", System.getenv("WEATHER_KEY"))
                .queryString("lat", lat)
                .queryString("lng", lng)
                .queryString("params", params)
                .asJson();
        String data = request.getBody().toString();
        return request.getBody();
    }
}
