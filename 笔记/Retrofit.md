# Retrofit
- 配置好一个根路径，就可以在指定服务器接口的时候只需要使用相对路径
- 对服务器接口进行分类，将功能同属于一类的服务器接口定义到同一个接口文件当中，从而让代码结构变得更加合理
- 不需关心网络通信的细节，只需要在接口文件中声明一系列方法和返回值，然后通过注解的方式指定该方法对应哪个服务器的接口，以及提供哪些参数。当我们在程序中调用该方法时，Retrofit就会自动向对应的服务器接口发起请求，并将响应的数据解析成返回值声明的类型。

**添加依赖**
```kotlin
implementation 'com.squareup.retrofit2:retrofit:2.6.1'
implementation 'com.squareup.retrofit2:converter-gson:2.6.1'
```
第一条依赖会将Retrofit、OkHttp和Okio这几个库一起下载
第二条依赖是Retrofit的转换库，它借助GSON来解析JSON

**（1）新建接口文件**
根据服务器接口的功能进行归类，创建不同种类的接口文件，其中定义对应的服务器的接口方法
本例子是只有一个获取JSON数据的接口，所以只需定义一个接口文件，并包含一个方法

接口文件以具体功能种类名开头，Service结尾
```kotlin
internal interface AppService {
    //@GET注释表示调用getAppData时Retrofit会发起一条GET请求
    //请求地址是注释中传入的参数，这里只需要根地址
    @GET("get_data.json")
    //返回值为Call，通过泛型指定服务器响应的数据应该转换成什么对象
    fun getAppData():Call<List<App>>
}
```
//JSON数据对应的类
```kotlin
class App(val id:String,val name:String,val version:String)
```

**（2）处理具体的网络请求逻辑**
```kotlin
getAppDataBtn.setOnClickListener {

    val retrofit=Retrofit.Builder()
        .baseUrl("")  //指定所有Retrofit请求的根路径
        .addConverterFactory(GsonConverterFactory.create())  //指定解析数据时所用的转换库
        .build()
    val appService=retrofit.create(AppService::class.java) //创建该接口的动态代理对象
    //getAppData会返回一个Call<List<App>>对象，再调用enqueue，Retrofit会根据注解中配置的
    //服务器接口地址去进行网络请求 数据回调到callback
    appService.getAppData().enqueue(object:Callback<List<App>>{
        override fun onResponse(call: Call<List<App>>, response: Response<List<App>>) {
            //调用response.body()获取解析后的对象
            val list=response.body()
            if(list!=null){
                for(app in list){
                    Log.d("MainActivity","id is ${app.id}")
                    Log.d("MainActivity","name is ${app.name}")
                    Log.d("MainActivity","version is ${app.version}")
                }
            }
        }

        override fun onFailure(call: Call<List<App>>, t: Throwable) {
            TODO("Not yet implemented")
            t.printStackTrace()
        }

    })

}

```


**处理复杂的接口地址**
```kotlin
interface ExampleService{
    //@GET @POST @PUT @PATCH @DELETE

    //普通的静态
    @GET("get_data.json")
    fun getData(): Call<Data>   //通过泛型指通过服务器响应返回的数据应该转换成什么对象

    //动态情况

    //（1）GET http://example.com/<page>/get_data.json 在这个接口中<page>代表不同的页数
    //这里在相对路径使用了一个{page}占位符，然后在getData()方法中传入一个page参数，并且使用@Path("page")来声明这个参数
    //在调用getData()方法时，会自动将page参数的值替换到占位符的位置上
    @GET("{page}/get_data.json")
    fun getData(@Path("page") page:Int):Call<Data>

    //（2）GET http://example.com/get_data.json？u=<user>&t=<token>  借口后面？键值对 传入一系列参数的情况
    //使用@Query("键") 值:数据类型
    @GET("get_data.json")
    fun getData(@Query("u") user:String,@Query("t") token:String):Call<Data>


    //删除  DELETE http://example.com/data/<id>
    @DELETE("data/{id}")
    fun deleteData(@Path("id") id:String ):Call<ResponseBody>  //不需要解析，直接返回响应体

    //提交数据 POST http://example.com/data/create  {"id":1,"content":"The description for this data"}
    @POST("data/create")
    fun createData(@Body data:Data):Call<ResponseBody>
    //createData方法中声明了一个Data类型的参数，并且加上了@Body注解，在发出POST请求时，
    //会自动将Data对象中的数据转换成JSON格式的文本，并放到HTTP请求的body部分
    //服务器在收到请求之后只需要从body中将这部分数据解析出来

    /**
     * 请求header中指定的参数
     *User-Agent:okhttp
     * Cache-Control:max-age=0
     * header参数就是一个个键值对，使用@Headers注解
     */
    //静态
    @Headers("User-Agent:okhttp","Cache-Control:max-age=0")
    @GET("get_data.json")
    fun getData2(): Call<Data>

    //动态
    @GET("get_data.json")
    fun getData2(@Header("User-Agent") userAgent:String,
                 @Header("Cache-Control") cacheControl:String): Call<Data>

}
```

**Retrofit构建器的最佳写法**

```kotlin
//封装构建动态代理对象的过程
object ServiceCreator {
    private  const val BASE_URL="http:10.0.2.2/"

    private val retrofit=Retrofit.Builder()
        .baseUrl(BASE_URL)
        .build()

    fun <T> create(serviceClass: Class<T>):T= retrofit.create(serviceClass)

    inline  fun <reified T>create():T= create(T::class.java)

}
```

然后就可以使用
val appService=ServiceCreator.create<AppService>()获取动态代理对象



**使用协程简化回调的写法**

 suspendCoroutine，当前协程会被立即挂起，Lambda表达式中的代码则会在普通线程中执行，然后我们在表达式中调用 enqueue()进行网络请求，成功调用continuationd的resume恢复被挂起的协程，并传入服务器响应的数据，会成为 suspendCoroutine的返回值，请求失败调用continuation.resumeWithException(t)恢复协程，并传入具体的异常



```kotlin
    private suspend fun <T> Call<T>.await():T{
        return suspendCoroutine { continuation ->
            enqueue(object : Callback<T>{
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body=response.body()
                    if(body!=null) continuation.resume(body)
                    //resumeWithException和resume 让协程恢复执行
                    else continuation.resumeWithException(
                        RuntimeException("response body is null")
                    )
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }

            })
        }
    }
```

调用就可写成

```kotlin
suspend fun getAppData(){
try{
    val appList=ServiceCreator.create<AppService>.getAppData.await()
}catch(e:Exception){
    //
}
    
```

