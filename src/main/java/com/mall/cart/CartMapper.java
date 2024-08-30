package com.mall.cart;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface CartMapper {
	
	//A. 장바구니 담기	----------------------------
		//1. 사용자가 동일 제품 담은 레코드 조회
		@Select ("SELECT cartid FROM cart "
				+ "WHERE uid = #{uid} "
				+ "AND isDel=0 "
				+ "AND product_code = #{product_code}")				
				String findCartidByUidAndProductCode(@Param("uid") String uid, 
				@Param("product_code") String product_code);
		
		/* isDel: cart에 있었던 건을 주문.결제 완료 시 가삭제 (isDel=1로 업뎃) 됨에 따라,
		해당 상품 다시 담았을 때 cart가 숨겨지지 않도록 isDel=0 조건 필요 */
		
		//2. 동일 건 없으면 새 레코드 삽입
		@Insert ("INSERT INTO cart (uid, product_code, pquantity) "
				+ "VALUES (#{uid}, #{product_code}, #{pquantity})")				
				void addCart(CartVo vo);
		
		//3. 동일 건 있으면 해당 건의 주문수량 변경
		@Update ("UPDATE cart SET pquantity = pquantity + #{pquantity} "
				+ "WHERE cartid = #{cartid}")				
				void updatePquantity(CartVo vo);
	//장바구니 담기 끝	-----------------------------
	
	//B. uid별 전체 cart 보기
	@Select ("select cartid, uid, c.product_code, pquantity, ctime, "
			+ "pname, pprice, pdelifee, pimgStr, pstock "
			+ "from cart c join products p on c.product_code = p.product_code "
			+ "where uid=#{uid} "
			+ "AND isDel=0 "
			+ "order by ctime desc")	
			List<CartVo> cart (CartVo vo);	

	/*cart 1건만 결제할 경우 - 보류
	@Update("<script>"
	        + "<foreach collection='cartItems' item='item' separator=';'>"
	        + "UPDATE cart SET pquantity=#{item.pquantity} "
	        + "WHERE uid=#{uid} "
	        + "and cartid=#{item.cartid}"
	        + "</foreach>"
	        + "</script>")
	void saveCart(@Param("uid") String uid, @Param("cartItems") List<CartVo> cartItems);
	*/
	
	//C. cart에서 체크항목을 ord로 전달
	@Select("<script>"
            + "SELECT cartid, uid, c.product_code, pquantity, "
            + "pname, pprice, pdelifee "
            + "FROM cart c JOIN products p ON c.product_code = p.product_code "
            + "WHERE uid = #{uid} "
            + "AND cartid IN "
            + "<foreach item='cartId' collection='cartIds' open='(' separator=',' close=')'>"
            + "#{cartId}"
            + "</foreach>"
            + "</script>")
    List<CartVo> getCartItemsByIds(@Param("uid") String uid, 
    		@Param("cartIds") List<String> cartIds);
    // uid와 체크된 cartId 리스트에 해당하는 cart 항목들 조회
	// 해당 사용자가 선택한 장바구니 아이템 목록(CartVo 객체의 리스트)을 반환
	
	//D. del
	@Delete ("delete from cart where cartid=#{cartid}")
			void delCart(CartVo vo);	
	
/*Aa. 바로 구매할 cart 조회
	@Select ("select cartid, uid, c.product_code, pquantity, "
			+ "			pname, pprice, pdelifee, pimgStr, pstock "
			+ "			from cart c join products p on c.product_code = p.product_code "
			+ "			where cartid=#{cartid}")
			CartVo eachCart (@Param("cartid") String cartid); */
}
