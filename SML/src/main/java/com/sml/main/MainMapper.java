package com.sml.main;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface MainMapper {

    /**
     * 기본 main Test 값 뿌리기
     * @return
     */
   public List<MainVO> selectMain();

}
