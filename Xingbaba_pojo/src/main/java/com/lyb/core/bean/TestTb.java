package com.lyb.core.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by lin on 2018/3/13.
 */
public class TestTb implements Serializable{
    public int id;
    public String name;
    public Date birthday;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
}
