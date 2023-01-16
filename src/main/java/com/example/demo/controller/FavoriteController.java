package com.example.demo.controller;

import com.example.demo.controller.dto.RegisterFavoriteRequest;
import com.example.demo.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FavoriteController {
    private final FavoriteService favoriteService;

    @PostMapping("/favorite")
    public void registerFavorite(@RequestBody RegisterFavoriteRequest request) {
        favoriteService.registerFavorite(request.getCompanyCode());
    }
}
