package com.mall.review;

import lombok.Data;

@Data
public class ReviewVo {
	
	private String rid; //고유키
	private String rcontent;
	private String rdate;	
	private int review_count; //제품별 후기 건수. vo에만 있음
	
	//참조키
	private String oid;
	private String product_code;
	private String uid;
	
	//users와 join
	private String uname;	
	
	//products와 join
	private String pname;
	private String pimgStr;
}
