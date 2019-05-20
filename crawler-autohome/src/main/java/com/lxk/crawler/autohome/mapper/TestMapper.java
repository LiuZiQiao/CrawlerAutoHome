package com.lxk.crawler.autohome.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TestMapper {
    /**
     * 查询数据库时间
     *
     * @return
     */
    @Select("SELECT NOW()")
    public String queryDate();

}
