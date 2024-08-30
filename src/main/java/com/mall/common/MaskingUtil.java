package com.mall.common;

public class MaskingUtil {
	
	// 이름 별표 처리
    public static String maskName(String uname) {
        if (uname == null || uname.length() < 2) {
            return uname; // 이름이 1글자이면 그대로 반환
        }

        // 첫 글자만 표시하고 나머지는 별표 처리
        String visiblePart = uname.substring(0, 1);
        String maskedPart = uname.substring(1).replaceAll(".", "*");

        return visiblePart + maskedPart;
    }

}
