package com.straw.lession.physical.vo.item;

/**
 * Created by straw on 2016/7/6.
 */
public class CourseItemInfo {

    /**
     * id
     */
    private long id;

    /**
     * name
     */
    private String name;

    private String location;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
