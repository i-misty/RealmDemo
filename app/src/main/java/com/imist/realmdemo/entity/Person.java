package com.imist.realmdemo.entity;

import java.util.UUID;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;
import io.realm.annotations.Required;

/**
 * Created by user10 on 2017/10/27.
 */

/***
 * 除了直接继承于RealmObject来声明 Realm 数据模型之外，还可以通过实现 RealmModel接口并添加 @RealmClass修饰符来声明。
 */
@RealmClass
public class Person extends RealmObject {
    @PrimaryKey //Realm主键不支持自增长用UUID保持唯一性 ;
    private long id ;
    @Required //——表示该字段非空
    private String name ;
    private int age ;
    private int sex;
    private RealmList<Dog> dogs ;
    // 注意 ：如果你创建Model并运行过，然后修改了Model。那么就需要升级数据库，否则会抛异常。
    //@Ignore表示忽略该字段；
    //@Index 表示索引 写入可能会变慢，但是读取操作会变快 ;
    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", sex=" + sex +
                ", dogs=" + dogs +
                ", address='" + address + '\'' +
                '}';
    }

    @Ignore
    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

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
