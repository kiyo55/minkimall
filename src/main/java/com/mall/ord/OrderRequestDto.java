package com.mall.ord;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

//Data Transfer Object - js에서 전송된 데이터를 담기 위한 클래스

@Data
public class OrderRequestDto {
	private String paynum;
	private List<String> cartidList = new ArrayList<>();
    private List<String> productCodeList = new ArrayList<>();
    private List<Integer> pquantityList = new ArrayList<>();
    
    private String uid;
    private String pnames;
    private String oname;
    private String otel;
    private String omail;
    private String oadd;
    private String odetailAddress;
    private String omethod;
    private String ostatus;
    private int totalPay;
    
}


