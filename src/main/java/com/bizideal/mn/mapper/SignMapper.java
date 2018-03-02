package com.bizideal.mn.mapper;

import com.bizideal.mn.entity.Sign;
import com.bizideal.mn.config.MyMapper;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author : liulq
 * @date: 创建时间: 2018/3/2 11:13
 * @version: 1.0
 * @Description:
 */
public interface SignMapper extends Mapper<Sign> {

    List<Sign> selectToday();
}