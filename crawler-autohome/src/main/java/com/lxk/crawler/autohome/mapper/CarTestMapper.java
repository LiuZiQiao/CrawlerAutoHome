package com.lxk.crawler.autohome.mapper;

import com.lxk.crawler.autohome.pojo.CarTest;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;
@Mapper
public interface CarTestMapper {
    @Select("select title from car_test limit #{start},#{rows}")
    List<String> queryTitleByPage(Map<Object, Object> map);

    @Insert("INSERT INTO `car_test` (" +
            "	`title`," +
            "	`test_speed`," +
            "	`test_brake`," +
            "	`test_oil`," +
            "	`editor_name1`," +
            "	`editor_remark1`," +
            "	`editor_name2`," +
            "	`editor_remark2`," +
            "	`editor_name3`," +
            "	`editor_remark3`," +
            "	`image`," +
            "	`created`," +
            "	`updated`" +
            ")" +
            "VALUES" +
            "	(" +
            "		#{title}," +
            "		#{test_speed}," +
            "		#{test_brake}," +
            "		#{test_oil}," +
            "		#{editor_name1}," +
            "		#{editor_remark1}," +
            "		#{editor_name2}," +
            "		#{editor_remark2}," +
            "		#{editor_name3}," +
            "		#{editor_remark3}," +
            "		#{image}," +
            "		#{created}," +
            "		#{updated}" +
            "	)"
    )
    void saveCarTest(CarTest carTest);

//    @Select("select title from car_test")
//    public  queryTitle();
}
