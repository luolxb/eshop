package com.soubao.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SBApi {
    private int status = 1;
    private String msg = "成功";
    private Object result;
    public Object getResult(){
        if(result == null){
            return new ArrayList<>();
        }else{
            return result;
        }
    }
}
