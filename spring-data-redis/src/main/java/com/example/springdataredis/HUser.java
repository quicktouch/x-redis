package com.example.springdataredis;

import lombok.Data;

import java.io.Serializable;

@Data
public class HUser implements Serializable {
    private String id;
    private String nickName;
    private Integer age;
}