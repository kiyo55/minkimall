package com.mall.ord;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface OrdMapper {		
	
	//사용자 영역------------------------------------------------------------------------------------
	// 주문서 미리보기, 제출준비
	@Select("<script>"
	        + "SELECT cartid, c.uid, c.product_code, pquantity, "
	        + "pname, pprice, pdelifee, pimgStr, "
	        + "uname as oname, utel as otel, umail as omail, "
	        + "uadd as oadd, detailAddress as odetailAddress "
	        + "FROM cart c "
	        + "JOIN products p ON c.product_code = p.product_code "
	        + "JOIN users u ON c.uid = u.uid "
	        + "WHERE c.uid = #{uid} "
	        + "AND cartid IN "
	        + "<foreach item='cartId' collection='selectedCartItems' open='(' separator=',' close=')'>"
	        + "#{cartId}"
	        + "</foreach> "
	        + "ORDER BY c.cartid DESC"
	        + "</script>")
	    List<OrdVo> prepareOrder(@Param("uid") String uid, 
	                             @Param("selectedCartItems") List<String> selectedCartItems);
	
	//--------------------
		// 주문 생성
		@Insert("INSERT INTO ord (paynum, uid, cartid, product_code, pquantity, "
		        + "oname, otel, omail, oadd, odetailAddress, "
		        + "omethod, ostatus, totalPay, pnames) "
		        + "VALUES (#{paynum}, #{uid}, #{cartid}, #{product_code}, #{pquantity}, "
		        + "#{oname}, #{otel}, #{omail}, #{oadd}, #{odetailAddress}, "
		        + "#{omethod}, #{ostatus}, #{totalPay}, #{pnames})")
		void createOrder(OrdVo order);	
	
		// 사용자별 주문된 cart 가삭제 (주문내역에 이동 - Soft Delete)
		@Update("<script>"
		        + "UPDATE cart SET isDel=1 "
		        + "WHERE uid = #{uid} "
		        + "AND cartid IN "
		        + "<foreach item='cartId' collection='cartIds' open='(' separator=',' close=')'>"
		        + "#{cartId}"
		        + "</foreach>"
		        + "</script>")
		void delSelectedCart(@Param("uid") String uid, @Param("cartIds") List<String> cartIds);
	//--------------------
	
	//paynum별 상세 결제내역 보기
	@Select("select paynum, oid, o.uid, o.product_code, pnames, pquantity, "
			+ "totalPay, odate, omethod, ostatus, oname, otel, oadd, beReviewed, "
			+ "uname, utel, "
			+ "pname, pprice, pdelifee, pimgStr "
			+ "from ord o join users u on o.uid = u.uid "
			+ "join products p on o.product_code = p.product_code "				
			+ "where paynum=#{paynum}")
	List<OrdVo> viewOrder(String paynum);		
	
	//uid별 주문목록•결제내역 보기
	@Select("select paynum, oid, o.uid, o.product_code, pnames, pquantity, "
			+ "totalPay, odate, omethod, ostatus, oname, otel, oadd, beReviewed, "
			+ "uname, utel, "
			+ "pname, pprice, pdelifee, pimgStr "
			+ "from ord o join users u on o.uid = u.uid "
			+ "join products p on o.product_code = p.product_code "				
			+ "where o.uid=#{uid} "
			+ "order by odate desc")
	List<OrdVo> orderListByUid(OrdVo vo);
	
	//후기등록 위해 등록 가능한 (도착 + 후기 없는) 주문 보기
		@Select("select oid, pname, pimgStr, p.product_code, odate "
				+ "from ord o join products p "
				+ "on o.product_code = p.product_code "
				+ "where uid=#{uid} "
				+ "and ostatus = '도착' "
				+ "and beReviewed=0 "
				+ "order by oid desc")		
		List<OrdVo> reviewable(OrdVo vo);
	
	//후기 등록할 oid 기준 상품정보 호출
		@Select("select oid, pname, pimgStr, p.product_code, odate "
				+ "from ord o join products p "
				+ "on o.product_code = p.product_code "
				+ "and oid=#{oid} ")		
		OrdVo reviewByOid (OrdVo vo);
		
	//관리자 영역------------------------------------------------------------------------------------
	//전체 결제내역(주문목록) 보기
	@Select("select paynum, oid, o.uid, o.product_code, pquantity, "
			+ "totalPay, odate, ostatus, oname, otime, "
			+ "uname, beReviewed, "
			+ "pname, pprice, pdelifee  "
			+ "from ord o join users u on o.uid = u.uid "
			+ "join products p on o.product_code = p.product_code "
			+ "order by odate desc")
	List<OrdVo> orderList4adm();
	
	//주문상태별 주문 건수 및 비중
	@Select("SELECT ostatus, COUNT(ostatus) as cntStatus, "
			+ "ROUND(COUNT(ostatus) / (SELECT COUNT(*) FROM ord) * 100, 1) AS perStatus "
			+ "FROM ord "
			+ "GROUP BY ostatus "
			+ "ORDER BY perStatus desc")
	List<OrdVo> cntByStatus();
	
	//주문상태별 상품 총건수 및 비중
	@Select("SELECT ostatus, sum(pquantity) as cntStatus, "
			+ "ROUND(sum(pquantity) / (SELECT sum(pquantity) FROM ord) * 100, 1) AS perStatus "
			+ "FROM ord "
			+ "GROUP BY ostatus "
			+ "ORDER BY perStatus desc")
	List<OrdVo> cntPrdByStatus();
	
	//일별 주문건수, 주문상품건수 통계
	@Select("SELECT odate, COUNT(*) AS ordCnt, SUM(pquantity) AS prdQntSum "
			+ "FROM ord "
			+ "GROUP BY odate "
			+ "ORDER BY odate desc")
	List<OrdVo> stcByOdate();
	
	//--------------------
		//1, 주문상태 변경 전 기존상태 확인
		@Select("select ostatus from ord "
				+ "where oid=#{oid}")
		String getOrdStatusByOid(String oid);
		
		//2, 변경 진행
		@Update("update ord set ostatus=#{ostatus} "
				+ "where oid=#{oid}")		
		void updOrdStatus(OrdVo vo);
	//--------------------
		
	// 추후
	@Update("UPDATE ord SET oname = #{oname}, otel = #{otel}, omail = #{omail}, "
			+ "oadd = #{oadd}, odetailAddress = #{odetailAddress} WHERE oid = #{oid}")
	void updateOrder(OrdVo order);		

}
