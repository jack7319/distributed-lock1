package com.bizideal.mn.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @author : liulq
 * @date: 创建时间: 2018/3/2 10:58
 * @version: 1.0
 * @Description:
 */
public class Sign implements Serializable{

    private int id;
    private int userId;
    private Date createTime;

    public int getId() {
        return id;
    }

    public Sign setId(int id) {
        this.id = id;
        return this;
    }

    public int getUserId() {
        return userId;
    }

    public Sign setUserId(int userId) {
        this.userId = userId;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public Sign setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }
}
