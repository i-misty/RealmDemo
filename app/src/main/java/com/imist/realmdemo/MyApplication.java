package com.imist.realmdemo;

import android.app.Application;
import android.content.Context;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by user10 on 2017/10/27.
 */

public class MyApplication extends Application {
    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        //全局context对象；
        context = getApplicationContext();
        Realm.init(context);
        //配置默认realm配置；
        RealmConfiguration config = new RealmConfiguration.Builder().name("myrealm.realm").build();
        Realm.setDefaultConfiguration(config);
        //获取默认配置的realm实例 ；
        /*RealmConfiguration config = Realm.getDefaultConfiguration();
        Realm defaultRealm = Realm.getInstance(config);*/
        //Realm实例是线程单例，这意味着静态构造函数将返回相同的实例以响应给定线程的所有调用。
        /*RealmConfiguration configuration = new RealmConfiguration.Builder()
                .name("myrealm.realm")//realm的名字
                .schemaVersion(1)//realm的版本;
                .inMemory()   //在内存中保存，但是当内存不足时会存在磁盘中，退出程序会删掉；
                .modules(null)
                .migration(null)
                .encryptionKey("".getBytes())//加秘钥；
                .build();
        //获取其他realm实例;
        Realm defaultRealm = Realm.getInstance(configuration);*/
    }
    private static Context getContextInstance(){
        return context;
    }
}
