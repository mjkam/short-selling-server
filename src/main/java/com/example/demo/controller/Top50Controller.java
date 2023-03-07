package com.example.demo.controller;

import com.example.demo.controller.dto.GetTop50Response;
import com.example.demo.service.Top50Service;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class Top50Controller {
    private final Top50Service top50Service;

    @GetMapping("/top50")
    public GetTop50Response getTop50() {
        return new GetTop50Response(top50Service.getTop50());
    }
}