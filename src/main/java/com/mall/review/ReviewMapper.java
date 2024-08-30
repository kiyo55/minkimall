package com.mall.review;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ReviewMapper {
	
	// 후기 등록--------------------------------------
		//1, 수행
		@Insert("INSERT INTO review "
				+ "(oid, product_code, uid, rcontent) "
				+ "VALUES "
				+ "(#{oid}, #{product_code}, #{uid}, #{rcontent})")
		void addReview(ReviewVo vo);
		
		//2, 해당 oid 상태를 '후기 있음'으로 변경
		@Update("update ord set beReviewed=1 "
				+ "where oid=#{oid}")		
		void oidReviewed(@Param("oid") String oid);
		
		//3, oid에 해당하는 beReviewed 값 조회
		@Select("SELECT beReviewed FROM ord WHERE oid = #{oid}")
		int getBeReviewedByOid(@Param("oid") String oid);		
	// 후기 등록 끝-----------------------------------
	
	//제품별 후기 list 보기
	@Select("SELECT uname, rdate, rcontent "
			+ "FROM review r join users u on r.uid = u.uid "
			+ "WHERE product_code = #{product_code} "
			+ "order by rid desc")	
	List<ReviewVo> reviewList(ReviewVo vo);
	
	//제품별 후기 건수 보기
	@Select("SELECT product_code, COUNT(*) AS review_count "
			+ "FROM review "
			+ "GROUP BY product_code")
	List<ReviewVo> reviewCountList();
	
	//mypage에서 (uid별) 등록된 후기 보기
	@Select("SELECT o.oid, rdate, rcontent, pname, pimgStr, p.product_code "
			+ "FROM review r join products p on r.product_code = p.product_code "
			+ "join ord o on o.oid = r.oid "
			+ "WHERE r.uid=#{uid} "
			+ "and beReviewed=1 "
			+ "order by rid desc")	
	List<ReviewVo> myReview(ReviewVo vo);
	
	//최근 후기 4건
	@Select("SELECT pname, pimgStr, p.product_code, uname, rdate, rcontent "
			+ "FROM review r join products p on r.product_code = p.product_code "
			+ "join users u on r.uid = u.uid "
			+ "order by rdate desc "
			+ "limit 2")	
	List<ReviewVo> recentReview();
		
	
	
		

}
