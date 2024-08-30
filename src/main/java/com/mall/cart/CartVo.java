package com.mall.cart;

import java.text.NumberFormat;
import java.util.Locale;

import lombok.Data;

@Data
public class CartVo {
	
	private String cartid;	
	private String uid;	// 사용자 id
	private String product_code;	
	private String pname;
	private String pimgStr;	
	private String ctime;	//cart update time 최신순으로 정렬 위함
	
	private int isDel;	// 가삭제여부 (주문내역에 이동됐는지) - Soft Delete
	
	private int pstock;	
	private int pquantity;
	
	//연산 시 int 사용, 표시는 fmt 사용
	private int pprice;
	private int pdelifee;
	
	// 통화 변환. 천단위 콤마 표시
		public String getFmtPprice() {
			NumberFormat numberFormat = 
					NumberFormat.getNumberInstance(Locale.getDefault());
			
			return numberFormat.format(pprice);
		}
		
		public String getFmtPdelifee() {
			NumberFormat numberFormat = 
					NumberFormat.getNumberInstance(Locale.getDefault());
			
			return numberFormat.format(pdelifee);
		}
	
}
