package com.kkoz.sadogorod.controls;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/test")
public class ApiTest {

    @GetMapping("/all")
    public String test() {
        return "HELLO";
    }
}
