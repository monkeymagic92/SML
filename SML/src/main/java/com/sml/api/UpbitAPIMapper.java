package com.sml.api;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface UpbitAPIMapper {

	// KRW 코인리스트
	public void insertCoinListKRW(List<Map<String, Object>> map);

	// KRW 코인 가격 update
	public void updateCoinQuoteKRW(List<Map<String, Object>> map);

	// 10시 코인 리스트
	public void insertCoin10KRW(List<Map<String, Object>> map);




	// BTC 코인리스트
	public void insertCoinListBTC(List<Map<String, Object>> map);

	// BTC 코인 가격 update
	public void updateCoinQuoteBTC(List<Map<String, Object>> map);

}
