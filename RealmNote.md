
[TOC]

### RealmNote 


#### 同步realm:读取realm服务器的realm，而不是指定config的realm

``` 
SyncCredentials myCredentials = SyncCredentials.usernamePassword("bob", "greatpassword", true);
   SyncUser user = SyncUser.login(myCredentials, serverUrl());
   SyncConfiguration config = new SyncConfiguration.Builder(user, realmUrl())
       .schemaVersion(SCHEMA_VERSION)
       .build();
   // Use the config
   Realm realm = Realm.getInstance(config);
   
```

###内存领域:通过inMemory配置，您可以创建一个完全在内存中运行的领域，而不会永久保留到磁盘。
 
```
RealmConfiguration myConfig = new RealmConfiguration.Builder()
        .name("myrealm.realm")
        .inMemory()
        .build();
        
```
 
 >  如果内存不足，内存中的领域可能仍然会使用磁盘空间，但是当领域关闭时，由内存中领域创建的所有文件都将被删除。创建与持久领域同名的内存领域是不允许的 - 名称仍然必须是唯一的。
    当所有具有特定名称的内存中的Realm实例超出范围而没有引用时，就可以释放所有的Realm数据。为了在整个应用程序的执行过程中保持内存中的领域“活着”，请持有对它的引用。

