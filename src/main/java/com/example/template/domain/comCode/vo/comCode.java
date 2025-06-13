package com.example.template.domain.comCode.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class comCode {
    String GUBUN     ;
    String CODE      ;
    String NAME      ;
    String PROPERTY1 ;
    String PROPERTY2 ;
    String PROPERTY3 ;
    String PROPERTY4 ;
    String USEYN     ;
    String BIGO      ;
    LocalDateTime CREDATE   ;
    String CREUSER   ;
    LocalDateTime  MODATE    ;
    String MOUSER    ;

}
