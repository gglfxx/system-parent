package com.online.system.web.entity;

import lombok.Getter;

/**
 * 季节枚举
 */
@Getter
public enum SeasonEnum {
    SPRING("spring"),
    SUMMER("summer"),
    AUTUMN("autumn"),
    WINTER("winter");

    private String season;

    SeasonEnum(String season){
      this.season = season;
    }
}
