package com.sml.api;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Mapper
@Repository
public interface UpbitAPIMapper {

	public void insertCoinList(Map<String, Object> map);

	public void updateCoinQuote(Map<String, Object> map);
}
