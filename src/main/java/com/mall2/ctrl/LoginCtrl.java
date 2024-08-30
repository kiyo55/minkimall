package com.mall2.ctrl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mall.admin.AdminService;
import com.mall.login.LoginVo;
import com.mall.user.UserService;

import jakarta.servlet.http.HttpSession;



@RequestMapping("/login/")
@Controller
public class LoginCtrl {
	
	@Autowired
	private AdminService adminService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	HttpSession session;
	
	@GetMapping("loginForm")
	public String loginForm() {
		System.out.println("loginForm");
		return "login/loginForm";
	}
	
	@PostMapping("login")
	public String login(LoginVo vo) {
		System.out.println("login");
		
		String username = vo.getUsername();
	    String password = vo.getPassword();
		String loginType = vo.getLoginType();		
		boolean authenticated;
		
		if ("admin".equals(loginType)) { //관리자 로그인
		    authenticated = adminService.authenticate(username, password); //관리자 로그인 정보 비교
		    
		    if (authenticated) { // 관리자 로그인 성공
		    	session.setAttribute("username", username);
		    	session.setAttribute("loginType", loginType);
				System.out.println("관리자 로그인 성공: " + username + " / loginType: " + loginType);
				
				return "redirect:/prd/prdList";
		    }
		    
		} else if ("user".equals(loginType)) { //사용자 로그인
		    authenticated = userService.authenticate(username, password); //사용자 로그인 정보 비교
		    
		    if (authenticated) {	 //사용자 로그인 성공
				session.setAttribute("username", username);
				session.setAttribute("loginType", loginType);
				System.out.println("사용자 로그인 성공: " + username + " / loginType: " + loginType);
				
				return "redirect:/"; //index에 보내기
			}		    
		}
		
		// 로그인 실패 (else 필요x)
			System.out.println("로그인 실패");
			return "login/loginAgain";
	}
	
	@GetMapping("logout")
	public String logout() {
		System.out.println("logout");
		session.invalidate();
		
		return "login/logout";
	}
	
}
