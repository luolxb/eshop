package com.soubao.entity;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Region {

    private Integer id;

    private Byte level;

    private Integer parentId;

    private Byte isHot;

    private String name;

}