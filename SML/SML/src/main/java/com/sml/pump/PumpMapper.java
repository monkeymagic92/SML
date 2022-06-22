package com.sml.pump;

import com.sml.quote.QuoteVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 날짜       : 2022-06-16 / 목요일 / 오후 6:59
 * 설명       : 일정 상승률 이상을 기록한 코인기록
 */


@Mapper
@Repository
public interface PumpMapper {

	// (Day) Pump 리스트
	public List<?> selectPumpDayList(Map<String, Object> map);

	// (Day) Pump 저장
	public void insertPumpDay(List<Map<String, Object>> map);





	// <!-- 트랜잭션 Test -->
	public List<?> selectTest(Map<String, Object> map);

	public void saveTest(Map<String, Object> map);
	// <!-- 트랜잭션 Test -->


}
