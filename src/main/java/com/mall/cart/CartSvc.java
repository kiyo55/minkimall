package com.mall.cart;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CartSvc {
	
	@Autowired
	CartMapper cartMapper;
	
	//A. 장바구니 담기
	
		@Transactional
	    public void addOrUpdateCart(CartVo vo) {
			
			//1. 사용자가 동일 제품 담은 레코드 조회 결과의 cartid 가져오기
	        String cartid = cartMapper.findCartidByUidAndProductCode(vo.getUid(), vo.getProduct_code());
	        
	        //2. 동일 건 없으면 새 레코드 삽입
	        if (cartid == null) { 
	            cartMapper.addCart(vo);
	            
	        //3. 동일 건 있으면 해당 건의 주문수량 변경
	        } else { 
	            vo.setCartid(cartid);
	            cartMapper.updatePquantity(vo);
	        }
	    }
	
	//장바구니 담기 끝
	
	//B.
	public List<CartVo> cart (CartVo vo) {
		return cartMapper.cart(vo); //uid별 cart 보기
	}

	/*cart 1건만 결제할 경우 - 보류
	public void saveCart(List<String> cartIds, String uid) {
	    // cartIds에 있는 항목들의 수량을 업데이트하는 로직
	    List<CartVo> cartItems = cartMapper.getCartItemsByIds(uid, cartIds); // cartIds로 CartVo 목록 가져옴
	    cartMapper.saveCart(uid, cartItems);  // 수량 업데이트 (cartItems에서 수량 정보 사용)
	}
	*/
	
	//C. 해당 사용자가 선택한 장바구니 아이템 목록(CartVo 객체의 리스트)을 반환
	public List<CartVo> getCartItemsByIds(String uid, List<String> cartIds) {
        return cartMapper.getCartItemsByIds(uid, cartIds);
    }
	
	//D.
	public void delCart(CartVo vo) {
		cartMapper.delCart(vo);
	}
	
	
	
	//Aa. 바로 구매할 cart 조회
//	public CartVo eachCart (String cartid) {
//		return cartMapper.eachCart(cartid); //(uid별) cartid별 cart 보기
//	}

	
	
	
	
}
