package com.mall.ord;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mall.cart.CartMapper;

@Service
public class OrdSvc {
	
	@Autowired
	OrdMapper ordMapper;
	
	@Autowired
	CartMapper cartMapper;
	
	// 주문서 작성 준비 위해 체크된 cart 정보 받아오기
	public List<OrdVo> prepareOrder(String uid, List<String> selectedCartItems) {
        return ordMapper.prepareOrder(uid, selectedCartItems);
	}
	
	//주문 생성	-------
	@Transactional
    public void createOrder(List<OrdVo> orderList, String uid, List<String> cartIds) {
		
		for (OrdVo order : orderList) {
	        // 각 주문을 DB에 삽입. 상세내역은 ctrl에서 처리
	        ordMapper.createOrder(order);       
	    }		
		// 사용자별 주문된 cart 가삭제 (주문내역에 이동 - Soft Delete)
	        ordMapper.delSelectedCart(uid, cartIds);
    }	
	//주문 생성	 끝-----
	
	//paynum별 결제내역 보기
	public List<OrdVo> viewOrder(String paynum) {
	    return ordMapper.viewOrder(paynum);
	}
	
	//uid별 결제내역 보기
	public List<OrdVo> orderListByUid(OrdVo vo) {
		return ordMapper.orderListByUid(vo);
	}
	
	//관리자용 전체 결제내역 보기
	public List<OrdVo> orderList4adm() {
		return ordMapper.orderList4adm();
	}	
	
	//관리자용 주문상태 변경
		//변경 전 기존 상태확인
		public String getOrdStatusByOid(String oid) {
		    return ordMapper.getOrdStatusByOid(oid);
		}
		
		//변경 진행
		public void updOrdStatus(OrdVo vo) {
			ordMapper.updOrdStatus(vo);
		}
	
	//후기등록 위해 등록 가능한 (도착 + 후기 없는) 주문 보기
		public List<OrdVo> reviewable(OrdVo vo) {
			return ordMapper.reviewable(vo);
		}
		
	//후기 등록할 oid 기준 상품정보 호출
		public OrdVo reviewByOid(OrdVo vo) {
			return ordMapper.reviewByOid(vo);
		}
		
	//주문상태별 주문 건수 및 비중
		public List<OrdVo> cntByStatus() {
			return ordMapper.cntByStatus();
		}
		
	//주문상태별 상품 총건수 및 비중
		public List<OrdVo> cntPrdByStatus() {
			return ordMapper.cntPrdByStatus();
		}
		
	//일별 주문건수, 주문상품건수 통계
		public List<OrdVo> stcByOdate() {
			return ordMapper.stcByOdate();
		}
		
		
		
	// 추후	
	public void updateOrder(OrdVo updatedOrder) {
		ordMapper.updateOrder(updatedOrder);
	}
	


}
