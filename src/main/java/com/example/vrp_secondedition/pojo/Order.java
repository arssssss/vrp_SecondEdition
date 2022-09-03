package com.example.vrp_secondedition.pojo;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class Order {

    public Integer id;

    public Integer user_id;

    public Integer administrator_id;

    public Integer dispatcher_id;

    public Integer vehicle_id;

    public Integer demand;

    public LocalDateTime order_time;

    public String state;

    public String address;

    public Boolean dispatcher_check;

    public Boolean user_check;

    public LocalDateTime finish_time;

    public Integer routing_number;

    public Integer path_sequence;
}
