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


	// (RACE) Pump 페이지 리스트
	public List<?> selectCoinPumpRaceList(PumpVO vo);

	// (RACE) Pump 헤더
	public Map<String, Object> selectPumpRaceHeader(Map<String, Object> map) throws Exception;

	// (RACE) Pump 리스트
	public List<?> selectPumpRaceList(Map<String, Object> map) throws Exception;

	// (RACE) Pump 저장
	public void insertPumpRace(List<Map<String, Object>> map) throws Exception;





	// (DAY) Pump 페이지 리스트
	public List<?> selectCoinPumpDayList(PumpVO vo);

	// (DAY) Pump 헤더
	public Map<String, Object> selectPumpDayHeader(Map<String, Object> map) throws Exception;

	// (Day) Pump 리스트
	public List<?> selectPumpDayList(Map<String, Object> map) throws Exception;

	// (Day) Pump 저장
	public void insertPumpDay(List<Map<String, Object>> map) throws Exception;





	// <!-- 트랜잭션 Test -->
	public List<?> selectTest(Map<String, Object> map) throws Exception;

	public void saveTest(Map<String, Object> map) throws Exception;
	// <!-- 트랜잭션 Test -->


}
