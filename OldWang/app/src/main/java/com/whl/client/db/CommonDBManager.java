package com.whl.client.db;

import android.content.Context;

import com.whl.client.gateway.model.Affiche;

import org.litepal.crud.DataSupport;

import java.util.List;

import static org.litepal.crud.DataSupport.findAll;

/**
 * Created by lenovo on 2016/12/9.
 */

public class CommonDBManager {
    private static final String TAG = "CommonDBManager";

    public CommonDBManager(Context context) {

    }

    public void removeAllData(Class<?> clazz) {
        DataSupport.deleteAll(clazz);
    }

    public void saveAllData(List<? extends DataSupport> list) {
        DataSupport.saveAll(list);
    }

    public List<Affiche> getLocalAfficheList() {
        return findAll(Affiche.class);
    }

}
