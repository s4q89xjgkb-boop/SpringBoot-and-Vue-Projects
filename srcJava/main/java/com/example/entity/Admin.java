package com.example.entity;

/**
 * 管理员信息
 */
public class Admin extends Account {
    private String ids;
    private String[] idsArr;

    public String[] getIdsArr() {
        return idsArr;
    }
    public void setIdsArr(String[] idsArr) {
        this.idsArr = idsArr;
    }

    public String getIds(){
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }
}