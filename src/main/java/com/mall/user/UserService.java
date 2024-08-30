package com.mall.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
	
	@Autowired
    private UserMapper userMapper;
	
	//	login
    public boolean authenticate(String username, String password) {
        int count = userMapper.authenticate(username, password);
        return count > 0;
    }
 
    //가입 시 uid 중복확인
    public boolean duplication(String uid) {
		int count = userMapper.duplication(uid);
		return count > 0;
	}
    
    //가입
    public void join(UserVo vo) {
    	userMapper.join(vo);
	}
  
    //가입인사 위해 사용자명 조회
    public String selectUname(String uid) {
		return userMapper.selectUname(uid);
	}
    
    
    
}
