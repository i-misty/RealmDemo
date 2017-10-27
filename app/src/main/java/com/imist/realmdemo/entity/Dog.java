package com.imist.realmdemo.entity;

import io.realm.RealmObject;

/**
 * Created by user10 on 2017/10/27.
 */

public class Dog extends RealmObject {
    private String name ;
    private int age;
    private int type;
    public Dog() {
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Dog(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Dog(String name, int age, int type) {
        this.name = name;
        this.age = age;
        this.type = type;
    }
}
