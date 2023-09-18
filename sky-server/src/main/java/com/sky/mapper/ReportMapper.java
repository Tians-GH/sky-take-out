package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface ReportMapper {

    /**
     * @param map
     * @return
     */
    Double sumByMap(Map map);
}
