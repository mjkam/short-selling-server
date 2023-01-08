package com.example.demo.controller;

import com.example.demo.controller.dto.GetTop50Response;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class Top50Controller {
    @GetMapping("/top50")
    public GetTop50Response getTop50() {
        return new GetTop50Response();
    }
}
