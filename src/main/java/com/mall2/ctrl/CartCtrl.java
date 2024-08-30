package com.mall2.ctrl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mall.cart.CartSvc;
import com.mall.cart.CartVo;

import jakarta.servlet.http.HttpSession;


@RequestMapping("/cart/")
@Controller
public class CartCtrl {
	
	@Autowired
	CartSvc cartSvc;	
	
	@Autowired
	HttpSession session;
	
	@PostMapping("addCart")
	public String addCart(CartVo vo) {
		System.out.println("addCart");
		
        String uid = (String) session.getAttribute("username");
        vo.setUid(uid);

        cartSvc.addOrUpdateCart(vo);

        return "redirect:cart";
    }
	
	@GetMapping("cart")
	public String cart(Model model, CartVo vo) {
		System.out.println("uid별 cart 보기");

		String uid=(String) session.getAttribute("username");
		vo.setUid(uid);
		
		List<CartVo> li = cartSvc.cart(vo);
		//해당 선언 있어야 lisize 계산됨 (혹은 session.user별 cart.size로 대체 가능)
				
	    model.addAttribute("li", li);	    
	    model.addAttribute("lisize", li.size());
	    		
		return "cart/cart";
	}	
	
	/*cart 1건만 결제할 경우 - 보류
	@PostMapping("saveCart")
	public String saveCart(@RequestParam("cartIds") List<String> cartIds) {
	    System.out.println("saveCart");

	    // 현재 사용자의 uid를 세션에서 가져오기
	    String uid = (String) session.getAttribute("username");
	    
	    cartSvc.saveCart(cartIds, uid);
	    
	    return "redirect:/ord/order?cartIds=" + String.join(",", cartIds);  // 선택된 cartIds를 order 페이지로 전달
	}
	*/
	
	@GetMapping("delCart")
	public String delCart(CartVo vo) {
		System.out.println("delCart id: " + vo.getCartid());
		
		cartSvc.delCart(vo);
		
		String uid = (String) session.getAttribute("username");
        vo.setUid(uid);

		return "redirect:cart";
	}
	
}
