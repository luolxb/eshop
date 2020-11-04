package com.soubao.entity;

import lombok.*;

@Setter
@Getter
public class Region {
    private Integer id;
    private Byte level;
    private Integer parentId;
    private Byte isHot;
    private String name;
}