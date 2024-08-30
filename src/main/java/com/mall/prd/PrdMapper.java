package com.mall.prd;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

@Mapper
public interface PrdMapper {
		
	//공통-----------------------------------------
		//전체 리스트
		@Select("SELECT * FROM products "
				+ "ORDER BY product_code DESC "
				+ "LIMIT #{pageSize} OFFSET #{offset}")	
		List<PrdVo> prdList(@Param("pageSize") int pageSize, 
				@Param("offset") int offset);
		
		//페이지 분리 위한 총상품수 계산
		@Select("SELECT count(*)"
				+ "    FROM products")
		int totalCount();
		
		//상세보기
		@Select("SELECT * FROM products "
				+ "WHERE product_code = #{product_code}")	
		PrdVo viewPrd(PrdVo vo);
		
		//category별 모아보기
		@Select("SELECT * FROM products "
				+ "WHERE category = #{category} "
				+ "ORDER BY product_code DESC")	
		List<PrdVo> prdListByCtgr(PrdVo vo);
		
		//동일 category의 제품 추천받기. 상세보는 건 제외
		@Select("SELECT * FROM products "
				+ "WHERE category = #{category} "
				+ "AND product_code != #{product_code} "
				+ "ORDER BY product_code DESC "
				+ "limit 3")	
		List<PrdVo> rcmPrdList(@Param("category") String category, 
				@Param("product_code") String product_code);
		
		//카테고리 리스트
		@Select("SELECT category FROM products "
				+ "GROUP BY category")
		List<PrdVo> ctgrList();
		
		//상품 검색
		@SelectProvider(type = PrdSqlProvider.class, method = "buildSeachPrdQuery")
	    List<PrdVo> seachPrd(@Param("searchCdt") String searchCdt, 
	                         @Param("searchKw") String searchKw);		
		
		//신상 top4 - 비출력
		@Select("SELECT product_code, pname, pdate, "
				+ "pimgStr, pprice, pdelifee, category "
				+ "FROM products "
				+ "WHERE DATEDIFF(CURDATE(), pdate) <= 7 "
				+ "ORDER BY pdate DESC "
				+ "LIMIT 4")
		List<PrdVo> topNewPrdList();
		
		//인기 제품 선정: 도착 + 누적 주문량 순으로 정렬 + 후기건수 출력(0값 허용)
		@Select("SELECT "
				+ "    o.product_code, p.pname, pimgStr, pprice, pdelifee, category, "
				+ "    COALESCE(reviewStats.reviewCount, 0) AS reviewCount, "
				+ "    totalQuantity "
				+ "FROM "
				+ "    (SELECT "
				+ "        o.product_code, "
				+ "        SUM(o.pquantity) AS totalQuantity "
				+ "     FROM "
				+ "        ord o "
				+ "     WHERE "
				+ "        o.ostatus = '도착' "
				+ "     GROUP BY "
				+ "        o.product_code "
				+ "    ) o "
				+ "JOIN  "
				+ "    products p ON o.product_code = p.product_code "
				+ "LEFT JOIN "
				+ "    (SELECT "
				+ "         r.product_code, "
				+ "         COUNT(r.rid) AS reviewCount "
				+ "     FROM "
				+ "         review r "
				+ "     GROUP BY  "
				+ "         r.product_code "
				+ "    ) reviewStats ON o.product_code = reviewStats.product_code "
				+ "ORDER BY "
				+ "    totalQuantity DESC, reviewCount desc "
				+ "    LIMIT 4")		
		List<PrdVo> topPrd();
		
	
	//관리자만------------------------------------
		//상품 추가
		@Insert("INSERT INTO products "
				+ "(pname, pdesc, pimgStr, pstock, pprice, pdelifee, pdetail, category) "
				+ "VALUES "
				+ "(#{pname}, #{pdesc}, #{pimgStr}, #{pstock}, "
				+ "#{pprice}, #{pdelifee}, #{pdetail}, #{category})")	 
			void addPrd(PrdVo vo);
	
		//제품별 주문량. 상태 무관
		@Select("SELECT o.product_code, pname, "
				+ "SUM(o.pquantity) AS totalQuantity "
				+ "FROM ord o JOIN products p "
				+ "ON o.product_code = p.product_code "
				+ "GROUP BY o.product_code "
				+ "ORDER BY totalQuantity DESC")		
		List<PrdVo> ordQntByPrd();
		
		//총주문량
		@Select("SELECT SUM(o.pquantity) AS totalOrd "
				+ "FROM ord o JOIN products p "
				+ "ON o.product_code = p.product_code")
		int totalOrd();
		
		//제품별 매출. 상태=도착 건에 한함
		@Select("SELECT o.product_code, pname, pprice, "
				+ "SUM(o.pquantity) AS pquantity, "
				+ "SUM(o.pquantity * p.pprice) AS totalAmt "
				+ "FROM ord o JOIN products p "
				+ "ON o.product_code = p.product_code "
				+ "WHERE ostatus = '도착' "
				+ "GROUP BY o.product_code "
				+ "ORDER BY totalAmt DESC")
		List<PrdVo> salesByPrd();
		
		//총매출
		@Select("SELECT SUM(o.pquantity * p.pprice) AS totalSales "
				+ "FROM ord o JOIN products p "
				+ "ON o.product_code = p.product_code "
				+ "WHERE ostatus = '도착' ")
		int totalSales();
		
		//카테고리별 제품 건수 및 비중
		@Select("SELECT category, "
				+ "COUNT(product_code) AS prdCntByCtgr, "
				+ "ROUND(COUNT(product_code) / (SELECT COUNT(*) FROM products) * 100, 1) AS percentage "
				+ "FROM products "
				+ "GROUP BY category "
				+ "order by percentage desc")
		List<PrdVo> countByCtgr();
		
		
}
