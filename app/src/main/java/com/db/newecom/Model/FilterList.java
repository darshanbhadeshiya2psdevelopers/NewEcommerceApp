package com.db.newecom.Model;

import java.io.Serializable;

public class FilterList implements Serializable {

    private String name;
    private String type;
    private String isShow;

    public FilterList(String name, String type, String isShow) {
        this.name = name;
        this.type = type;
        this.isShow = isShow;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIsShow() {
        return isShow;
    }

    public void setIsShow(String isShow) {
        this.isShow = isShow;
    }

}
