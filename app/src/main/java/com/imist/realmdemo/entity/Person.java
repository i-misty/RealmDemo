package com.imist.realmdemo.entity;

import java.util.UUID;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by user10 on 2017/10/27.
 */

public class Person extends RealmObject {
    @PrimaryKey //Realm主键不支持自增长用UUID保持唯一性 ;
    private long id = UUID.randomUUID().timestamp();
    private String name ;
    private int age ;
    private int sex;
    private RealmList<Dog> dogs ;

    public Person() {
    }

    public Person(String name, int age, int sex, RealmList<Dog> dogs) {
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.dogs = dogs;
    }

    public Person(long id, String name, int age, int sex, RealmList<Dog> dogs) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.dogs = dogs;
    }

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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public RealmList<Dog> getDogs() {
        return dogs;
    }

    public void setDogs(RealmList<Dog> dogs) {
        this.dogs = dogs;
    }
}
