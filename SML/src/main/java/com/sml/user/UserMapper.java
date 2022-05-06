package com.sml.user;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Mapper
@Repository
public interface UserMapper {

	/**
	 * 로그인 유저 전체 데이터
	 * @param map
	 * @return
	 */
	public Map<String, Object> selectUserInfo(Map<String, Object> map);

	/**
	 * 아이디 중복검사
	 * @param map
	 * @return
	 */
	public int selectChkId(Map<String, Object> map);

	/**
	 * 이메일 중복검사
	 * @param map
	 * @return
	 */
	public int selectChkEmail(Map<String, Object> map);

	/**
	 * 회원가입
	 * @param vo
	 */
	public void insertUserId(UserVO vo);

}
