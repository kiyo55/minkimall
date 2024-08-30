package com.mall2.ctrl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mall.ord.OrdSvc;
import com.mall.ord.OrdVo;
import com.mall.review.ReviewSvc;
import com.mall.review.ReviewVo;

import jakarta.servlet.http.HttpSession;

@RequestMapping("/review/")
@Controller
public class ReviewCtrl {
	
	@Autowired
	ReviewSvc reviewSvc;
	
	@Autowired
	OrdSvc ordSvc;
	
	@Autowired
	HttpSession session;
	
	@GetMapping("myReview")
	public String myReview(Model model, ReviewVo vo) {
		System.out.println("myReview");
		
		String uid = (String) session.getAttribute("username");
		vo.setUid(uid);
		
		List<ReviewVo> li = reviewSvc.myReview(vo);
		model.addAttribute("li", li);
		model.addAttribute("lisize", li.size());
		
		return "review/myReview";
	}
	
	@GetMapping("reviewable")
	public String reviewable(Model model, OrdVo vo) {
		System.out.println("reviewable");
		
        String uid = (String) session.getAttribute("username");
        vo.setUid(uid);
        
        List<OrdVo> li = ordSvc.reviewable(vo);
        model.addAttribute("li", li);
        model.addAttribute("lisize", li.size());
        
        return "review/reviewable";
    }	
	
	@GetMapping("reviewForm")
	public String reviewForm(Model model, OrdVo vo) {
		System.out.println("reviewForm");
		
		String uid = (String) session.getAttribute("username");
		vo.setUid(uid);
		
		model.addAttribute("m", ordSvc.reviewByOid(vo));
		
		return "review/reviewForm";
	}
		
	@PostMapping("addReview")
	public String addReview(ReviewVo vo) {				
        String uid = (String) session.getAttribute("username");
        vo.setUid(uid);
        
        String oid = vo.getOid();
        System.out.println("addReview to oid: " + oid);
        
        reviewSvc.insertReview(vo, oid);

        return "redirect:myReview";
    }	
	
	
}
