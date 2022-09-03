package com.example.vrp_secondedition.enums.result;

import lombok.Getter;

@Getter
public enum ResultCode {
    ERROR(400),OK(200),TOKENERROR(209);

    int value;

    ResultCode(int i) {
        value=i;
    }
}
