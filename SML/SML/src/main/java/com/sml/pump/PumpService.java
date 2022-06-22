package com.sml.pump;

import com.nhncorp.lucy.security.xss.CommonUtils;
import com.sml.utils.common.CommonController;
import com.sml.utils.core.BusinessException;
import com.sml.utils.util.Bind;
import com.sml.utils.util.CommonUtil;
import com.sml.utils.util.StringUtil;
import com.sml.utils.util.TimeMaximum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Writer;
import java.util.*;

/**
 * 날짜       : 2022-06-16 / 목요일 / 오후 6:59
 * 설명       : 일정 상승률 이상을 기록한 코인기록
 */


@Service
public class PumpService extends CommonController {

	@Autowired
	private PumpMapper mapper;

	// PUMP 데이터 조회
	public List<?> selectPumpDayList(Map<String, Object> map) {
		return mapper.selectPumpDayList(map);
	}


	// ------ ★ 아래부터 PUMP관련은 패키지 만들어서 새로 Controller만들고 Mybatis Alias sqlmap 등록한뒤 작업하기 ★ ------
	public void insertPumpDay(List<Map<String, Object>> listMap, int pumpSetRisePrice) {

		List<Map<String, Object>> listPumpMap = new ArrayList<>();

		for(int i=0; i<listMap.size(); i++) {

			String market = StringUtil.nullValue(listMap.get(i).get("MARKET"));

			// -------------------- Map 세팅 --------------------
			Double low_price = (Double) listMap.get(i).get("LOW_PRICE");
			Double high_price = (Double) listMap.get(i).get("HIGH_PRICE");
			Double opening_price = (Double) listMap.get(i).get("OPENING_PRICE");
			Double result = (high_price - opening_price) / opening_price * 100;	// 그날 상승률
			Double rise_price = Math.round(result*100)/100.0;					// 상승률을 소수점 2자리 반올림
			String upd_dt = TimeMaximum.nowDate();

			// 매개변수로 받은 'pumpSetRisePrice 상승률' 보다 '실제 상승률'이 높다면
			if(rise_price >= pumpSetRisePrice) {
				Map<String, Object> map = new HashMap<>();

				map.put("MARKET", market);
				map.put("HIGH_PRICE", high_price);
				map.put("OPENING_PRICE", opening_price);
				map.put("LOW_PRICE", low_price);
				map.put("RISE_PRICE", rise_price);
				map.put("PUMP_SET_RISE_PRICE", pumpSetRisePrice);	// 설정한 파라미터를 상승률 몇%이상을 기준으로 잡았는지
				map.put("UPD_DT", upd_dt);

				listPumpMap.add(map);
			}
		}

		// pumpSetRisePrice에 설정한 상승률 이상으로 오른코인이 있다면 PUMP 테이블에 INSERT
		if(listPumpMap.size() > 0) {
			mapper.insertPumpDay(listPumpMap);
		}
	}









	// // <!-- 트랜잭션 Test -->
	public void selectTest(ModelMap model, HttpServletRequest request, HttpServletResponse response, Writer out) throws Exception {

		Bind bind = new Bind(request);
		Map<String, Object> map = bind.getDto();

		List<?> list = mapper.selectTest(map);

		CommonUtil.jsonResponse(response, list);
	}

	public void saveTest(ModelMap model, HttpServletRequest request, HttpServletResponse response, Writer out) throws Exception {

		Bind bind = new Bind(request);
		Map<String, Object> map = bind.getDto();

		mapper.saveTest(map);

		int a = 2;
		if(a == 2) {	// 트랜잭션 테스트
			throw new BusinessException("에러발생@");
		}

		Map<String, Object> map2 = new LinkedHashMap<>();
		map2.put("MSG", "서버에서 날라오는 jsonResponse");

		CommonUtil.jsonResponse(response, map2);
	}
	// // <!-- 트랜잭션 Test -->
}
