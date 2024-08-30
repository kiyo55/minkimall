package com.mall.review;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReviewSvc {
	
	@Autowired
	ReviewMapper reviewMapper;
	
	@Transactional
	public void insertReview(ReviewVo vo, String oid) {
		reviewMapper.addReview(vo); //후기 등록 수행
		reviewMapper.oidReviewed(oid); // 해당 oid 상태를 '후기 있음'으로 변경
		
		// 변경된 beReviewed 값을 조회
	    int beReviewed = reviewMapper.getBeReviewedByOid(oid);
	    System.out.println("beReviewed: " + beReviewed);
	}

	public List<ReviewVo> reviewList(ReviewVo vo) {
		return reviewMapper.reviewList(vo);
	}
	
	//제품별 후기 건수 보기
	public List<ReviewVo> getReviewCountList() {
	    return reviewMapper.reviewCountList();
	}
	
	public List<ReviewVo> myReview(ReviewVo vo) {
		return reviewMapper.myReview(vo);
	}
	
	//최근 후기 4건
	public List<ReviewVo> recentReview() {
		return reviewMapper.recentReview();
	}
	
	
	
	
	

	
	
	
}
