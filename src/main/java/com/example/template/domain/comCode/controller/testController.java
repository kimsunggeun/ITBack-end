package com.example.template.domain.comCode.controller;

import com.example.template.api.apiResponse;
import com.example.template.domain.comCode.service.comCodeService;

import com.example.template.domain.comCode.vo.comCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class testController {
    private final comCodeService comCodeService;

    @PostMapping("/test")
    public apiResponse<List<comCode>> searchCode() {
        List<comCode> result = comCodeService.searchCode();
        return apiResponse.success(result);
    }
}