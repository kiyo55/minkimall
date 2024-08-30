package com.mall2.ctrl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.mall.common.MaskingUtil;
import com.mall.prd.PrdSvc;
import com.mall.prd.PrdVo;
import com.mall.review.ReviewSvc;
import com.mall.review.ReviewVo;

@Controller
public class StartCtrl {
	
	StartCtrl(){
		System.out.println("StartCtrl 생성자");
	}

	@Autowired
	PrdSvc prdSvc;
	
	@Autowired
	ReviewSvc reviewSvc;
	
	@Value("${api.key.kakaomap}")
	String appKey;
	
	@GetMapping("/")
	public String index(Model model) {
		System.out.println("templates index");
		
		//top제품 리스트
		List<PrdVo> li = prdSvc.topPrd();
		model.addAttribute("rcmList", li);
		
		//카테고리 리스트
	    List<PrdVo> ctgrList = prdSvc.ctgrList();
	    model.addAttribute("ctgrList", ctgrList);	      
	    
	    //최근 후기 4건
	    List<ReviewVo> rctRvLi = reviewSvc.recentReview();
	    model.addAttribute("rctRvLi", rctRvLi);	
	    
	    // 각 후기 작성자 이름 마스킹 처리
	    rctRvLi.forEach(rv -> {
	    	rv.setUname(MaskingUtil.maskName(rv.getUname()));
	    });
	    
		return "index";
	}
	
	@GetMapping("/offlineStore")
	public String offlineStore(Model model) {
		System.out.println("offlineStore");		
		
        model.addAttribute("appkey", appKey);
        model.addAttribute("storeAddr", "서울 서대문구 홍은동 산 11-123");
        model.addAttribute("storeName", "밍키몰");
		
		return "common/offlineStore";
	}
		
}
