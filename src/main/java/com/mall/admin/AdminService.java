package com.mall.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
	
	@Autowired
    private AdminMapper adminMapper;

    public boolean authenticate(String username, String password) {
        int count = adminMapper.authenticate(username, password);
        return count > 0;
    }

}
