package com.mall.login;

import lombok.Data;

@Data
public class LoginVo {
	private String username;
    private String password;
    private String loginType; // admin 또는 user

}
