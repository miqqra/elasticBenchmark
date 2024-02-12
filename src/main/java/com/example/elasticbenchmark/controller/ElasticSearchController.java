package com.example.elasticbenchmark.controller;

import com.example.elasticbenchmark.dto.ElasticSearchDto;
import com.example.elasticbenchmark.dto.ElasticSearchDtos;
import com.example.elasticbenchmark.service.ElasticSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/elastic")
@RequiredArgsConstructor
public class ElasticSearchController {

    private final ElasticSearchService service;

    @PostMapping("/find")
    public List<ElasticSearchDto> findEntity(@RequestBody String phrase) {
        return service.findByName(phrase);
    }

    @PostMapping("/add")
    public ElasticSearchDto addEntity(@RequestBody ElasticSearchDto dto) {
        return service.save(dto);
    }

    @PostMapping("/add/bulk")
    public ElasticSearchDtos addEntities(@RequestBody ElasticSearchDtos dtos) {
        return service.bulkSave(dtos);
    }
}
