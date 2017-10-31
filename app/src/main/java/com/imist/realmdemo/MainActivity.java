package com.imist.realmdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;

import com.imist.realmdemo.entity.Dog;
import com.imist.realmdemo.entity.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.Realm;
import io.realm.RealmAsyncTask;
import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Realm详解
 * http://www.jianshu.com/p/37af717761cc
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Realm realm;
    private Button add1 ,add2 ,add3,addAsync ,find1,find2,update,delete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        Dog dog = new Dog("xiaobai",1,2);
        realm = Realm.getDefaultInstance(); //获取配置的默认realm实例 “myrealm.realm”
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (realm != null){
            realm.close();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            /**
            读操作是隐式完成的，也就是说，任何时候你都可以对实体进行访问和查询。
            而所有的写操作(添加，修改，删除)都必须在写事物中完成。
            写事物能够被提交和取消。写操作同时也用于保证线程安全。*/
            /**
             * 需要注意的是，写操作是互斥的。所以，如果我们同时在UI线程和后台线程中创建写操作就有可能导致ANR。
             * 当我们在UI线程创建写事物时，可以使用异步事物来避免ANR的出现。Realm是crash安全的，
             * 所以如果在事物中产生了一个异常，Realm本身是不会被破坏的。不过在当前事物中的数据被丢失，
             * 不过为了避免异常产生的一系列问题，取消事物就非常重要了。如果使用「executeTransaction()」这些操作都会被自动完成。
             由于Realm采用的MVCC架构，在写事物进行的同时，读操作也是被允许的。这也就意味着，除非需要在许多的线程中，
             同时处理许多的并行事务，我们可以使用大型事物，完成许多细粒度的事物。当我们向Realm提交一个写事物时，
             其他的Realm实例都会被通知，并且被自动更新
             */
            case R.id.add1:{
                realm.beginTransaction();
                Person xiaoming = realm.createObject(Person.class,6);
                //Dog dog = new Dog();
                Dog dog = realm.createObject(Dog.class);
                dog.setAge(12);
                dog.setName("xiaobai");
                RealmList<Dog> dogs = new RealmList<Dog>();
                dogs.add(dog);
                xiaoming.setName("xiaoming");
                xiaoming.setSex(1);
                xiaoming.setAge(12);
                xiaoming.setDogs(dogs);
                realm.commitTransaction();//不可以 多次执行插入操作 ;
                // 取消写操作
                // realm.cancelTransaction();
                break;
            }/**
             *****特别注意1. realm.beginTransaction(); realm.commitTransaction();之间不可以new realm对象，必须realm.createObject(Person.class）或者copyToRealm
                         2.如果在UI线程中插入过多的数据，可能会导致主线程拥塞。
             */
            case R.id.add2:{
                Person xiaoming = new Person();
                xiaoming.setId(7);
                xiaoming.setName("xiaohua");
                xiaoming.setSex(0);
                xiaoming.setAge(15);
                RealmList<Dog> dogs = new RealmList<>();
                dogs.add(new Dog("huihui",2));
                xiaoming.setDogs(dogs);

                realm.beginTransaction();
                //当有主键时使用copyToRealmOrUpdate
                Person persion = realm.copyToRealmOrUpdate(xiaoming);
                realm.commitTransaction();
                break;
            }
            /**
             * 我们也可以使用realm.executeTransaction()方法替代手动的跟踪realm.beginTransaction(),
             * realm.commitTransaction()和realm.cancelTransaction()，这个方法自动地处理了begin/commit，和错误发生后的cancel。
             */
            case R.id.add3 :{
                 realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {

                        Person xiaoming = realm.createObject(Person.class);
                        xiaoming.setId(12);
                        xiaoming.setName("xiaohong");
                        xiaoming.setSex(1);
                        xiaoming.setAge(12);
                        RealmList<Dog> dogs = new RealmList<Dog>();
                        xiaoming.setDogs(dogs);
                        realm.copyToRealmOrUpdate(xiaoming);
                    }
                });
                break;
            }/**
             防止后台和ui线程同时调用shiwu
             */
            case R.id.addAsync:{
                RealmAsyncTask transaction  = realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        Person xiaoming = realm.createObject(Person.class,4);
                        xiaoming.setName("dazhui");
                        xiaoming.setSex(0);
                        xiaoming.setAge(18);
                        RealmList<Dog> dogs = new RealmList<Dog>();
                        dogs.add(new Dog("lulu",10));
                        xiaoming.setDogs(dogs);
                        //可以直接使用json转换为对象
                        //realm.createObjectFromJson(City.class, "{ city: \"Copenhagen\", id: 1 }");
                        /**
                         * Realm解析JSON时遵循下面的规则：
                         使用包含null值的JSON创建对象
                         对于非必须字段，设置为默认值null
                         对于必须字段，直接抛出异常
                         使用包含null值的JSON更新对象
                         对于非必须字段，设置为null
                         对于必须字段，直接抛出异常
                         JSON不包含字段
                         保持必须和非必须字段的值不变
                         */
                    }
                }, new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess() {
                        Log.d("Tag","success");
                    }
                }, new Realm.Transaction.OnError() {
                    @Override
                    public void onError(Throwable error) {
                        Log.d("Tag","error"+error.getMessage());
                    }
                });
                break;
            }
            case R.id.find1:{//查询所有;
                RealmResults results = realm.where(Person.class).findAll();

                Log.d("Tag",results.toString());
                RealmResults results2 = realm.where(Person.class).findAllAsync();
                /**
                 * 这里并不会马上查到数据，是有一定延时的。也就是说，你马上使用userList的时候，里面是没有数据的。
                 * 可以注册RealmChangeListener监听器，或者使用isLoaded()方法，判断是否查询完成
                 */
                results2.addChangeListener(new RealmChangeListener<Realm>() {
                    @Override
                    public void onChange(Realm realm) {

                    }
                });
                //或者
                results2.isLoaded();
                break;
            }
            case R.id.find2:{
                //获取第一条数据
                Person person = realm.where(Person.class).findFirst();
                //条件查询
                RealmResults<Person> realmResults = realm.where(Person.class).equalTo("name","huihui").findAll();
                RealmQuery<Person> personRealmQuery = realm.where(Person.class).equalTo("dogs.name","huihui");
                realm.where(Person.class).equalTo("name","huihui").equalTo("dogs.name","huihui").findAll();
                realm.where(Person.class).lessThan("age",10).greaterThan("sex",0).findAllSorted("age", Sort.DESCENDING);
                personRealmQuery.findAllSortedAsync("age",Sort.DESCENDING);
                //4.1不在对查询结果集合进行排序，而是在查询排序返回结果集;
                /**
                 * sum()：对指定字段求和。
                 average()：对指定字段求平均值。
                 min(): 对指定字段求最小值。
                 max() : 对指定字段求最大值。count : 求结果集的记录数量。
                 findAll(): 返回结果集所有字段，返回值为RealmResults队列
                 findAllSorted() : 排序返回结果集所有字段，返回值为RealmResults队列
                 between(), greaterThan(),lessThan(), greaterThanOrEqualTo() & lessThanOrEqualTo()
                 equalTo() & notEqualTo()
                 contains(), beginsWith() & endsWith()
                 isNull() & isNotNull()
                 isEmpty()& isNotEmpty()
                 */
                break;
            }
            case R.id.update:{
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        //1.在shiwu中 将查询结果修改;
                        RealmResults<Person> realmResults = realm.where(Person.class).equalTo("name","huihui").findAll();
                        realmResults.get(0).setName("name");
                        //2.若是设置了主键则 copyToRealmOrUpdate可以更新
                    }
                });
                break;
            }
            case R.id.delete:{
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        //1.使用deleteFromRealm()
                        RealmResults<Person> realmResults = realm.where(Person.class).equalTo("name","huihui").findAll();
                        realmResults.get(0).deleteFromRealm();
                        //2.deleteFromRealm(int index)
                        realmResults.deleteFromRealm(0);

                        /**
                         * userList.deleteFirstFromRealm(); //删除user表的第一条数据
                         userList.deleteLastFromRealm();//删除user表的最后一条数据
                         results.deleteAllFromRealm();//删除user表的全部数据
                         */
                    }
                });

                break;
            }
        }
    }
    private void init(){
        add1 = findViewById(R.id.add1);
        add2 = findViewById(R.id.add2);
        add3 = findViewById(R.id.add3);
        addAsync = findViewById(R.id.addAsync);
        add1.setOnClickListener(this);
        add2.setOnClickListener(this);
        add3.setOnClickListener(this);
        addAsync.setOnClickListener(this);
        find1 = findViewById(R.id.find1);
        find2 = findViewById(R.id.find2);
        update = findViewById(R.id.update);
        delete = findViewById(R.id.delete);
        find1.setOnClickListener(this);
        find2.setOnClickListener(this);
        update.setOnClickListener(this);
        delete.setOnClickListener(this);

    }

    /**
     * 我们可以通过RealmAsyncTask获取一个异步事物的对象，
     * 这个对象可以用在当事物未完成而Activity或者Fragment被销毁时取消事物。
     * 如果在回调中进行了更新UI的操作，而又忘记了取消事物，就会造成crash。
     */
    @Override
    protected void onStop() {
        super.onStop();
         /*if (transaction != null && !transaction.isCancelled()) {
            transaction.cancel();
        }*/
    }
}
