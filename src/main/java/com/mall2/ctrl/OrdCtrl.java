package com.mall2.ctrl;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mall.cart.CartSvc;
import com.mall.cart.CartVo;
import com.mall.ord.OrdSvc;
import com.mall.ord.OrdVo;
import com.mall.ord.OrderRequestDto;

import jakarta.servlet.http.HttpSession;

@RequestMapping("/ord/")
@Controller
public class OrdCtrl {
	
	@Autowired
	OrdSvc ordSvc;
	
	@Autowired
	CartSvc cartSvc;
		
	@Autowired
	HttpSession session;
	
	@Value("${api.key.payment}")
	String payKey;
	
	@PostMapping("order")
	public String order(Model model, @RequestParam List<String> cartIds) {
	    System.out.println("cart에서 order로 이동");

	    String uid = (String) session.getAttribute("username");
	    
	    // 체크된 항목만 가져오기
	    // uid, cartIds를 사용하여 해당 사용자가 선택한 장바구니 아이템 목록(CartVo 객체의 리스트)을 반환
	    List<CartVo> selectedCartItems = cartSvc.getCartItemsByIds(uid, cartIds);
	    System.out.println("체크된 cart 건수: " + selectedCartItems.size());
	    
	    // 주문서 생성 전에 필요한 정보 조회 (Cart, User, Product 포함)
	    List<OrdVo> orderItems  = ordSvc.prepareOrder(uid, cartIds);	    
	    System.out.println("체크된 cartIds: " + cartIds); // [C00034, ...]
	    
	    // 모델에 선택된 아이템 목록 추가
	    if (!orderItems.isEmpty()) {
	        model.addAttribute("order", orderItems.get(0)); // 사용자정보 표시 위해 첫 항목 사용
	        model.addAttribute("orderItems", orderItems);	
	        
	        //총계값 전달
	        model.addAttribute("totalPay", calculateTotal(orderItems));
	        model.addAttribute("fmtTotalPay", formatNumber(calculateTotal(orderItems)));
	        
	        //소계값 전달
	        model.addAttribute("hapList", calculateHap(orderItems));
	        model.addAttribute("fmtHapList", orderItems.stream()
	    	    .map(item -> formatNumber(item.getPprice() * item.getPquantity() + item.getPdelifee()))
	    	    .collect(Collectors.toList()));	        
	        
	        model.addAttribute("payKey", payKey); //고객사 식별코드
	        
	        if (orderItems.size() > 1) {
	        	String pnames = orderItems.get(0).getPname() + " 외 " + (orderItems.size() - 1) + "건";
		        model.addAttribute("pnames", pnames);
		        System.out.println("pnames: " + pnames);
		        
	        } else if (orderItems.size() == 1) {
	            // orderItems.size()가 1일 때는 "외 0건"이 아닌, 단순히 그 상품명만 표시
	            String pnames = orderItems.get(0).getPname();
	            model.addAttribute("pnames", pnames);
	            System.out.println("pnames: " + pnames);
	        }	        
	    }
	    
	    System.out.println("orderItems: " + orderItems);
	    System.out.println("totalPay: " + calculateTotal(orderItems));
	    System.out.println("이동 끝");
	    
	    return "ord/orderForm";
	}
		
	@ResponseBody // 비동기 통신 통해 돌아가기 위해 필수
	@PostMapping("submitOrder")
	public String submitOrder(@RequestBody OrderRequestDto orderRequest) {
	    System.out.println("submitOrder");
	    
	    //주문내역으로 이동할 cart 가삭제 위한 준비
	    String uid = orderRequest.getUid();
	    List<String> cartIds = orderRequest.getCartidList();
	    
	    //order 레코드 추가. js 통해 받은 값(getCartidList) 반영
	    List<OrdVo> orderList = new ArrayList<>();
	    for (int i = 0; i < orderRequest.getCartidList().size(); i++) {
	        OrdVo order = new OrdVo();	        
	        order.setPaynum(orderRequest.getPaynum());
	        order.setCartid(orderRequest.getCartidList().get(i));
	        order.setProduct_code(orderRequest.getProductCodeList().get(i));
	        order.setPquantity(orderRequest.getPquantityList().get(i));	        
	        order.setUid(orderRequest.getUid()); 
	        order.setPnames(orderRequest.getPnames());
	        order.setOname(orderRequest.getOname());
	        order.setOtel(orderRequest.getOtel());
	        order.setOmail(orderRequest.getOmail());
	        order.setOadd(orderRequest.getOadd());
	        order.setOdetailAddress(orderRequest.getOdetailAddress());
	        order.setOmethod(orderRequest.getOmethod());
	        order.setOstatus(orderRequest.getOstatus());
	        order.setTotalPay(orderRequest.getTotalPay());
	        
	        orderList.add(order);
	    }

	    //주문 처리
	    ordSvc.createOrder(orderList, uid, cartIds);

	    System.out.println("주문, 결제 완료: " + orderList);
	    System.out.println("가삭제된 cartid: " + orderRequest.getCartidList());
	    
	    // 결제내역 보러가기
	    return "viewOrder?paynum=" + orderRequest.getPaynum();    	    	    
	}
	
	@GetMapping("viewOrder")
	public String viewOrder(Model model, @RequestParam("paynum") String paynum) {
	    System.out.println("viewOrder");
	    
	    //방금 주문한 내역 리스트로 가져오기
	    List<OrdVo> orderList = ordSvc.viewOrder(paynum);
	    System.out.println("paynum: " + paynum);
	    
	    // 모델에 주문 리스트 추가: product_code, pname, pprice, pdelifee, pquantity
	    model.addAttribute("orderList", orderList);
	    
	    // 공통 정보 (첫 주문 항목 기준)	    
	    if (!orderList.isEmpty()) {
	    	OrdVo order = orderList.get(0);
	    	model.addAttribute("order", order);
	    }
	    	    
    	return "ord/viewOrder";
	}
	
	@GetMapping("myOrder")
	public String orderListByUid(Model model, OrdVo vo) {
	    System.out.println("orderListByUid");
	    
	    String uid=(String) session.getAttribute("username");
		vo.setUid(uid);
	    
	    // uid별 결제내역 가져오기
	    List<OrdVo> orderListByUid = ordSvc.orderListByUid(vo);        
	    
	    // paynum 기준으로 정보 저장할 Map 생성
	    Map<String, Map<String, Object>> ordersByPaynum = new LinkedHashMap<>();
	    
	    for (OrdVo order : orderListByUid) {
	        String paynum = order.getPaynum();
	        if (!ordersByPaynum.containsKey(paynum)) {
	        	
	            // paynum별 첫 주문정보 초기화
	            Map<String, Object> orderInfo = new HashMap<>();
	            orderInfo.put("odate", order.getFmtDateOnly());
	            orderInfo.put("totalPay", order.getFmtTotalPay());
	            orderInfo.put("products", new ArrayList<OrdVo>());
	            ordersByPaynum.put(paynum, orderInfo);
	        }
	        
	        // 경고 억제
	        @SuppressWarnings("unchecked")
	        List<OrdVo> products = (List<OrdVo>) ordersByPaynum.get(paynum).get("products");

	        // 해당 paynum에 속하는 주문상품을 리스트에 추가
	        products.add(order);
	    }
	    
	    // 모델에 그룹화된 주문 리스트 추가
	    model.addAttribute("ordersByPaynum", ordersByPaynum);
	    
	    return "ord/myOrder";
	}
	
	@GetMapping("orderList4adm") //관리자용 전체 결제내역(주문목록) 보기
	public String orderList4adm(Model model, OrdVo vo) {
		System.out.println("orderList4adm");
		
		//전체 목록
		List<OrdVo> li=ordSvc.orderList4adm();		
		model.addAttribute("li", li);
		model.addAttribute("lisize", li.size());
		
		//주문상태별 주문 건수 및 비중
		List<OrdVo> cntLi=ordSvc.cntByStatus();
		model.addAttribute("cntLi", cntLi);
		
		return "ord/orderList4adm";
	}
	
	@PostMapping("updOrdStatus")
	public String updOrdStatus(OrdVo vo) {
		System.out.println("updOrdStatus");

		// 업데이트 전의 상태를 조회
	    String before = ordSvc.getOrdStatusByOid(vo.getOid());
	    System.out.println("변경할 oid: " + vo.getOid());
	    System.out.println("변경전: " + before);
	    
		ordSvc.updOrdStatus(vo);		
		
		// 업데이트 후의 상태를 조회
	    String after = ordSvc.getOrdStatusByOid(vo.getOid());
	    System.out.println("변경후: " + after);
	    
		return "redirect:orderList4adm";
	}
	
	@GetMapping("cntPrdByStatus")
	public String cntPrdByStatus(Model model) {
		System.out.println("cntPrdByStatus");
				
		//주문상태별 상품 총건수 및 비중
		List<OrdVo> cntLi = ordSvc.cntPrdByStatus();
		model.addAttribute("cntLi", cntLi);
		
		// Labels (ostatus)와 데이터 (cntStatus)를 추출하여 JSON 형태로 전달
        List<String> labels = cntLi.stream().map(OrdVo::getOstatus).collect(Collectors.toList());
        List<Integer> dataValues = cntLi.stream().map(OrdVo::getCntStatus).collect(Collectors.toList());

        model.addAttribute("labels", labels);
        model.addAttribute("dataValues", dataValues);		
				
		return "ord/cntPrdByStatus";
	}
	
	@GetMapping("stcByOdate")
	public String stcByOdate(Model model) {
		System.out.println("stcByOdate");
		
		//주문상태별 상품 총건수 및 비중
		List<OrdVo> li = ordSvc.stcByOdate();
		model.addAttribute("li", li);
		
		return "ord/stcByOdate";
	}
	
	//---------------------------------------------------------------
	//총계 계산식
	private int calculateTotal(List<OrdVo> orderItems) {
	    return orderItems.stream().mapToInt(item -> 
	        item.getPprice() * item.getPquantity() + item.getPdelifee()).sum();
	}
	
	//소계 계산식	
	private List<Integer> calculateHap(List<OrdVo> orderItems) {
	    return orderItems.stream()
	        .map(item -> item.getPprice() * item.getPquantity() + item.getPdelifee())
	        .collect(Collectors.toList());
	}
	
	//천단위 콤마 fmt - vo/front보다 ctrl에서 처리할 것 권장됨
	private String formatNumber(int number) {
	    NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());
	    return numberFormat.format(number);
	}

}
