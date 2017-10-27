package com.imist.realmdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.imist.realmdemo.entity.Dog;
import com.imist.realmdemo.entity.Person;

import javax.annotation.Nullable;

import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Dog dog = new Dog("xiaobai",1,2);
        Realm realm = Realm.getDefaultInstance(); //获取配置的默认realm实例 “myrealm.realm”
        final RealmResults <Dog> results = realm.where(Dog.class).lessThan("age",2).findAll();
        int size = results.size();

        realm.beginTransaction();
        final Dog managedDog = realm.copyToRealm(dog);
        Person person = realm.createObject(Person.class);
        person.getDogs().add(managedDog);
        realm.commitTransaction();

        //添加集合改变的监听
        results.addChangeListener(new OrderedRealmCollectionChangeListener<RealmResults<Dog>>() {
            @Override
            public void onChange(RealmResults<Dog> dogResults, @Nullable OrderedCollectionChangeSet changeSet) {
                changeSet.getInsertions();
            }
        });
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Dog dog = realm.where(Dog.class).equalTo("age",1).findFirst();
                dog.setAge(3);
            }
        },new Realm.Transaction.OnSuccess(){
            @Override
            public void onSuccess() {
                results.size();
                managedDog.getAge();
            }
        });
    }
}
