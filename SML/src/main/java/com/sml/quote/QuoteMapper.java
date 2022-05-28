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

	public List<?> selectQuoteKRWList();	// 값 뿌리기 테스트용

	public String selectThKRWUpdDt();		// 갱신일자 값 가져오기

	public void insertBeforeRaceList(Map<String, Object> map);	// 경주마 테이블에 값 넣기

	// 10시 코인 리스트
	public void insertCoin10KRW(List<Map<String, Object>> map);

}
