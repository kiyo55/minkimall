package com.mall.admin;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AdminMapper {
    
	@Select("SELECT COUNT(*) FROM admin "
			+ "WHERE aid = #{username} "
			+ "AND AES_DECRYPT(encryption, 'encryption_key') = #{password}")
	
    int authenticate(@Param("username") String username, 
    		@Param("password") String password);
}

/*
AES_DECRYPT: 해독 - 암호화 해제
AES_ENCRYPT: 암호화
MyBatis를 사용하여 암호화된 비밀번호를 검증하려면 
데이터베이스 쿼리를 통해 암호를 해독하고 비교해야 함
*/
