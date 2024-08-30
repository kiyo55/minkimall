package com.mall2.ctrl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mall.user.UserService;
import com.mall.user.UserVo;


@RequestMapping("/user/")
@Controller
public class JoinCtrl {
	
	@Autowired
	private UserService userService;

	@GetMapping("joinForm")
	public String joinForm() {
		System.out.println("joinForm");
		return "user/joinForm";
	}
	
	@GetMapping("checkDup") //uid 중복 체크
	@ResponseBody
	public Map<String, Boolean> checkDuplication(@RequestParam("uid") String uid) {
		System.out.println("checkDup");
		
	    boolean isDuplicate = userService.duplication(uid);
	    Map<String, Boolean> response = new HashMap<>();
	    response.put("isDuplicate", isDuplicate);
	    
	    System.out.println("시도한 id: " + uid);
	    
	    return response;
	}
	
	@PostMapping("join")
	public String join(UserVo vo) {
		System.out.println("join");
		
		// 중복 확인
	    boolean isDuplicate = userService.duplication(vo.getUid());
	    if (isDuplicate) { // 중복된 경우 처리
	        throw new IllegalArgumentException("이미 존재하는 id입니다.");
	    }
	    
		userService.join(vo);
		
		return "redirect:firstLogin?uid=" + vo.getUid();
	}	

	@GetMapping("firstLogin")
	public String firstLogin(@RequestParam("uid") String uid, Model model) {
		System.out.println("firstLogin");
		
		String uname = userService.selectUname(uid);
		model.addAttribute("welcome", uname);
		System.out.println(uname);
		
		return "user/firstLogin";
	}
	
}
