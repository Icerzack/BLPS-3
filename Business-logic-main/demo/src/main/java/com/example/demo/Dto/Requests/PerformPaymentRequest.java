package com.example.demo.Dto.Requests;

import lombok.Data;

@Data
public class PerformPaymentRequest {
    private int userId;
    private String cardNum;
    private String cardDate;
    private String cardCvv;
    private Double cost;
    private String address;

}