package com.example.template.domain.comCode.mapper;


import com.example.template.domain.comCode.vo.comCode;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface  comCodeMapper {
    List<comCode> searchCode();
}
