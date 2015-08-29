package com.jixianxueyuan.dto;

import java.io.Serializable;

/**
 * Created by pengchao on 8/30/15.
 */
public class CourseMinDTO implements Serializable{
    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
