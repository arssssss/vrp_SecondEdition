package com.example.vrp_secondedition.enums.Driving;

public enum Strategy {

    Default(32),Fastest(38),LessCharge(36);

    int value;

    Strategy(int value){
        this.value=value;
    }
}
