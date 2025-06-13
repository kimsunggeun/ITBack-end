package com.example.template.domain.comCode.service;


import com.example.template.domain.comCode.mapper.comCodeMapper;

import com.example.template.domain.comCode.vo.comCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class comCodeService {
    private final comCodeMapper comCodeMapper;
    public List<comCode> searchCode() {
        return comCodeMapper.searchCode();
    }
}