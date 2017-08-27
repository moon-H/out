package com.whl.client.gateway.model;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by lenovo on 2016/12/8.
 */

public class Affiche extends DataSupport implements Serializable {
    private int id;
    private String title;
    private int afficheId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getAfficheId() {
        return afficheId;
    }

    public void setAfficheId(int afficheId) {
        this.afficheId = afficheId;
    }

    @Override
    public String toString() {
        return "Affiche{" +
            "title='" + title + '\'' +
            ", afficheId=" + afficheId +
            '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
