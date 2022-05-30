# Material Design

界面设计语言，包括视觉、运功、互动效果等特性

## Toolbar

继承了ActionBar的所有功能，而且灵活性更高，可以配合其他控件完成一些Material Design的效果。

**1.取消ActionBar**

通常我们的ActionBar是在res/values/themes文件下

![](https://img-blog.csdnimg.cn/20210419190102530.png)

![](https://img-blog.csdnimg.cn/20210419190113790.png)

通过AppTheme来指定它的parent主题是什么，这里我们准备使用Toolbar来代替ActionBar，因此需要指定一个不带ActionBar的主题

Light.NoActionBar表示不带ActionBar的浅色主题

```xml
<style name="Theme.MaterialTest" parent="Theme.MaterialComponents.Light.NoActionBar">
```

**2使用Toolbar**

（1）修改布局文件

```xml
<androidx.appcompat.widget.Toolbar
    android:id="@+id/toolbar"
    android:layout_width="match_parent"
    android:layout_height="?attr/actionBarSize"
    android:background="@color/design_default_color_primary"
    android:theme="@style/ThemeOverlay.AppCompat.Dark.ActionBar"
    app:popupTheme="@style/ThemeOverlay.AppCompat.Light"
    />
        <!-- 高度为 actionBar的高度-->
        <!-- theme指定Toolbar为深色主题-->
        <!--popupTheme弹出菜单项指定成浅色-->

```

添加了一个Toolbar的控件

（2）修改Acitivity.kt文件

```kotlin
setSupportActionBar(toolbar) //将会toolbar实例传入，让它的外观和功能和ActionBar一致
```

（3）添加菜单

在res文件下新建menu文件夹，然后在菜单文件夹下创建一个ToolBar的菜单布局

```
<menu xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto">
    <item
        android:id="@+id/backup"
        android:icon="@mipmap/ic_backup"
        android:title="Backup"
        app:showAsAction="always"
        />
    <item
        android:id="@+id/delete"
        android:icon="@mipmap/ic_delete"
        android:title="Delete"
        app:showAsAction="ifRoom"
        />
    <item
        android:id="@+id/settings"
        android:icon="@mipmap/ic_settings"
        android:title="Settings"
        app:showAsAction="never"
        />

    <!-- app:showAsAction指定显示位置，always表示永远在Toolbar中；ifRoom表示如果屏幕
     空间足够显示在Toolbar中，否则显示在菜单栏中，never表示永远显示在菜单栏中-->


</menu>
```

修改Activity.kt中的代码

```kotlin
//初始化菜单，加载菜单文件
override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.toolbar,menu)
    return true
}

//给菜单设置点击事件
override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when(item.itemId){
        R.id.backup->Toast.makeText(this,"you click backup",Toast.LENGTH_SHORT).show()
        R.id.delete->Toast.makeText(this,"you click delete",Toast.LENGTH_SHORT).show()
        R.id.settings->Toast.makeText(this,"you click settings",Toast.LENGTH_SHORT).show()

    }
    return true
}
```

## DrawerLayout(滑动菜单)

DrawerLayout是一个布局，在布局中允许放入两个直接子控件，第一个子控件是在主屏幕中显示的内容，第二个子控件是滑动菜单中显示的内容。

**修改布局文件**

第一个子控件为FrameLayout，用于中间屏幕的显示内容，第二个TextView作为滑动菜单中显示的内容

```kotlin
<androidx.drawerlayout.widget.DrawerLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/drawerLayout"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    >

    <FrameLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent">

    </FrameLayout>


    <TextView
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_gravity="start"
        android:background="#FFF"
        android:text="This is menu"
        android:textSize="30sp"
        />
    <!--layout_gravity指定滑动菜单是向左滑还是向右划，start会根据系统语言进行判断，英语汉语就在左边，阿拉伯语就在右边-->
</androidx.drawerla
yout.widget.DrawerLayout
```

这样就可以达到滑动的效果了

**添加导航按钮**

点击导航按钮也可以打开滑动菜单

修改Activity.kt

```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    setSupportActionBar(toolbar) 
    
    supportActionBar?.let {
        it.setDisplayHomeAsUpEnabled(true) //让导航按钮显示出来
        it.setHomeAsUpIndicator(R.mipmap.ic_menu) //设置导航图标
    }
}
....
//给菜单设置点击事件
override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when(item.itemId){
  
        android.R.id.home->drawerLayout.openDrawer(GravityCompat.START)
        //对home的点击事件进行处理，将滑动菜单显示出来，GravityCompat用来指定这里的行为和我们xml中指定的一致
        .....

    }
    return true
}
```

## NavigationView(实现滑动菜单的内容优化)

一个控件，实现滑动菜单内容展示的优化

由Material Desig提供，所以我们需要引入这个库

```
implementation 'com.google.android.material:material:1.1.0'
implementation 'de.hdodenhof:circleimageview:3.0.1'
```

第一个是Material库，第二个是Circleimageview可以实现图片的圆形化功能

这个控件需要两个部分，一个是头部分headerLayout和菜单部分menu

**nav_menu.xml**

```xml
<menu xmlns:android="http://schemas.android.com/apk/res/android">
    <group android:checkableBehavior="single">
        <item
            android:id="@+id/navCall"
            android:icon="@mipmap/nav_call"
            android:title="Call"
            />
     ....
    </group>
</menu>
```

所有的子菜单都放在一个组里面，指定checkableBehavior="single"，表示这个组里面所有的菜单只能单选

**nav_header.xml**

最外层是一个RelativeLayout，设置一个合适的高度，然后里面放了三个控件，一个头像图片采用de.hdodenhof.circleimageview.CircleImageView使它圆形化，两个TextView分别显示邮箱和用户名。

```
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="180dp"
    android:padding="10dp"
    android:background="@color/design_default_color_primary"
    >

    <de.hdodenhof.circleimageview.CircleImageView
        android:id="@+id/iconImage"
        android:layout_width="70dp"
        android:layout_height="70dp"
        android:src="@mipmap/nav_icon"
        android:layout_centerInParent="true"
        />

    <TextView
        android:id="@+id/mailText"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_alignParentBottom="true"
        android:text="rookieWai@gmail.com"
        android:textSize="14sp"/>

    <TextView
        android:id="@+id/userText"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_above="@id/mailText"
        android:text="rookieWai"
        android:textSize="14sp"/>


</RelativeLayout>
```

**使用NavigationView**

修改之前的代码，将layout中的TextView滑动菜单内容修改为NavigationView

```
<com.google.android.material.navigation.NavigationView
    android:id="@+id/navView"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:layout_gravity="start"
    app:menu="@menu/nav_menu"
    app:headerLayout="@layout/nav_header"
    />
```



**修改activity**

```kotlin
navView.setCheckedItem(R.id.navCall)  //设置为默认选择
navView.setNavigationItemSelectedListener {
    drawerLayout.closeDrawers()
    true   //返回true表示事件已被处理
}
```

## FloatingActionButton(悬浮按钮)

Material库中提供的一个控件，可以轻松实现悬浮按钮的效果

```xml
<com.google.android.material.floatingactionbutton.FloatingActionButton
    android:id="@+id/fab"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_gravity="bottom|end"
    android:layout_margin="16dp"
    android:src="@mipmap/ic_done"
    app:elevation="8dp"
    />
```

end表示如果系统语言是从左往右，那么end就在右，从右往左就在左

app:elevation="8dp" 设置悬浮高度，高度越大投影范围也就越大，投影效果越淡



```kotlin
fab.setOnClickListener{
    Toast.makeText(this,"Fab clicked",Toast.LENGTH_SHORT).show()
}
```

设置点击事件，实际用法和Button一样



## Snackbar(更先进的提示工具)

Material库提供的更加先进的提示工具

Snackbar在Tost的提示基础上，允许在提示中加入一个可交互按钮，点击按钮可以执行额外的逻辑操作。

```kotlin
//view，当前页面的任意View,会使用它自动查找最外层的布局
Snackbar.make(view,"Data deleted",Snackbar.LENGTH_SHORT)
//设置一个动作，添加点击后的逻辑
    .setAction("Undo"){
        Toast.makeText(this,"Data restored",Toast.LENGTH_SHORT).show()

    }
    .show()
```



## CoordinatorLayout(加强版的FramLayout)

由Android库提供

可以监听所有子控件的所有事件，并自动帮助我们做出最合理的响应。

使用方式就是将FramLayout标签换成CoordinatorLayout

上述Snackbar弹出的提示会将悬浮按钮遮挡住，而如果我们使用CoordinatorLayout就能监听到Snackbar的弹出时间，那么它就会自动将内部的FloatingActionButton向上偏移，从而保证不会被Snackbar遮挡。





## MaterialCardView(卡片布局)

MaterialCardView本质上是一个FrameLayout，只是额外提供了圆角和阴影等效果。



添加依赖，第一个是recyclerview循环控件，用来放卡片内容；第二个是Glide库，它是一个开源的图片加载库，可以用于加载本地图片，网络图片、Gif。

```
implementation 'androidx.recyclerview:recyclerview:1.0.0'
implementation 'com.github.bumptech.glide:glide:4.9.0'
```



**首先在主界面添加一个RecyclerView**

```xml
<androidx.recyclerview.widget.RecyclerView
    android:id="@+id/recyclerView"
    android:layout_width="match_parent"
    android:layout_height="match_parent"/>
```



**然后为RecyclerView的子项创建子布局**

这个子布局的最外层布局就是MaterialCardView

```xml
<com.google.android.material.card.MaterialCardView
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_margin="5dp"
    app:cardCornerRadius="4dp"
    >
    <LinearLayout
        android:orientation="vertical"
        android:layout_width="match_parent"
        android:layout_height="wrap_content">

        <ImageView
            android:id="@+id/fruitImage"
            android:layout_width="match_parent"
            android:layout_height="100dp"
            android:scaleType="centerCrop"
            />
        <TextView
            android:id="@+id/fruitName"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_gravity="center_horizontal"
            android:layout_margin="5dp"
            android:text="16sp"/>
    </LinearLayout>

  <!--app:cardCornerRadius="4dp"指定卡片圆角的弧度  app:elevation可指定高度-->
    <!--android:scaleType="centerCrop"指定图片的缩放模式，这里是让图片保存原有比例
    填充满整个ImageView并将超出屏幕的部分裁剪掉-->
  

</com.google.android.material.card.MaterialCardView>
```

这里的子布局有ImageView和TextView分别用来存放水果图片和名字，所以我们需要一个水果类

```kotlin
class Fruit (val name:String,val imageId:Int)
```



**为RecyclerView创建适配器**

```
class FruitAdapter(val context: Context,val fruitList: List<Fruit>): RecyclerView.Adapter<FruitAdapter.ViewHolder>() {
	
	//ViewHolder类
    inner class ViewHolder(view : View):RecyclerView.ViewHolder(view){
        val fruitImage:ImageView=view.findViewById(R.id.fruitImage)
        val fruitName: TextView =view.findViewById(R.id.fruitName)
    }
	
	
	//创建viewHolder实例
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view=LayoutInflater.from(context).inflate(R.layout.fruit_item,parent,false)
        return ViewHolder(view)
    }

	//数据绑定
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val fruit=fruitList[position]
        holder.fruitName.text=fruit.name
        Glide.with(context).load(fruit.imageId).into(holder.fruitImage)
        //load方法记载图片，传入的可以是URL地址，也可以是一个本地路径或者是一个资源id，最后调用into()方法将图片设置到某一个ImageView中
    }

    override fun getItemCount(): Int {
        return fruitList.size
    }
}
```

**修改activit.kt文件展示RecyclerView**

```kotlin
val fruits= mutableListOf(Fruit("Apple",R.mipmap.apple),Fruit("Banana",R.mipmap.banana),
    Fruit("Orange",R.mipmap.orange),Fruit("Watermelon",R.mipmap.watermelon),Fruit("Pear",R.mipmap.pear),
    Fruit("Grape",R.mipmap.grape),Fruit("Pineapple",R.mipmap.pineapple),Fruit("Strawberry",R.mipmap.strawberry),
    Fruit("Cherry",R.mipmap.cherry),Fruit("Mango",R.mipmap.mango))

val fruitList=ArrayList<Fruit>()


override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        .......
        initFruits()
        //GridLayoutManager布局方式，参数：context、列数
        val layoutManager=GridLayoutManager(this,2)
        //将recyclerView布局方式制定为GridLayoutManager
        recyclerView.layoutManager=layoutManager
        val adapter=FruitAdapter(this,fruitList)
        recyclerView.adapter=adapter
    }

    private fun initFruits(){
        fruitList.clear()
        repeat(50){
            val index=(0 until fruits.size).random()
            fruitList.add(fruits[index])
        }
    }

```





## AppBarLayout(解决控件遮挡)

AppBarLayout是一个垂直方向的LinearLayout，它内部做了很多滚动事件封装

下面使用AppBarLayout解决上面RecyclerView遮挡Toolbar的问题

**将Toolbar嵌套到AppBarLayout里**

```
<com.google.android.material.appbar.AppBarLayout
    android:layout_width="match_parent"
    android:layout_height="wrap_content">
    <androidx.appcompat.widget.Toolbar
		...
		app:layout_scrollFlags="scroll|enterAlways|snap"
        />

</com.google.android.material.appbar.AppBarLayout>
```

app:layout_scrollFlags="scroll|enterAlways|snap"，其中scroll表示当RecyclerView向上滚动的时候，Toolbar会跟着一起向上滚动并实现隐藏；enterAlways表示当Recycler向下滚动的时候，Toolbar会跟着一起向下滚动并重新显示；snap表示当Toolbar还没有完全隐藏或显示的时候，会根据滚动的距离，自动选择是隐藏还是显示。

**给RecyclerView制定一个布局行为**

```xml
app:layout_behavior="@string/appbar_scrolling_view_behavior"
```



## SwipeRefreshLayout(实现下拉刷新)

添加依赖

```xml
implementation 'androidx.swiperefreshlayout:swiperefreshlayout:1.0.0'
```

**在RecyclerView外面嵌套一层SwipeRefreshLayout**

```xml
<androidx.swiperefreshlayout.widget.SwipeRefreshLayout
    android:id="@+id/swipeRefresh"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:layout_behavior="@string/appbar_scrolling_view_behavior">
    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/recyclerView"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        />
</androidx.swiperefreshlayout.widget.SwipeRefreshLayout>
```

**处理刷新逻辑**

```koltin
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    .....
    //设置刷新进度条的颜色
    swipeRefresh.setColorSchemeResources(R.color.design_default_color_primary)
    //设置下拉刷新的监听器
    swipeRefresh.setOnRefreshListener {
        refreshFruit(adapter)
    }
}

//通过本地刷新模拟
private fun refreshFruit(adapter: FruitAdapter){
    thread {
        Thread.sleep(2000)
        runOnUiThread{
            initFruits()
            adapter.notifyDataSetChanged() //通知数据发送改变
            swipeRefresh.isRefreshing=false //表示刷新时间结束，并隐藏进度条
        }

    }
}
```



## 可折叠式标题栏

### CollapsingToolbarLayout

作用于Toolbar基础上的控件，只能作为AppBarLayout的直接子布局来使用，AppBarLayout必须作为CoordinatorLayout的子布局



**新建一个布局**

包括标题栏和内容详情

标题拉满使用CoordinatorLayout作为最外层，里面嵌套一个AppBarLayout，然后在AppBarLayout里面再嵌套一个CollapsingToolbarLayout，CollapsingToolbarLayout中放入标题栏中的内容，这里我们放入了一个ImageView展示水果图片和一个Toolbar，表示这是高级版的标题栏是由普通的标题加上图片组合而成。



内容详情由一个NestedScrollView嵌套一个LinearLayout，LinearLayout里面放一个卡片布局，布局中有一个TextView展示内容。



```xml
<androidx.coordinatorlayout.widget.CoordinatorLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent">


    <com.google.android.material.appbar.AppBarLayout
        android:id="@+id/appBar"
        android:layout_width="match_parent"
        android:layout_height="250dp"
        >

        <com.google.android.material.appbar.CollapsingToolbarLayout
            android:id="@+id/collapsingToolbar"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:theme="@style/ThemeOverlay.AppCompat.Dark.ActionBar"
            app:contentScrim="@color/design_default_color_primary"
            app:layout_scrollFlags="scroll|exitUntilCollapsed">`
            <!--contentScrim趋于折叠状态以及折叠之后的背景颜色-->
            <!--scroll表示会随着内容详情的滚动一起滚动-->
            <!--exitUntilCollapsed表示随着滚动完成折叠之后就保留在界面上，不再移出屏幕-->

            <ImageView
                android:id="@+id/fruitImageView"
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:scaleType="centerCrop"
                app:layout_collapseMode="parallax"/>

            <androidx.appcompat.widget.Toolbar
                android:id="@+id/toolbar"
                android:layout_width="match_parent"
                android:layout_height="?attr/actionBarSize"
                app:layout_collapseMode="pin"/>
            <!--layout_collapseMode指定折叠模式，parallax折叠过程中产生一定的偏移，pin位置不变-->

        </com.google.android.material.appbar.CollapsingToolbarLayout>


    </com.google.android.material.appbar.AppBarLayout>


    <!--NestedScrollView在ScrollView的基础上增加了嵌套响应滚动时间，内部只允许存在一个直接子布局-->
    <androidx.core.widget.NestedScrollView
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:layout_behavior="@string/appbar_scrolling_view_behavior">
        <LinearLayout
            android:orientation="vertical"
            android:layout_width="match_parent"
            android:layout_height="wrap_content">

            <com.google.android.material.card.MaterialCardView
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_marginBottom="15dp"
                android:layout_marginLeft="15dp"
                android:layout_marginRight="15dp"
                android:layout_marginTop="35dp"
                app:cardCornerRadius="4dp">

                <TextView
                    android:id="@+id/fruitContentText"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:layout_margin="10dp"
                    />

            </com.google.android.material.card.MaterialCardView>
        </LinearLayout>
    </androidx.core.widget.NestedScrollView>

    <com.google.android.material.floatingactionbutton.FloatingActionButton
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_margin="16dp"
        android:src="@mipmap/ic_comment"
        app:layout_anchor="@id/appBar"
        app:layout_anchorGravity="bottom|end"/>
    <!--layout_anchor指定一个锚点-->


</androidx.coordinatorlayout.widget.CoordinatorLayout>
```



**创建fruit的Activity**

```kotlin
class FruitActivity :AppCompatActivity() {
    companion object{
        const val FRUIT_NAME="fruit_name"
        const val FRUIT_IMAGE_ID="fruit_image_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fruit)
        val fruitName=intent.getStringExtra(FRUIT_NAME)?:""
        val fruitImageId=intent.getIntExtra(FRUIT_IMAGE_ID,0)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        collapsingToolbar.title=fruitName
        Glide.with(this).load(fruitImageId).into(fruitImageView)
        fruitContentText.text=fruitName.repeat(500)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home->{
                finish()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }
}
```



**在适配器的onCreateViewHolder方法中实现界面跳转的逻辑，并传递数据**

```kotlin
override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view=LayoutInflater.from(context).inflate(R.layout.fruit_item,parent,false)
    val holder=ViewHolder(view)
    holder.itemView.setOnClickListener {
        val position=holder.adapterPosition
        val fruit=fruitList[position]
        val intent=Intent(context,FruitActivity::class.java).apply {
            putExtra(FruitActivity.FRUIT_NAME,fruit.name)
            putExtra(FruitActivity.FRUIT_IMAGE_ID,fruit.imageId)
        }
        context.startActivity(intent)
    }
    return holder
}
```



## fitsSystemWindows(让标题栏背景与系统状态栏融合)

1.CollapsingToolbarLayout标题栏中的ImageView设置属性android:fitsSystemWindows="true"，其外层的所有控件都需设置



2.在程序的主题中将状态栏颜色设置为透明

添加主题

```xml
<style name="FruitActivityTheme" parent="Theme.MaterialTest">
    <item name="android:statusBarColor">@android:color/transparent</item>
</style>
```



应用主题

```xml
<activity 
android:name=".FruitActivity"
android:theme="@style/FruitActivityTheme">
</activity>
```

