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

	// 매수한 코인 전체 리스트 업데이트
	public void updateDBCoinMyList(List<Map<String, Object>> list) throws Exception;

	// 매수한 코인 전체 리스트
	public List<Map<String, Object>> selectDBCoinMyList() throws Exception;


	// *********** 모바일로 매도시 DB 나의 코인 리스트에는 그대로 남아있으니 업비트와 동기화 ***********
	// API 나의 코인 리스트 삭제 (초기화)
	public void deleteAPICoinMyList() throws Exception;

	// API 나의 코인 리스트 호출할때 MARKET값만 저장
	public void insertApiCoinList(List<Map<String, Object>> map) throws Exception;

	// API 나의 코인리스트와 DB에 저장된 나의 코인리스트중에 없는 코인 체크
	public List<Map<String, Object>> selectChkAPiDbMyList() throws Exception;

	// 나의 코인 리스트에서 업비트 API 리스트와 동기화
	public void deleteReloadMyList(Map<String, Object> map) throws Exception;
	// *****************************************************************************************







	// BTC 코인리스트
	public void insertCoinListBTC(List<Map<String, Object>> map);

	// BTC 코인 가격 update
	public void updateCoinQuoteBTC(List<Map<String, Object>> map);

}
