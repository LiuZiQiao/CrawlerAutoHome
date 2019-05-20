package com.lxk.crawler.autohome.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;
@Mapper
public interface CarTestMapper {
    @Select("select title from car_test limit #{start},#{rows}")
    List<String> queryTitleByPage(Map<Object, Object> map);
}
