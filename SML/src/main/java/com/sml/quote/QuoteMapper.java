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

	// 값 뿌리기 테스트용
	public List<?> selectCoinRaceList();

	// 갱신일자 값 가져오기
	public String selectCoinRaceUpdDt();

	// 10시 코인 리스트
	public void insertCoinRace(List<Map<String, Object>> map);

}
