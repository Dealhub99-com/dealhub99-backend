package com.dealhub99.backend.controller;

import com.dealhub99.backend.dto.BaseResponse;
import com.dealhub99.backend.entity.News;
import com.dealhub99.backend.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/news")
@RequiredArgsConstructor
public class NewsController {

    private final NewsRepository newsRepository;

    @GetMapping
    public ResponseEntity<BaseResponse<List<News>>> getAllNews() {
        return ResponseEntity.ok(BaseResponse.success("News fetched successfully", newsRepository.findAllByOrderByCreatedAtDesc()));
    }

    @PostMapping
    public ResponseEntity<BaseResponse<News>> createNews(@RequestBody News news) {
        return ResponseEntity.ok(BaseResponse.success("News created successfully", newsRepository.save(news)));
    }
}
