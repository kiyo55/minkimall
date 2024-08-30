package com.mall.prd;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mall.common.PaginationVo;

@Service
public class PrdSvc {
	
	@Autowired
	PrdMapper prdMapper;
	
	//공통-----------------------------------------
	public List<PrdVo> prdList(int pageSize, int pageNumber) {		
		PaginationVo pagination = new PaginationVo();
	    pagination.setPageSize(pageSize);
	    pagination.setPageNumber(pageNumber);
	    
	    int offset = pagination.getOffset();
	    return prdMapper.prdList(pageSize, offset);
	}
		
	//페이지 분리 위한 총상품수 계산
	public int totalCount() {
		return prdMapper.totalCount();
	}
	
	public PrdVo viewPrd(PrdVo vo) {
		return prdMapper.viewPrd(vo);
	}
	
	//category별 모아보기
	public List<PrdVo> prdListByCtgr(PrdVo vo) {
		return prdMapper.prdListByCtgr(vo);
	}
	
	//동일 category의 제품 추천받기
	public List<PrdVo> rcmPrdList(String category, String product_code) {
		return prdMapper.rcmPrdList(category, product_code);
	}
	
	//카테고리 리스트
	public List<PrdVo> ctgrList() {
		return prdMapper.ctgrList();
	}
	
	//상품 검색
	public List<PrdVo> seachPrd(String searchCdt, String searchKw) {
		return prdMapper.seachPrd(searchCdt, searchKw);
	}
		
	//신상 top4 - 비출력
	public List<PrdVo> topNewPrdList() {
		return prdMapper.topNewPrdList();
	}
	
	//top 제품 선정
	public List<PrdVo> topPrd() {
		return prdMapper.topPrd();
	}
	
	//관리자만------------------------------------
	public void addPrd(PrdVo vo) {
		prdMapper.addPrd(vo);
	}
	
	//제품별 주문량
	public List<PrdVo> ordQntByPrd() {
		return prdMapper.ordQntByPrd();
	}
	
	//제품별 매출
	public List<PrdVo> salesByPrd() {
		return prdMapper.salesByPrd();
	}
	
	//총주문량
	public int totalOrd() {
		return prdMapper.totalOrd();
	}
	
	//총매출
	public int totalSales() {
		return prdMapper.totalSales();
	}
	
	//카테고리별 제품 건수 및 비중
	public List<PrdVo> countByCtgr() {
		return prdMapper.countByCtgr();
	}
	
}
