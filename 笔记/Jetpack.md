JetPack是Google在2018年推出的一个全新的开发组件工具集，并将Architecture Components作为它的一部分融入其中。
它的组件大部分不依赖于任何Android系统版本，通常是定义在AndroidX库中的，拥有非常好的向下兼容性。

<br>
<br>

## Jetpack

![](https://img-blog.csdnimg.cn/20200708224514617.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0NTRE5fRGFTaHVpTml1,size_16,color_FFFFFF,t_70)

主要由基础、架构、行为、界面这4各部分组成。

### ViewModle（存放与界面相关的数据）
专门用于存放与界面相关的数据，ViewModle的生命周期和Activtiy不同，只有当Activity退出时才会跟着Actiivty一起销毁。所以它可以保证手机屏幕在发生旋转的时候不会被重新创建，将域界面相关的数据存储到其中，这样在手机屏幕发生旋转时，屏幕上显示的数据也不会丢失。

![说明 ViewModel 随着 Activity 状态的改变而经历的生命周期。](https://developer.android.google.cn/images/topic/libraries/architecture/viewmodel-lifecycle.png?hl=zh_cn)

#### 基本使用

添加依赖
```xml
implementation 'androidx.lifecycle:lifecycle-extensions:2.2.0'
```

给每一个Activity和Fragment都创建一个对应的ViewModel

MainViewModel
```kotlin
//MainActivity对应的ViewModel，存放与MainActivity对应界面相关的数据
class MainViewModel :ViewModel(){
    //用于计数
    var counter=0
}
```

注意获取ViewModel实例是使用的ViewModelProvider
```kotlin
class MainActivity : AppCompatActivity() {

    private lateinit var viewModel:MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //不能直接创建ViewModel实例，因为ViewModel有独立的生命周期，直接创建的话每次执行onCreate()都会创建一个新的ViewModel
        //采用ViewModelProvider来获取ViewModel实例
        viewModel=ViewModelProvider(this).get(MainViewModel::class.java)
        plusOneBtn.setOnClickListener {
            viewModel.counter++ //计数器加1
            refreshCounter()
        }

        refreshCounter()
    }

    //设置新的数据
    private fun refreshCounter(){
        infoText.text=viewModel.counter.toString();
    }
}
```
#### 使用ViewModel传递参数（ViewModelProvider.Factory）

因为我们是通过ViewModelProvider获取ViewModel实例时的，所以没办法直接使用实例化viewModel时进行传参
所以在我们使用ViewModelProvider获取ViewModel实例时，将ViewModelProvider.Factory作为参数传入ViewModelProvider
其大概过程就是通过ViewModelProvider.Factory来实例化有参的ViewModel


**首先创建有参的MainViewModel**
```kotlin
class MainViewModel(countReserved:Int) :ViewModel(){
    //用于计数
    var counter=countReserved
}
```

**然后需要新建一个MainViewFactoty类**
```kotlin
//新建一个ViewModelFactory类实现ViewModelProvider.Factory接口
class MainViewModelFactory(private val countReserved:Int) : ViewModelProvider.Factory{
    //必须实现create()方法，该方法中创建了MainViewModel并将countReserved参数传入
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(countReserved) as T
    }
}
```


**使用**
```koltin
class MainActivity : AppCompatActivity() {

    private lateinit var viewModel:MainViewModel
    lateinit var sp: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sp=getPreferences(Context.MODE_PRIVATE)
        //读取保存的值
        val countReserved=sp.getInt("count_reserved",0)
        //不能直接创建ViewModel实例，因为ViewModel有独立的生命周期，直接创建的话每次执行onCreate()都会创建一个新的ViewModel
        //采用ViewModelProvider来获取ViewModel实例
        //传入一个MainViewModelFactory，并将countReserved传递给它
        viewModel=ViewModelProvider(this,MainViewModelFactory(countReserved)).get(MainViewModel::class.java)
        plusOneBtn.setOnClickListener {
            viewModel.counter++ //计数器加1
            refreshCounter()
        }

        refreshCounter()
    }

    override fun onPause() {
        super.onPause()
        sp.edit {
            //保存数值
            putInt("count_reserved",viewModel.counter)
        }
    }

    //设置新的数据
    private fun refreshCounter(){
        infoText.text=viewModel.counter.toString();
    }
}
```
<br>

### Lifecycles（感知Activity的生命周期）

Lifecycles能让任何一个类都能轻松感知到Acitvity的生命周期，同时又不需要在Activity中编写大量的逻辑处理。

**新建一个MyObserver类实现LifecycleObserver接口**
通过注解感知Activity的生命周期
 @OnLifecycleEvent注解的种类：
 ||||
 ----|---|---|
 ON_CREATE|ON_START|ON_RESUME|
 ON_PAUSE|ON_STOP|ON_DESTROY
 ON_ANY表示可以匹配Activity的任何生命周期回调
```kotlin
class MyObserver : LifecycleObserver{

    //Activity的onStart()触发时执行
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun activityStart(){
        Log.d("MyObserver","activityStart");
    }

    //Activity的onStop()触发时执行
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun activityStop(){
        Log.d("MyObserver","activityStart");
    }

}
```

**调用LifecycleOwner的getLifecycle()方法得到有个Lifecycle对象，再调用addObserver观察LifecycleOwner生命周期**
```kotlin
//Activity、Fragment本身就是一个LifecycleOwner，所以可以直接使用getLifecycle
lifecycle.addObserver(MyObserver())
```

**主动获取生命周期**
在MyObserver的构造函数中传入Lifecycle，就可以使用lifecycle.currentState返回当前的生命状态
```kotlin
class MyObserver (val lifecycle: Lifecycle) : LifecycleObserver{
   .....
}
```
### LiveData（数据发生变化时通知观察者）
LiveData是一种响应式编程组件，它可以包含任何类型的数据，并在数据发生变化时通知给观察者。
一般来说我们配合是ViewModel，使用在ViewModel其中。
原本我们使用的是在Activity中手动获取ViewModel中的数据，现在我们采用LiveData可以使ViewModel将数据的变化主动通知给Activity。

MutableLiveData是一种可变的LiveData，它有三种数据读写的方式。getValue()用于获取LiveData中包含的数据；setValue()用于给Livedata设置数据，只能在主线程调用；postVaule()方法用于在非主线程中给LiveData设置数据。

**MainViewModel**
```kotlin
//MainActivity对应的ViewModel，存放与MainActivity对应界面相关的数据
//countReserved用于存放之前保存的数据值
class MainViewModel(countReserved:Int) :ViewModel(){
    //将counter变量定义为MutableLiveData对象
    //MutableLiveData对象是一种可变的LiveData
    val counter=MutableLiveData<Int>()

    init{
        //设置数据
        counter.value=countReserved
    }

    fun plusOne(){
        //获取counter中包含的数据
        val count=counter.value?:0
        //对其数据加一,再重新设置给counter
        counter.value=count+1
    }

    fun clear(){
        counter.value=0
    }

}
```
**MainActivity**
```kotlin
class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        ......
        //数值加一
        plusOneBtn.setOnClickListener {
            viewModel.plusOne()
        }

        //数值清零
        clearBtn.setOnClickListener {
            viewModel.clear()
        }

        //调用observe来观察数据
        //第一个参数是LifecycleOwner对象，第二个参数是一个Observer接口
        //当counter中包含的数据发生变化时，就会回调到这里
        viewModel.counter.observe(this, Observer { count ->
            infoText.text=count.toString()
        })
    }

    override fun onPause() {
        super.onPause()
        sp.edit {
            //保存数值
            putInt("count_reserved",viewModel.counter.value?:0)
        }
    }

}
```
<br>
加入observe()方法的语法扩展依赖，可以对Observer这个参数使用函数式API的写法
```xml
implementation "androidx.lifecycle:lifecycle.livedata-ktx:2.2.0"
```
```kotlin
viewModel.counter.observe(this){ count ->
    infoText.text=count.toString()
}
```


<br>
上述LiveData的基本用法可以正常工作，但是不是规范的。
<br>

**规范的LiveData用法**
将可变的LiveData隐藏在内部，只暴露不可变的LiveData。比如上述的counter在ViewModel外面也可以设置数据，是暴露给外部的，所以我们需要修改时代码，使外部只能观察它，不能修改它。

将原来的counter变量改名为_counter变量，声明为private，对外部不可见，重新定义一个counter变量，声明为不可变LiveData，外部调用get()属性返回_counter变量。
```kotlin
class MainViewModel(countReserved:Int) :ViewModel(){

    val counter:LiveData<Int>
    get()=_counter

    private val _counter= MutableLiveData<Int>()

    init{
        //设置数据
        _counter.value=countReserved
    }

    fun plusOne(){
        //获取counter中包含的数据
        val count=_counter.value?:0
        //对其数据加一,再重新设置给counter
        _counter.value=count+1
    }

    fun clear(){
        _counter.value=0
    }

}
```

### map和switchMap（LiveData的转换方法）
#### map
用于将实际包含数据的LiveData和仅用于观察数据的LiveData进行转换。

**使用**
比如我们有一个User类，里面包含用户的姓名和年龄
```kotlin
data class User(var fistName:String,var lastName:String,var age:Int)
```
然后我们在ViewModel中创建一个Live来包含User类型的数据
```kotlin
class MainViewModel(countReserved:Int) :ViewModel(){

    val userLiveData=MutableLiveData<User>()
    ...
}
```
但是我们在主界面明确只会显示用户的姓名，而完全不关心用户的年龄，所以我们只需将姓名这一部分暴露给外部，而且是只供观察。

将userLiveData设置为私有，调用Transformations的map()方法来对LiveData的数据类型进行转换。map()接收两个参数，第一个是原始的LiveData对象，第二个是一个转换函数，里面是转换的具体逻辑。
```kotlin
class MainViewModel(countReserved:Int) :ViewModel(){

    private val userLiveData=MutableLiveData<User>()
    //将User对象转换成一个只包含用户姓名的字符串，当userLiveData的数据发生变化时，
    //map()会监听到变化并执行转换函数的逻辑，然后通知给userName的观察者。
    val userName:LiveData<String> = Transformations.map(userLiveData){user -> 
    "${user.fistName} ${user.lastName}"}
    ...
}
```

#### switchMap
如果某ViewModel中的某个LiveData对象是调用另外的方法获取的，我们需要借助switchMap()将这个LiveData对象转换成另外一个可观察的LiveData对象。


**创建一个Repository单例类**
有一个getUser方法用来返回一个包含User数据的LiveData对象
```kotlin
object Repository{
    fun getUser(userId:String):LiveData<User>{
        val liveData=MutableLiveData<User>()
        liveData.value=User(userId,userId,0)
        return liveData
    }
}
```
在MainViewModel中也定义一个getUser()方法，并让它调用Repository的getUser()方法来获取LiveData对象。
```kotlin
class MainViewModel(countReserved:Int) :ViewModel(){
....
    fun getUser(userId:String):LiveData<User>{
        return Repository.getUser(userId)
    } 
}
```
直接在Activity中观察LiveData的数据变化
```kotlin
viewModel.getUser(userId).observe(this){
    user ->
}
```
这样是不可行的的，因为我们每次调用getUser()方法返回的都是一个新的LiveData实例，而上述写法一直观察的是老的LiveData实例，所以无法观察到数据的变化。
**需要使用switchMap()方法**
```kotlin
class MainViewModel(countReserved:Int) :ViewModel(){

    ....

    //用来观察userId的数据变化
    private val userIdLiveData=MutableLiveData<String>()

    //第一个参数是新增的userIdLiveData，switchMap()方法会对它进行观察。
    //第二个参数是转换函数，转换函数返回一个LiveData对象，switchMap()方法就是将这个对象转换成另一个可以观察的LiveData对象。
    val user:LiveData<User> = Transformations.switchMap(userIdLiveData){ userId ->
        Repository.getUser(userId)
    }

    fun getUser(userId : String){
        userIdLiveData.value=userId
    }
}
```
- 首先，当外部调用MainViewModel的getUser()方法来获取用户数据时，并不会发起任何请求或者函数调用，只需将传入的userId值设置到userIdLiveData中
- 一旦userIdLiveData的数据发生变化，那么观察userIdLiveData的switchMap()方法就会执行，并且调用我们编写的转换函数。
- 在转换函数中调用Repository.getUser(userId)方法获取真正的用户数据，同时switchMap()方法会将返回的LiveData对象转换成一个可观察的LiveData对象。
- 对于Activity而言，只需要观察这个LiveData对象就可以了。

```kotlin
getUserBtn.setOnClickListener { 
    val userId=(0..1000).random().toString()
    viewModel.getUser(userId)
}
viewModel.user.observe(this){ user->
    infoText.text=user.fistName
}
```

### Room（数据库设计ORM框架）
官方为Android数据库设计的ORM框架。
ORM(Object Relational Mapping)关系对象映射，将面向对象的语言和面向关系的数据库建立一种映射关系。
**整体结构**
- Entity
  用于定义封装实际数据的实体类，每个实体类都会在数据库中有一张对应的表，并且表中的列是根据实体类中的字段自动生成的。
- Dao
  Dao是数据访问对象的意思，通常会在这里对数据库的各项操作进行封装，在实际编程的时候，逻辑层就不需要和底层数据库打交道了，直接和Dao层进行交互即可。
- Database
  用于定义数据库中的关键信息，包括数据库的版本号、包含哪些实体类以及提供Dao层的访问实例。

在项目中声明的注解会动态生成代码，因此这里一定要使用kapt引入Room的编译时注解库，需要添加Kotlin-kapt插件。在java项目中，使用annotationProcessor
```xml
id 'kotlin-kapt'


implementation 'androidx.room:room-runtime:2.1.0'
kapt "androidx.room:room-compiler:2.1.0"
```


#### 具体使用
##### 实体类的声明
使用@Entity声明一个实体类
```kotlin
@Entity
data class User(var fistName:String,var lastName:String,var age:Int){
    //使用@PrimaryKey注解将id设为主键，autoGenerate设置为true表示主键的值自动生成
    @PrimaryKey(autoGenerate = true)
    var id : Long=0
}

```

##### 定义Dao
定义一个接口，使用@Dao注解让Room识别它为一个Dao，内部提供对应增删查改的4种注解，@Insert、@Delete、@Query、@Update、@Query。
从数据库中查询数据，或者使用非实体类参数来增删查改数据，就必须编写SQL语句

```kotlin
@Dao
interface UserDao {
    //插入对象，返回该对象的id
    @Insert
    fun insertUser(user:User):Long

    //将参数中的对象更新到数据库中
    @Update
    fun updateUser(newUser:User)

    //查询User表中所有数据，返回一个List对象
    @Query("SELECT * FROM User")
    fun loadAllUsers():List<User>

    //查询User表中age大于指定大小的行
    @Query("SELECT * FROM User WHERE age> :age" )
    fun loadUserOlderThan(age:Int):List<User>

    //将参数传入的对象从数据库中删除
    @Delete
    fun deleteUser(user:User)

    //删除指定lastName的对象
    @Query("DELETE FROM User WHERE lastName= :lastName")
    fun deleteUserByLastName(lastName:String):Int
}
```


##### 定义Database
分为三部分：数据库的版本号，包含哪些实体类，提供Dao层的访问实例


- 创建一个AppDatabase类继承RoomDatabase类。
- 并使用@Database进行注解，注解中声明数据库的版本号以及包含哪些实体类，多个实体类之前用逗号分开即可。
- AppDatabase类必须声明为抽象类，在其中提供相应的抽象方法声明，具体的方法由Room在底层自动完成。
- AppDatabase类在全局应该只有唯一一个，所以编写一个单例类。


```kotlin
@Database(version = 1,entities = [User::class])
abstract class AppDatabase : RoomDatabase(){
    abstract fun userDao():UserDao


    //单例类
    companion object{

        //instance缓存AppDatabase的实例
        private var instance:AppDatabase?=null

        //获取AppDatabase的实例的方法
        @Synchronized
        fun getDatabase(context: Context):AppDatabase{
            //如果instance不为空就直接放回
            instance?.let {
                return  it
            }
            //为空则调用Room.databaseBuilder来构建一个AppDatabase实例
            //参数1：applicationContext 参数2：AppDatabase的Class类型 参数3：数据库名
            //build方法完成构建，最后赋值给instance
            return Room.databaseBuilder(context.applicationContext, 
                AppDatabase::class.java,"app_data")
                .build().apply {
                    instance=this
                }
        }
    }
}
```


##### 使用它们
由于数据库操作属于耗时操作，Room默认是不允许在主线程中进行数据库操作的，所以我们需要将这些操作放入子线程中。
```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    ....

    //通过AppDatabase获取userDao实例
    val userDao=AppDatabase.getDatabase(this).userDao()
    //创建User对象
    val user1=User("Tom","Brady",40)
    val user2=User("Tom","Hanks",63)

    addDataBtn.setOnClickListener {
        thread {
            //调用userDao的insertUser方法插入User，并将返回的id赋值给user
            user1.id=userDao.insertUser(user1)
            user2.id=userDao.insertUser(user2)
        }
    }

    updateBtn.setOnClickListener {
        thread {
            //更新数据库内user1这个对象
            user1.age=42
            userDao.updateUser(user1)
        }
    }

    deleteDataBtn.setOnClickListener { 
        thread {
            //删除lastName为Hanks这个对象
            userDao.deleteUserByLastName("Hanks")
        }
    }
    
    queryDataBtn.setOnClickListener { 
        thread{
            //查询所有的User对象
            for(user in userDao.loadAllUsers()){
                Log.d("MainActivity",user.toString())
            }
        }
    }
}
```

#### Room的数据库升级
在上述已经拥有了一个User表的数据库中添加一个Book表
Book表的实体类
```kotlin
@Entity
data class Book(var name:String,var pages:Int){

    @PrimaryKey(autoGenerate = true)
    var id : Long=0

}
```
Book表的Dao层接口
```kotlin
@Dao
interface BookDao {
    @Insert
    fun insertBook(book:Book):Long

    @Query("select * from Book")
    fun loadAllBooks():List<Book>

}
```

修改AppDatabase
- @Database注解中的版本号升级为2，实体类声明加入Book::class
- 在单例类的结构体中，实现一个Migration匿名类，并传入1、2两个参数，表示从版本1升级到版本2，然后在migrate方法中编写相应的建表语句
- 最后再构建AppDatabase实例的时候，加入一个addMigrations()方法，并把MIGRATION_1_2传入即可
```kotlin
@Database(version = 2,entities = [User::class,Book::class])
abstract class AppDatabase : RoomDatabase(){
    ...
    abstract fun bookDao():BookDao

    companion object{

        private val MIGRATION_1_2=object:Migration(1,2){
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("create table Book(id integer primary key autoincrement not null" +
                        ",name text not null," +
                        "pages integer not null)")
            }
        }

        private var instance:AppDatabase?=null

        @Synchronized
        fun getDatabase(context: Context):AppDatabase{
            instance?.let {
                return  it
            }
            return Room.databaseBuilder(context.applicationContext,
                AppDatabase::class.java,"app_data")
                //构建database时，使用addMigrations方法传入，自动实现升级逻辑
                .addMigrations(MIGRATION_1_2)
                .build().apply {
                    instance=this
                }
        }
    }
}
```
后面如果还需要升级就继续更改数据库版本，比如版本为3，从2到3的升级，编写相应的SQL语句就行。

### WorkManager（定时任务处理工具）
用于处理一些要求定时执行的任务，它可以根据操作系统的版本自动选择底层是使用AlarmManager实现还是JobScheduler实现，从而降低我们的使用成本。支持周期性任务、链式任务等功能，是一个非常强大的工具。
- 一个处理定时任务的工具，可以保证即使在应用退出甚至手机重启的情况下，之前注册的任务仍会得到执行。
- 使用WorkManager注册的周期性任务不能保证一定会准时执行，因为系统为了减少电量消耗，可能会将触发时间临近的的几个任务放在一起执行。

#### 基本用法
添加依赖
```xml
implementation 'androidx.work:work-runtime:2.2.0'
```

步骤：
- 定义一个后台任务，并实现具体任务逻辑
- 配置该后台任务的运行条件和约束信息，并构建后台任务请求
- 将该后台任务请求传入WorkManager的enqueue()方法中，系统会在合适的时间运行
  

**定义后台任务**

- 继承Worker类
- 重写doWork()方法，在这个方法中编写具体的后台任务逻辑
- doWork()不会运行在主线程中，会返回一个Result对象，用于表示任务的运行结果，成功返回Result.success()，失败返回Result.failure()，Result.retry()也代表失败，可以结合WorkRequest.Builder的setBackoffCriteria()方法重新执行任务。
```kotlin   
//后台任务
class SimpleWorker(context: Context, params: WorkerParameters) :Worker(context, params){

    override fun doWork(): Result {
        Log.d("SimpleWorker","do work in SimpleWorker")
        return Result.success()
        
    }
    
}
```



**配置后台任务的运行条件和约束条件**

```kotlin
//OneTimeWorkRequest.Builder是WorkRequest.Builder的子类，用于构建单次运行的后台任务请求
//val request=OneTimeWorkRequest.Builder(SimpleWorker::class.java).build()
//PeriodicWorkRequest.Builder用于构建周期性运行的后台任务请求，为了降低设备性能消耗，运行周期间隔不能短于15分钟
val request=PeriodicWorkRequest.Builder(SimpleWorker::class.java,15,TimeUnit.MINUTES).build()

```



**将该后台任务请求传入WorkManager的enqueue()方法中，系统会在合适的时间运行**

```kotlin
//将构建的后台任务请求传入WorkManager的enqueue()方法中
WorkManager.getInstance(this).enqueue(request)
```

