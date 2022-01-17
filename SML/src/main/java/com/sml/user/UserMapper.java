package com.sml.user;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface UserMapper {

	public List<?> selUser(Map<String, Object> map) throws Exception;

	public void insertTr1();

	public void insertTr2();

	public void t_test();

}
