package com.sml.quote;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 날짜       : 2022-01-01
 * 시스템      : 시세조회
 * 설명        :
 */
@Mapper
@Repository
public interface QuoteMapper {

	// *********************** 10:00 (RACE) 코인 Start ***********************

	// 10:00am (RACE) 코인 리스트
	public List<?> selectCoinRaceList();

	// 10:00am (RACE) 코인 갱신일자
	public String selectCoinRaceUpdDt();

	// 10:00am 코인 저장
	public void insertCoinRace(List<Map<String, Object>> map);

	// *********************** 10:00 (RACE) 코인 End ***********************



	// *********************** 08:55 (DAY) 코인 Start ***********************

	// DAY 코인 리스트
	public List<?> selectCoinDayList();

	// DAY 코인 갱신일자
	public String selectCoinDayUpdDt();

	// DAY 코인 저장
	public void insertCoinDay(List<Map<String, Object>> map);

	// *********************** 08:55 (DAY) 코인 End ***********************

}
