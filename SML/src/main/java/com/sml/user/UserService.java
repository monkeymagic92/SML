package com.sml.user;

import com.sml.utils.core.BeanUtil;
import com.sml.utils.encriyp.NaraARIAUtil;
import com.sml.utils.util.Bind;
import com.sml.utils.util.CommonUtil;
import com.sml.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * 프로그램명  : UserServiceImpl
 * 날짜       : 2022-02-13 / 일요일 / 오후 6:57
 * 설명       :
 */

@Service
public class UserService {

	@Autowired
	private UserMapper mapper;

	/**
	 * 로그인 유저 전체 데이터
	 * @param model
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public Map<String, Object> selectUserInfo(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		Bind bind = new Bind(request);
		Map<String, Object> map = bind.getDto();

		CommonUtil.mapInfo(map); //요청 데이터 로그 보기

		// request로 받은 비밀번호 암호화 작업
		String privateKey = "SML@200";
		String pw = StringUtil.nullValue(map.get("LOGIN_PW"));
		String encryPw = NaraARIAUtil.ariaEncrypt(pw, privateKey);

		Map<String, Object> userInfo = mapper.selectUserInfo(map);

		if(userInfo == null) {
			map.put("message", "ID를 다시 확인해 주세요.");

		} else {
			if(encryPw.equals(userInfo.get("USER_PW"))) {	// 로그인 성공

				userInfo.put("USER_PW", "");

				HttpSession session = request.getSession();
				session.setAttribute("userList", userInfo);
				map.put("message", "success");

			} else {
				map.put("message", "비밀번호를 다시 확인해 주세요.");
			}
		}
		return map;
	}

	/**
	 * 회원가입
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> signUp(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		Bind bind = new Bind(request);
		Map<String, Object> map = bind.getDto();

		CommonUtil.mapInfo(map); //요청 데이터 로그 보기

		// map타입을 vo로 변환
		UserVO vo = new UserVO();
		BeanUtil.populate(vo, map);

		// 비밀번호 Aria 암호화 적용
		String privateKey = "SML@200";
		String pw = StringUtil.nullValue(map.get("USER_PW"));
		String encryPw = NaraARIAUtil.ariaEncrypt(pw, privateKey);

		// *************** ID / Email 중복검사 Start ****************
		int idChk = mapper.selectChkId(map);
		int emailChk = mapper.selectChkEmail(map);
		// *************** ID / Email 중복검사 End ****************

		Map<String, Object> resultMap = new HashMap<>();

		if(idChk > 0) {				// 아이디 중복체크
			resultMap.put("message", "failId");

		} else if(emailChk > 0) {	// 이메일 중복체크
			resultMap.put("message", "failEmail");

		} else {					// 회원가입 성공
			vo.setUSER_PW(encryPw);
			mapper.insertUserId(vo);
			resultMap.put("message", "success");
		}

		return resultMap;
	}
}
