package com.test.thin.entity;

import java.io.Serializable;

/**
 * @author shishaopeng
 * @project onlytech-server
 * @createDate 13:57 2018/1/3
 * @modfiyDate
 * @function
 */
public class Tree implements Serializable {

    private static final long serialVersionUID = -3325274946276110529L;

    private String name;
    private String age;

    public Tree() {
    }

    public Tree(String name, String age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
