package com.sml.main;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface MainMapper {

    public List<MainVO> selectMain();

    public void insertMain(Map<String, Object> map) throws Exception;

    public void insertMain2(Map<String, Object> map) throws Exception;
}
