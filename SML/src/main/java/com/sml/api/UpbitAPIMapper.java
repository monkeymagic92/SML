package com.sml.api;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Mapper
@Repository
public interface UpbitAPIMapper {

	// KRW 코인리스트
	public void insertCoinListKRW(Map<String, Object> map);

	// KRW 코인 가격 update
	public void updateCoinQuoteKRW(Map<String, Object> map);

	// BTC 코인리스트
	public void insertCoinListBTC(Map<String, Object> map);

	// BTC 코인 가격 update
	public void updateCoinQuoteBTC(Map<String, Object> map);
}
