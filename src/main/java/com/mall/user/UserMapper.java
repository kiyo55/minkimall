package com.mall.user;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

	//login
    @Select("SELECT COUNT(*) FROM users "
    		+ "WHERE uid = #{username} "
    		+ "AND AES_DECRYPT(upw, 'encryption_key') = #{password}")    
    int authenticate(@Param("username") String username, 
    		@Param("password") String password);
    
    //가입 시 uid 중복확인
    @Select("select count(*) from users where uid=#{uid}")
    int duplication(@Param("uid") String uid);
    
    //가입
    @Insert("insert into users (uid, upw, uname, utel, umail, uadd, detailAddress) values ( "
    		+ "#{uid}, "
    		+ "AES_ENCRYPT(#{upw}, 'encryption_key'), "
    		+ "#{uname}, "
    		+ "#{utel}, "
    		+ "#{umail}, "
    		+ "#{uadd}, "
    		+ "#{detailAddress})")    
    void join(UserVo vo);
    
    //가입인사 위해 사용자명 조회
    @Select("select uname from users where uid=#{uid}")
    String selectUname(@Param("uid") String uid);
    
}

/*
AES_DECRYPT: 해독 - 암호화 해제
AES_ENCRYPT: 암호화
MyBatis를 사용하여 암호화된 비밀번호를 검증하려면 
데이터베이스 쿼리를 통해 암호를 해독하고 비교해야 함
*/
