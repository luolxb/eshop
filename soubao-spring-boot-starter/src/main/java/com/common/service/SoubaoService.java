package com.common.service;

public class SoubaoService {
    public String sayWhat;
    public String toWho;
    public SoubaoService(String sayWhat_, String toWho_){
        sayWhat = sayWhat_;
        toWho = toWho_;
    }
    public String say(){
        return sayWhat + "!  " + toWho;
    }
}
