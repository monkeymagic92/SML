package com.sml.quote;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 날짜       : 2022-01-01
 * 시스템      : 시세조회
 * 설명        :
 */
@Mapper
@Repository
public interface QuoteMapper {

	public List<?> selectQuote();

}
