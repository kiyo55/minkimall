package com.mall.user;

import lombok.Data;

@Data
public class UserVo { //login과는 무관하며 order 정보엔 참조 필요
	
	private String uid;
	private String upw; //첫째 비밀번호
	private String confirmUpw; //비밀번호 재확인용
	private String uname;
	private String utel;
	private String umail;
	private String uadd;
	private String detailAddress;
	
	private String username;
}
