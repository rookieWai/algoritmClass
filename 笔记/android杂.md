Fragment隐藏键盘

```koltin
val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
imm.hideSoftInputFromWindow((context as Activity).currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
```





ImageView图片自适应

```xml
android:scaleType="fitXY"
android:adjustViewBounds="true"
```





- ObservableField只有在数据发生改变时UI才会收到通知，而LiveData不同，只要你postValue或者setValue，UI都会收到通知，不管数据有无变化





Recyclerview缓存问题

不知道大家有没有遇到这种情况，当你通过点击事件改变第一个item的状态之后，向下滑动到第10个item也会同样触发，然后第19、28、37、46等等，每间隔9个item就会重复之前的操作后的显示状态。
如何解决呢？其实就是一个缓存的问题，只要在recyclerview相应的activity或者fragment中设置
recyclerview.setItemViewCacheSize(int)
int是缓存数，默认值为5，每间隔（4+5）个item就会进行复用，如果你把它设为10，那么每间隔（4+10）个item就会复用，你只要把int的值设为：【item数 - 4】即可！





顶部标题栏设置颜色值达到view风格一致



```xml
 <item name="android:windowTranslucentStatus">true</item>

```

设置view属性，其外层的所有控件都需设置

```gr
 android:fitsSystemWindows="true"
```

设置系统栏字体颜色

```kotlin
getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR) 
```

