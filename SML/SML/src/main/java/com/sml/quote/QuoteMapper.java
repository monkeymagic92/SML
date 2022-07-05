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

	// (Race) 코인 list
	public List<?> selectCoinRaceList(QuoteVO vo);

	// (Race) 코인 갱신일자 값 가져오기
	public String selectCoinRaceUpdDt();

	// (Race) 코인 저장
	public void insertCoinRace(List<Map<String, Object>> map);




	// (Day)코인 list
	public List<?> selectCoinDayList(QuoteVO vo);

	// (Day) 코인 갱신일자 값 가져오기
	public String selectCoinDayUpdDt();

	// (Day) 코인 저장
	public void insertCoinDay(List<Map<String, Object>> map);

	public List<?> selectCoinDayRank();

}
