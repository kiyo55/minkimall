package com.mall.ord;

import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import lombok.Data;

@Data
public class OrdVo {
	
	//결제내역 송부용	
	private String omail;
	
	//수신 결제내역에 표시될 것	
	private String oname; //mname
	private String pname; //dataname 해당
	private String pnames; //'상품명 외 n건' 표시용
	private int totalPay; //dataprice
	
	private String paynum; //결제번호
	
	private String oid;
	private String uid;	// 사용자 id
	private String cartid;
	private String product_code;
	
	//해당 테이블에 있는 값
	private String omethod;
	private Timestamp odate;
	private String ostatus;	
	private String otel;	
	private String oadd;
	private String odetailAddress;
	private Timestamp otime; //최신순으로 정렬 위함
	
	//users와 join
	private String uname;
	private String utel;
	private String umail; //결제내역 송부용
	private String uadd;	
	private String detailAddress;
	
	private int pquantity;
	
	//products와 join	
	private String pimgStr;
	private int pprice;	
	private int pdelifee;
	
	private int beReviewed; //후기 있는지 여부
	
	private int cntStatus; //주문상태별 주문 건수 / 주문상태별 상품 총건수
	private double perStatus; //주문상태별 비중 / 주문상태별 상품 비중
	
	private int ordCnt; //일별 주문건수
	private int prdQntSum; //일별 주문상품개수
	
	//테이블엔 없고 Vo에만 있는 값	
	private int totalPaySum;	
	
	// 통화 변환. 3자리 간 쉼표로 분리
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
		
		public String getFmtTotalPay() {
			NumberFormat numberFormat = 
					NumberFormat.getNumberInstance(Locale.getDefault());
			
			return numberFormat.format(totalPay);
		}
		
	//날짜 변환 - 결제내역, 주문목록 조회용
		public String getFmtOdate() {
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd (E) HH:mm", Locale.KOREAN);
	        return sdf.format(odate);
	    }
		
	//날짜 변환 - 통계용
		public String getFmtDateOnly() {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd (E)", Locale.KOREAN);
			return sdf.format(odate);
		}
	//날짜 변환 - 일별 통계용
		public String getFmtOtime() {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd (E)", Locale.KOREAN);
			return sdf.format(otime);
		}
}
