package com.mall.prd;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Locale;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;
@Data
public class PrdVo {
	private String product_code;    
    private String pname;
    private String pdesc;
    private String pdetail; //상세설명, brochure
    private String category; // 분류 및 추천용
    
    private String pimgStr; // db에는 varchar로 저장
	private MultipartFile pimg; // db엔 없고 백-프론트에만 사용
	
	private Date pdate;
	private boolean isNew; //등록일에 따라 신상 여부 표시용
	
	private String uid; // 사용자 id. 구매 가능 여부 관련
	
	// 검색용
	private String searchCdt;
	private String searchKw;
		
	private int pstock;
	private int pquantity = 1; //db엔 없음. 상세페이지 연산 위함
	
	private int reviewCount; //제품별 후기 건수값 저장용
	
	private int totalQuantity; //제품별 주문량 통계용
	private int totalAmt; //제품별 매출 통계용
	
	private int totalOrd; //총주문량
	private int totalSales; //총매출
	
	private int prdCntByCtgr; //카테고리별 제품 건수
	private double percentage; //카테고리별 제품 비중
		
	//연산 시 int 사용, 표시는 fmt 사용
	private int pprice;
	private int pdelifee;
	
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
	
	public String getFmtTotalAmt() {
		NumberFormat numberFormat = 
				NumberFormat.getNumberInstance(Locale.getDefault());
		
		return numberFormat.format(totalAmt);
	}
	
	public String getFmtTotalSales() {
		NumberFormat numberFormat = 
				NumberFormat.getNumberInstance(Locale.getDefault());
		
		return numberFormat.format(totalSales);
	}
	
	//신상 여부 체크
	public void checkIfNew() {
        LocalDate today = LocalDate.now();
        LocalDate productDate = pdate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        this.isNew = ChronoUnit.DAYS.between(productDate, today) <= 7; //등록일 기준 첫 7일간 '신상' 표시
    }
	
	public boolean isNew() {
        return isNew;
    }
	
	//날짜 변환 - 일별 통계용
		public String getFmtPdate() {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd (E)", Locale.KOREAN);
			return sdf.format(pdate);
		}
		
}
