package com.kkoz.sadogorod.controls;


import com.kkoz.sadogorod.services.ServiceTest;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/test")
public class ApiTest {

    private final ServiceTest serviceTest;

    @GetMapping("/msg")
    public ResponseEntity<String> getCurrentUzer() throws UnirestException {
        log.info("-> GET: TEST");
        JsonNode tmp = serviceTest.mail();
        log.info("<- GET: TEST");
        return ResponseEntity.ok(tmp.toString());
    }
}
