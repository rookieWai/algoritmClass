# View

view是Android所有控件的基类

ViewGroup是View的组合，它可以包含很多View和ViewGroup



![未命名文件](C:\Users\weiwai\Desktop\笔记图片\未命名文件.png)

View的部分继承关系

![20190815100748544](C:\Users\weiwai\Desktop\笔记图片\20190815100748544.png)







# 坐标系

## Android坐标系

屏幕左上角的定点作为android坐标系的原点，这个原点向右是X轴正方向，向下是Y轴正方向，

![Android坐标系](C:\Users\weiwai\Desktop\笔记图片\Android坐标系.png)



## View坐标系

![View坐标系](C:\Users\weiwai\Desktop\笔记图片\View坐标系.png)

**1 获取自身的宽和高**

```java
width=getRight-getLeft();
height=getBottom()-getTop();


//系统提供的获取方法
getWidth()  getHeight()
```



**2 View自身的坐标**

获取View到其父控件的距离

- getTop()：获取View自身顶边到其父布局顶边的距离
- getLeft()：获取View自身左边到其父布局左边的距离
- getRight()：获取View自身右边到其父布局左边的距离
- getBottom()：获取View自身底部到其父布局顶边的距离



**MotionEvent提供的方法**

- getX()：获取点击事件距离控件左边的距离，即视图坐标
- getY()：获取点击事件距离控件顶边的距离，即视图坐标
- getRawX()：获取点击事件距离整个屏幕左边的距离，即绝对坐标
- getRawY()：获取点击事件距离整个屏幕顶边的距离，即绝对坐标



# View的滑动

滑动思路：点击事件传到View时，系统记下触摸点的坐标，手指移动时系统记下移动后触摸的坐标并算出偏移量，并通过偏移量来修改View的坐标。

滑动方法：

## layout方法

修改View的left、top、right、bottom来控制View的坐标

在ACTION_MOVE事件中计算偏移量，再调用layout方法重新放置这个自定义View的位置

```java
public class CustomView extends View {
    private int lastX;
    private int lastY;

    public CustomView(Context context) {
        super(context);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public boolean onTouchEvent(MotionEvent event){
         //获取手指触摸点的横坐标和纵坐标
        int x = (int)event.getX();
        int y = (int)event.getY();

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                lastX=x;
                lastY=y;
                break;
            case MotionEvent.ACTION_MOVE:
                //计算移动的距离
                int offsetX=x-lastX;
                int offsetY=y-lastY;
                //调用layout方法来重新放置它的位置
                layout(getLeft()+offsetX,getTop()+offsetY,
                        getRight()+offsetX,getBottom()+offsetY);
                break;
        }
        return true;
    }
}
```



## offsetLeftAndRight()与offsetTopAndBottom()

```JAVA
            case MotionEvent.ACTION_MOVE:
                //计算移动的距离
                int offsetX=x-lastX;
                int offsetY=y-lastY;
                //对left和right进行偏移
                offsetLeftAndRight(offsetX);
                //对top和bottom进行偏移
                offsetTopAndBottom(offsetY);
                break;
```



## LayoutParams（改变布局参数）

LayoutParams主要保存了一个View的布局参数，可通过LayoutParams来改变View的布局参数，从而改变View的位置

父控件为LinearLayout，所以用LinearLayout.LayoutParams

```java
            case MotionEvent.ACTION_MOVE:
                //计算移动的距离
                int offsetX=x-lastX;
                int offsetY=y-lastY;
				//也可以使用ViewGroup.MarginLayoutParams
                LinearLayout.LayoutParams layoutParams=(LinearLayout.LayoutParams) getLayoutParams();
                layoutParams.leftMargin=getLeft()+offsetX;
                layoutParams.topMargin=getTop()+offsetY;
                setLayoutParams(layoutParams);
  
                break;
```



## 动画

res->anim->translate.xml

view向右平移300像素，android:fillAfter="true"移动后留在当前位置

```xml
<set xmlns:android="http://schemas.android.com/apk/res/android"
    android:fillAfter="true">
    <translate
        android:duration="1000"        android:fromXDelta="0"
        android:toXDelta="300"
        />
</set>
```

```java
mCustomView.setAnimation(AnimationUtils.loadAnimation(this,R.anim.translate));
```



View动画并不能改变View的位置参数，对于系统来说并没有改变它的原有位置，点击初始位置才能触发点击事件

属性动画可解决上述问题

```java
ObjectAnimator.ofFloat(mCustomView,"translationX",0,300).setDuration(1000).start();
```



## scrollTo与scrollBy

scrollTo移动到某一个具体的坐标点

```kotlin
((View) getParent()).scrollTo(x,y);
```

scrollBy移动的增量为dx dy

```java
((View)getParent()).scrollBy(-offsetX,-offsetY);
```



## Scroller

scrollTo与scrollBy滑动时瞬间的，scoller可实现有过渡效果的滑动，scoller自身不能实现滑动，需要与View的computeScroll方法配合

初始化scroller

```
public CustomView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    mScroller=new Scroller(context);
}
```

重写computerScroll方法，系统会在绘制View的时候在draw方法中调用该方法

```kotlin
    @Override
    public void computeScroll() {
        super.computeScroll();
        if(mScroller.computeScrollOffset()){
            //通过Scroller来不断获取当前的滑动值
            ((View)getParent()).scrollTo(mScroller.getCurrX(),mScroller.getCurrY());
            //每滑动一小段距离就调用 invalidate进行重绘
            invalidate();
        }
    }

```

```kotlin
public void smoothScrollTo(int destX,int destY){
    int scrollX=getScrollX();
    int delta=destX-scrollX;
    //在2000ms内沿X轴平移delta像素
    mScroller.startScroll(scrollX,0,delta,0,2000);
    invalidate();
}
```

activity中调用

```kotlin
mCustomView.smoothScrollTo(-200,0); //沿着X轴向右平移400像素
```



# 属性动画

传统的View动画

- 相比于属性动画，View的缺陷为不具有交互能力，只能做普通的动画，需要避免交互操作。
- 但效率比较高效，使用也方便

## ObjectAnimator

 通过objectAnimator来实现动画

首先创建一个objectAnimator对象，通过其静态工厂类直接返回

ObjectAnimator.java对象中的ofFloat方法，参数依次为：操作的Object，操作的属性，可变的float类型数组

```kotlin
public static ObjectAnimator ofFloat(Object target, String propertyName, float... values) {
    ObjectAnimator anim = new ObjectAnimator(target, propertyName);
    anim.setFloatValues(values);
    return anim;
}
```



简单使用属性动画实现平移

```
ObjectAnimator mObjectAnimator=ObjectAnimator.ofFloat(mCustomView,"translationX",200);
mObjectAnimator.setDuration(600);  //持续时间
mObjectAnimator.start();
```

这里的操作的属性必须要有get和set方法

可直接使用属性值

| translationX和translationY     | 用来沿着X轴或者Y轴进行平移                                 |
| ------------------------------ | ---------------------------------------------------------- |
| rotation、rotationX、rotationY | 用来围绕View的支点进行旋转                                 |
| PrivotX和PrivotY               | 控制View对象的支点位置，围绕这个支点进行旋转和缩放变换处理 |
| alpha                          | 透明度，默认是1不透明，0代表完全透明                       |
| x、y                           | 描述View对象在其容器中的最终位置                           |



如果一个属性没有get方法和set方法，可通过自定义一个属性或包装类来间接地给这个属性增加get方法和set方法

```java
    private static class MyView{
        private View mTarget;
        private  MyView(View mTarget){
            this.mTarget=mTarget;
        }
        public int getWidth(){
            return mTarget.getLayoutParams().width;
        }
        public void setWidth(int width){
            mTarget.getLayoutParams().width=width;
            mTarget.requestLayout();
        }
    }
```



```java
        MyView myView=new MyView(mButton);
        ObjectAnimator.ofInt(myView,"width",500).setDuration(600).start();
```



## ValueAnimator

不提供动画，数值发生器，用来产生有一定规律的数字，从而让调用者控制动画的实现过程

```java
        ValueAnimator mValueAnimator=ValueAnimator.ofFloat(0,100);
        mValueAnimator.setTarget(myView);
        mValueAnimator.setDuration(100).start();
		//AnimatorUpdateListener中监听数值的变化，从而完成动画的变换
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Float mFloat = (Float) animation.getAnimatedValue();
            }
        });
```





## 动画的监听

```java
        //完整的动画流程，Start、 Repeat、End、Cancel
        mObjectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
```



```java
        mObjectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }
        });
```



## 组合动画—AnimatorSet

AnimatorSet类提供了一个play方法，向方法中传入一个Animator对象，将返回一个AnimatorSet.Builder实例

Builder类采用了建造者模式，每次调用方法时都返回Builder自身用于继续构建

| AnimatorSet.Builder中的四个方法 |                                    |
| ------------------------------- | ---------------------------------- |
| after(Animator anim)            | 将现有动画插入到传入的动画之后执行 |
| after(long delay)               | 将现有动画延迟指示的毫秒后执行     |
| before(Animator anim)           | 将现有动画插入到传入动画之前执行   |
| with(Animator anim)             | 将现有动画和传入动画同时执行       |

```java
        ObjectAnimator objectAnimator1=ObjectAnimator.ofFloat(mCustomView,"translationX",0.0f,200.0f,0f);
        ObjectAnimator objectAnimator2=ObjectAnimator.ofFloat(mCustomView,"scaleX",1.0f,2.0f);
        ObjectAnimator objectAnimator3=ObjectAnimator.ofFloat(mCustomView,"rotationX",0.0f,90.0f,0.0F);
        AnimatorSet set=new AnimatorSet();
        set.setDuration(1000);
        set.play(objectAnimator1).with(objectAnimator2).after(objectAnimator3);
        set.start();
//这里先执行objectAnimator3，然后同时执行objectAnimator1和objectAnimator2
```

```java
// 两个动画同时执行
animatorSet.playTogether(animator1, animator2); 
```

## 动画组合—PropertyValuesHolder

只能做到多个动画一起执行

```java
        //PropertyValuesHolder组合动画
        PropertyValuesHolder valuesHolder1=PropertyValuesHolder.ofFloat("scaleX",1.0f,1.5f);
        PropertyValuesHolder valuesHolder2=PropertyValuesHolder.ofFloat("rotationX",0.0f,90.0f,0.0F);
        PropertyValuesHolder valuesHolder3=PropertyValuesHolder.ofFloat("alpha",1.0f,0.3f,1.0F);
        ObjectAnimator objectAnimator=ObjectAnimator.ofPropertyValuesHolder(mCustomView,valuesHolder1,valuesHolder2,valuesHolder3);
        objectAnimator.setDuration(2000).start();
```

PropertyValuesHolder 的基础上更进一步，通过设置 Keyframe （关键帧），把同一个动画属性拆分成多个阶段。 例如，你可以让一个进度增加到 100% 后再「反弹」回来。

```java
// 在 0% 处开始
Keyframe keyframe1 = Keyframe.ofFloat(0, 0); 
// 时间经过 50% 的时候，动画完成度 100%
Keyframe keyframe2 = Keyframe.ofFloat(0.5f, 100); 
// 时间见过 100% 的时候，动画完成度倒退到 80%，即反弹 20%
Keyframe keyframe3 = Keyframe.ofFloat(1, 80); 
PropertyValuesHolder holder = PropertyValuesHolder.ofKeyframe("progr
ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(view
animator.start();
```



## 在XML中使用属性动画

在res文件中新建animator文件，然后在里面新建一个scale.xml

```xml
<objectAnimator xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:duration="3000"
    android:propertyName="scaleX"
    android:valueFrom="1.0"
    android:valueTo="2.0"
    android:valueType="floatType">

</objectAnimator>
```



在程序中引用XML

```java
        Animator animator= AnimatorInflater.loadAnimator(this,R.animator.scale);
        animator.setTarget(mButton);
        animator.start();
```



# View的事件分发机制

## Activity的构成

![Activity构成](C:\Users\weiwai\Desktop\笔记图片\Activity构成.png)

Activity包含一个Window对象，该对象是由PhoneWindow来实现的。PhoneWindow将DecorView作为整个应用窗口的根View，这个DecorView将屏幕划分成个区域：一个区域是TitleView，另一个区域是ContentView，我们平常做应用所写的布局就展示在ContentView中。





·







# 笔记

## 绘制基础

onDraw()

绘制的关键Canvas

- Canvas的绘制类方法：drawXXX() （关键参数：Paint对象）
- Canvas的辅助类方法：范围裁切和几何变换

可以使用不同的绘制方法来控制遮盖关系



Canvas类方法：

- drawColor(@ColorInt int color) 颜色填充 、整个区域；Color.parseColor("#88880000")原有效果上半透明

- drawCircle(float centerX , float centerY , float radius , Paint paint) 画圆； 

  ```
      paint.color = Color.RED  //设置为红色
      
      //FILL , STROKE 和 FILL_AND_STROKE 。FILL 是填充模式，STROKE 是画线模 式（即勾边模式），FILL_AND_STROKE 是两种模式一并使用：既画线又填充。它的 默认值是 FILL ，填充模式。
      paint.style = Paint.Style.STROKE  //修改为划线模式
      
      //在 STROKE 和 FILL_AND_STROKE 下
      paint.strokeWidth= 20F  //线条宽度为20像素
  
      //（1）new Paint()时开启抗锯齿
      Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
      paint.isAntiAlias=true   //（2）动态开关抗锯齿
   
  ```

  

- drawRect(float left , float top , float right , float bottom , Paint paint) 画矩形 四条边的坐标

  drawRect(RectF rect, Paint paint)    drawRect(Rect rect, Paint paint)

- drawPoint(float x , float y , Paint paint)画点

  ```kotlin
  //设置点的大小
  paint.strokeWidth = 20F
  //设置点的形状，ROUND圆形、SQUARE BUTT方形
  paint.strokeCap=Paint.Cap.SQUARE;
  ```

- drawPoints(float[ ] pts , int offset , int count , Paint paint) / drawPoints(float[ ] pts , Paint paint)批量画点  pts数组点的坐标，每两个成一对，offset表示跳过数组的前几个数再开始记坐标，count表示一共要绘制几个点。

  ```
  // 绘制四个点：(50, 50) (50, 100) (100, 50) (100, 100)
  val points = floatArrayOf(0f, 0f, 50f, 50f, 50f, 100f, 100f, 50f, 100f,
      100f, 150f, 50f
  )
  //跳过前两个数，绘制8个数（4个点）
  canvas?.drawPoints(points,2,8,paint)
  ```

- drawOval(float left , float top , float right , float bottom , Paint paint) 画椭圆

  drawOval(RectF rect, Paint paint)

- drawLine(float startX , float startY , float stopX , float stopY , Paint paint) 画线

-  drawLines(float[ ] pts , int offset , int count , Paint paint) / drawLines(float[ ] pats , Paint paint)批量画线

- drawRoundRect(float left , float top , float right , float bottom , float rx , float ry , Paint paint)画圆角矩形  rx、ry为圆角的横向半径和纵向半径

- drawArc(float left , float top , float right , float bottom , float startAngle , float sweepAngle , boolean useCenter , Paint paint )绘制弧形或扇形

- drawPath(Path path, Paint paint) 画自定义图形 

  参数path是用来描述图形路径的对象

  1、Path第一类方法：直接描述路径

  （1）添加子图形

  addCircle(float x , float y , float radius , Direction dir)添加圆

  参数dir是画圆的路径方向，顺时针和逆时针，cw和ccw，在需要填充图形时图形相交用于判断填充范围。

  添加椭圆、矩形、圆角矩形类似......

  addPath(Path path)添加另一个Path

  （2）xxxTo — 画线

  lineTo(float x , float y)/rLineTo(float x , float )画直线

  从当前位置向目标位置画一条直线，lineTo的参数是目标位置坐标即绝对坐标，rLine的参数是相对于当前位置的坐标。
  
  quadTo(float x1, float y1, float x2, float y2)/rQuadTo(float dx1, float dy1,float dx2,float dy2) 画二次贝塞尔曲线
  
  cubicTo(float x1, float y1,float x2,float y2,float x3,float y3)/rCubicTo(float x1,float y1,float x2,float y2,float x3,float y3) 画三次贝塞尔曲
  
  moveTo(float x, float y)/rMoveTo(float x, float y)移动到目标位置，改变当前位置
  
  arcTo(RectF oval, float startAngle, float sweepAngle, boolean forceMoveTo) / arcTo(float left, float top, float right, float bottom, float startAngle, float sweepAngle, boolean forceMoveTo) / arcTo(RectF oval, float startAngle, float sweepAngle)画弧形 forceMoveTo参数的意思是绘制时是否抬笔后移动到弧形起点，是否留下移动的痕迹
  
  addArc(float left, float top, float right, float bottom, float startAngle, float sweepAngle) / addArc(RectF oval , float startAngle, float sweepAngle) 画弧形，直接使用forceMoveTo=true的简化版
  
  close()封闭当前子图形   等价于lineTo(起点坐标)
  
  2、Path方法第二类：辅助的设置或计算
  
  Path.setFillType(Path.FillType ft) 设置填充方式，用来设置图形自相交时的填充算法
  
  填入不同的FillType值，会有不同的填效果
  
  FilType的四个取值：EVEN_ODD     WINDING(默认值)    INVERSE_EVEN_ODD INVERSE_WINDING
  
  后面两个是前两个的反色版本
  
  EVEN_ODD（交叉填充）原理
  
  即 even-odd rule （奇偶原则）：对于平面中的任意一点，向任意方向射出一条射 线，这条射线和图形相交的次数（相交才算，相切不算哦）如果是奇数，则这个点 被认为在图形内部，是要被涂色的区域；如果是偶数，则这个点被认为在图形外 部，是不被涂色的区域。
  
  ![image-20220221133618623](C:\Users\weiwai\Desktop\笔记图片\image-20220221133618623.png)
  
  
  
  WINFING（全填充）
  
  即 non-zero winding rule （非零环绕数原则）：首先，它需要你图形中的所有线条 都是有绘制方向的。
  
  然后，同样是从平面中的点向任意方向射出一条射线，但计算规则不一样：以 0 为 初始值，对于射线和图形的所有交点，遇到每个顺时针的交点（图形从射线的左边 向右穿过）把结果加 1，遇到每个逆时针的交点（图形从射线的右边向左穿过）把 结果减 1，最终把所有的交点都算上，得到的结果如果不是 0，则认为这个点在图形 内部，是要被涂色的区域；如果是 0，则认为这个点在图形外部，是不被涂色的区 域

![image-20220221133721405](C:\Users\weiwai\Desktop\笔记图片\image-20220221133721405.png)

![image-20220221133750533](C:\Users\weiwai\Desktop\笔记图片\image-20220221133750533.png)

Canvas绘制Bitmap

drawBitmap(Bitmap bitmap, float left, float top ,Paint paint)画Bitmap

绘制 Bitmap 对象，也就是把这个 Bitmap 中的像素内容贴过来，其中 left 和 top 是要把 bitmap 绘制到的位置坐标。

drawText(String text, float x, float y, Paint paint)绘制文字

Paint.setTextSize(float textSize)设置文字大小

## Paint

Api大致分为四类：

- 颜色
- 效果
- drawText相关
- 初始化

### 1 颜色

Canvas 绘制的内容，有三层对颜色的处理：

![image-20220221165044946](C:\Users\weiwai\Desktop\笔记图片\image-20220221165044946.png)



**1.1基本颜色**



|                      Canvas的方法                      |     像素颜色的设置方式     |
| :----------------------------------------------------: | :------------------------: |
|                  drawColor/RGB/ARGB()                  |      直接作为参数传入      |
|                      drawBitmap()                      | 与bitmap参数的像素颜色相同 |
| 图形和文字(drawCircle() / drawPath() / drawText()....) |     在paint参数中设置      |

##### 

**1.1.1直接设置颜色**

（1）setColor(int color)

（2）setARGB(int a, int r, int g, int b)

**1.1.2 setShader(Shader shader)设置Shader**

Paint也可使用Shader设置颜色，并不直接用 Shader 这个类，而是用它的几个 子类。有 LinearGradient RadialGradient SweepGradient BitmapShader ComposeShader等

(1) LinearGradient线性渐变

```kotlin
        val shader =LinearGradient(50f,50f,250f,250f,Color.parseColor("#0080FF"),Color.parseColor("#FE2E64"),Shader.TileMode.CLAMP)
        paint2.shader = shader
        canvas?.drawCircle(200f,200f,100f,paint2)
```

```
LinearGradient(float x0, float y0, float x1, float y1, int color0, int color1,Tile tile)
参数：
x0 y0 x1 y1 ：渐变的两个端点的位置
color0 color1 是端点的颜色
tile ：端点范围之外的着色规则，类型是 TileMode 。TileMode 一共有 3 个值可
选： CLAMP(会在端点之外延续端点处的颜色), MIRROR(镜像模式)和REPEAT(重复模式) 。
```

(2) RadialGradient 辐射渐变

从中心向周围辐射状的渐变

```
构造方法：
RadialGradient(float centerX, float centerY, float radius, int centerColor, int edgeColor, Tile tileMode)
参数：
centerX centerY ：辐射中心的坐标
radius ：辐射半径
centerColor ：辐射中心的颜色
edgeColor ：辐射边缘的颜色
tileMode ：辐射范围之外的着色模式。
```

(3)SweepGradient 扫描渐变

```
构造方法：
SweepGradient(float cx, float cy, int color0, int color1)
参数：
cx cy ：扫描的中心
color0 ：扫描的起始颜色
color1 ：扫描的终止颜色
```

(4)BitmapShader

使用Bitmap来着色，用Bitmap像素来作为图形或文字的填充

```
val bitmap=BitmapFactory.decodeResource(resources,R.drawable.aa);
val shader=BitmapShader(bitmap,Shader.TileMode.CLAMP,Shader.TileMode.CLAMP)
paint.shader = shader

canvas?.drawCircle(200f,200f,200f,paint)
```

```
构造方法：
BitmapShader(Bitmap bitmap, Shader.TileMode tileX, Shader.TileMode tiley)
参数：
bitmap ：用来做模板的 Bitmap 对象
tileX：横向的 TileMode
tileY：纵向的 TileMode 
```



(5)ComposeShader混合着色器

....



#### 



## Canvas对绘制的辅助



#### 1.范围裁切

**（1）clipRect()**

```java
canvas.save();
canvas.clipRect(left, top, right, bottom); 
canvas.drawBitmap(bitmap, x, y, paint); 
canvas.restore();
```

Canvas.save() 和 Canvas.restore() 来及时恢复绘制范围

**（2）clipPath()**

和 clipRect() 用法一样，将参数换成了 Path

```java
canvas.save(); 
canvas.clipPath(path1); 
canvas.drawBitmap(bitmap, point1.x, point1.y, paint); 
canvas.restore();
canvas.save(); 
canvas.clipPath(path2); 
canvas.drawBitmap(bitmap, point2.x, point2.y, paint); 
canvas.restore(); 
```

#### 2.几何变换

- 使用Canvas来做常见的二维变换
- 使用Matrix来做常见和不常见的二维变换
- 使用Camera来做三维变换



##### 使用Canvas来做常见的二维变换

**（1）Canvas.translate(float dx, float dy)平移**

dx和dy表示横向和纵向的位移

```java
canvas.save(); 
canvas.translate(200, 0); 
canvas.drawBitmap(bitmap, x, y, paint); 
canvas.restore();
```

**（2）Canvas.rotate(float degress, float px, float py)旋转**

degress是旋转角度，单位为度，方向是顺时针为正向，px和py是轴心的位置。

```java
canvas.save(); 
canvas.rotate(45, centerX, centerY); 
canvas.drawBitmap(bitmap, x, y, paint); 
canvas.restore(); 
```

**（3）Canvas.scale(float sx, float sy, float px, float py)放缩**

sx、sy是横向和纵向的放缩倍数；px、py是放缩的轴心。

```java
canvas.save(); 
canvas.scale(1.3f, 1.3f, x + bitmapWidth / 2, y + bitmapHeight / 2)
canvas.drawBitmap(bitmap, x, y, paint); 
canvas.restore();
```

**（4）skew(float sx, float sy)错切**

```java
canvas.save(); 
canvas.skew(0, 0.5f); 
canvas.drawBitmap(bitmap, x, y, paint); 
canvas.restore();
```



##### 使用Matrix来做变换

**（1）常见变换**

常见变换的方式流程：

- 常见Matrix对象；
- 调用Matrix的pre/postTranslate/Rotate/Scale/Skew()方法来设置几何变换；
- 使用Canvas.setMatrix(matrix) 或 Canvas.concat(matrix)来把几何变换应用到Canvas。

1. Canvas.setMatrix(matrix) ：用 Matrix 直接替换 Canvas 当前的变换矩 阵，即抛弃 Canvas 当前的变换，改用 Matrix 的变换（不同的系统中 setMatrix(matrix) 的行为可能不一致，尽量用concat(matrix) ）； 
2. Canvas.concat(matrix) ：用 Canvas 当前的变换矩阵和 Matrix 相乘，即基 于 Canvas 当前的变换，叠加上 Matrix 中的变换。

**（2）自定义变换**

Matrix.setPolyToPoly(float[] src, int srcIndex, float[] dst, int dstIndex, int pointCount)用点对点映射的方式设置变换

setPolyToPoly() 的作用是通过多点的映射的方式来直接设置变换。「多点映射」的意思就是把指定的点移动到给出的位置，从而发生形 变。例如：(0, 0) -> (100, 100) 表示把 (0, 0) 位置的像素移动到 (100, 100) 的位置， 这个是单点的映射，单点映射可以实现平移。而多点的映射，就可以让绘制内容任意地扭曲。 

参数：src和dst是源点集合目标点集；srcIndex和dstIndex是第一个点的偏移；pointCount是采集的点的个数（个数不能大于4，因为大于4个点就无法计算变换了）



##### 使用Camera来做三维变换

三类：旋转、平移、移动相机

**（1）Camera.rotate*() 三维旋转**

- rotateX(deg)
- rotateY(deg)
- rotateZ(deg)
- rotate(x,y,z)

Camera 和 Canvas 一样也需要保存和恢复状态才能正常绘制

```java
canvas.save();
camera.save(); // 保存 Camera 的状态 
camera.rotateX(30); // 旋转 Camera 的三维空间 
camera.applyToCanvas(canvas); // 把旋转投影到 Canvas 
camera.restore(); // 恢复 Camera 的状态
canvas.drawBitmap(bitmap, point1.x, point1.y, paint); 
canvas.restore(); 
```

如果你需要图形左右对称，需要配合上 Canvas.translate() ，在三维旋转之前把 绘制内容的中心点移动到原点，即旋转的轴心，然后在三维旋转后再把投影移动回来：

```java
canvas.translate(-centerX, -centerY); // 旋转之前把绘制内容移动到轴心
```

Canvas 的几何变换顺序是反的，所以要把移动到中心的代码写在移动后，把从中心移动回来的代码写在上面。



**（2）Camera.translate(float x,float y,float z)移动**

**（3）Camera.setLocation(x,y,z)设置虚拟相机的位置**

参数单位不是像素，是inch英寸



### 绘制顺序

![image-20220303161217190](C:\Users\weiwai\Desktop\笔记图片\image-20220303161217190.png)

#### 1.super.onDraw前或者后

绘制代码在其前或者后会有不同的效果，view类里onDraw为空实现。

**（1）写在super.onDraw()下面**

由于绘制代码会在原有内容绘制结束之后才执行，所以绘制内容就会盖住控件原来的内容。

（ Debug 模式下绘制出 ImageView 的图像尺寸信息）

**（2）写在super.onDraw()上面**

由于绘制代码会执行在原有内容之前，所以绘制的内容会被控件的原内容盖住。



#### 2.dispatchDraw()：绘制子View的方法

在绘制过程中，每一个ViewGroup会先调用自己的onDraw()来绘制自己的主体之后再调用dispatchDraw()方法绘制值View，所以子View会覆盖主View。

- onDraw()绘制主体 
- dispatchDraw()绘制子View

**（1）写在super.dispatchDraw()下面**

将onDraw换成dispatchDraw()

重写dispatchDraw()，在super.dispatchDraw()下面写上绘制代码，这段绘制代码就会发生在子View的绘制之后，从而让绘制内容盖住子View。

**（2）写在super.dispatchDraw()上面**

将绘制代码写在super.dispatchDraw()上面，这段绘制就会在onDraw()之后、super.dispatchDraw()之前发生，绘制内容就会出现在主体内容和子View之间。



#### 3.绘制过程简述

背景 、主体、子 View、滑动边缘渐变和滑动条 、前景

一般来说，一个 View（或 ViewGroup）的绘制不会这几项全都包含，但必然逃不出 这几项，并且一定会严格遵守这个顺序

- 背景                                    drawBackground()   不能重写
- 主题                                    onDraw()
- 子View                                dispatchDraw()
- 滑动边缘渐变和互动条      onDrawForeground()
- 前景                                     onDrawForeground()

drawBackground() 的方法里，但这个方法是 private 的，不能重写，设置背景只能用自带的 API （xml 布局文件的 android:background 属性以及 Java 代码的 View.setBackgroundXxx() 方法）

滑动边缘渐变和滑动条可以通过 xml 的 android:scrollbarXXX 系列属性或 Java 代码的 View.setXXXScrollbarXXX() 系列方法来设置；前景可以通过 xml 的 android:foreground 属性或 Java 代码的 View.setForeground() 方法来设置。 而重写 onDrawForeground() 方法，并在它的 super.onDrawForeground() 方法的上面或下面插入绘制代码，则可以控制绘制内容和滑动边缘渐变、滑动条以及前景的遮盖关系。





#### 4.onDrawForeground()

API23引入，在 onDrawForeground() 中，会依次绘制滑动边缘渐变、滑动条和前景。

**（1）写在onDrawForeground下面**

将绘制代码写在super.onDrawForeground下面，绘制代码会在滑动边缘渐变、滑动条和前景之后被执行，那么绘制内容将会盖住滑动边缘渐变、滑动条和前景。

**（2）写在supoer.onDrawForeground上面**

将绘制代码写在super.onDrawForeground()上面，绘制内容就会在dispatchDraw()和super.onDrawForeground之间执行，那么绘制内容就就会盖住子View，但被滑动边缘渐变、滑动条以及前景盖住。



#### 5.draw()总调度方法

View的整个绘制过程都发生在draw()方法里，背景、主体、子View、滑动相关以及前景的绘制都在draw()方法里。

```java
// View.java 的 draw() 方法的简化版大致结构（是大致结构，不是源码哦）：

public void draw(Canvas canvas) { 
 ...
 drawBackground(Canvas); // 绘制背景（不能重写）
 onDraw(Canvas); // 绘制主体
 dispatchDraw(Canvas); // 绘制子 View
 onDrawForeground(Canvas); // 绘制滑动相关和前景
 ...
}
```

可重写draw()方法来做自定义的绘制。

**（1）写在super.draw()下面**

将绘制代码写在super.draw()下面，那么这段代码会在其他所有绘制完成之后再执行，它的绘制内容会盖住其他的所有绘制内容。、

**（2）写在super.draw()上面**

将绘制代码写在super.draw()上面，那么这段代码会在其他所有绘制完成之前执行，会被其他所有内容盖住。

**注意**

1. 出于效率的考虑，ViewGroup 默认会绕过 draw() 方法，换而直接执行 dispatchDraw() ，以此来简化绘制流程。所以如果你自定义了某个 ViewGroup 的子类（比如 LinearLayout ）并且需要在它的除 dispatchDraw() 以外的任何一个绘制方法内绘制内容，你可能会需要调用 View.setWillNotDraw(false) 这行代码来切换到完整的绘制流程（是「可 能」而不是「必须」的原因是，有些 ViewGroup 是已经调用过 setWillNotDraw(false) 了的，例如 ScrollView ）。 

   **在 ViewGroup 的子类中重写除 dispatchDraw() 以外的绘制方法时，可能需 要调用 setWillNotDraw(false) 。**

2.  有的时候，一段绘制代码写在不同的绘制方法中效果是一样的，这时你可以选一 个自己喜欢或者习惯的绘制方法来重写。但有一个例外：如果绘制代码既可以写 在 onDraw() 里，也可以写在其他绘制方法里，那么优先写在 onDraw() ，因 为 Android 有相关的优化，可以在不需要重绘的时候自动跳过 onDraw() 的重 复执行，以提升开发效率。享受这种优化的只有 onDraw() 一个方法。

   **在重写的方法有多个选择时，优先选择 onDraw() 。**



![image-20220303161645243](C:\Users\weiwai\Desktop\笔记图片\image-20220303161645243.png)



### 属性动画

#### ViewPropertyAnimator

使用方法：View.animate()后面跟translationX等方法

```java
view.animate().translationX(500)
```



|   View中的方法    |      功能       | 对应ViewPropertyAnimator中的方法 |
| :---------------: | :-------------: | :------------------------------: |
| setTranslationX() |   设置x轴偏移   | translationX()  translationXBy() |
| setTranslationY() |   设置y轴偏移   | translationY()  translationYBy() |
| setTranslationZ() |   设置z轴偏移   | translationZ()  translationYBy() |
|      setX()       | 设置x轴绝对位置 |     x()               xBy()      |
|      setY()       | 设置y轴绝对位置 |     y()               yBy()      |
|      setZ()       | 设置z轴绝对位置 |     z()               zBy()      |
|   setRotaion()    |  设置平面旋转   |   rotation()      rotationBy()   |
|   setRotionX()    |  设置沿x轴旋转  |  rotationX()      rotationXBy()  |
|   setRotionY()    |  设置沿y轴旋转  |  rotationX()      rotationXBy()  |
|    setScaleX()    |  设置横向缩放   |      scaleX()    scaleXBy()      |
|    setScaleY()    |  设置纵向缩放   |     scaleY()     scaleYBy()      |
|    setAlpha()     |   设置透明度    |      alpha()     alphaBy()       |

- 带有-By后缀为增量模式，增加多少，translationXBy(100) 则表示用动画把 View 的 translationX值渐变地增加100。



#### ObjectAnimator

使用方法：

- （1）如果是自定义控件，需要添加setter/getter方法;
- （2）用ObjectAnimator.ofXXX()创建objectAnimator对象；
- （3）用start()方法执行动画

```java
public class SportsView extends View { 
  float progress = 0;
 ......
 // 创建 getter 方法
 public float getProgress() {
 return progress;
 }
 // 创建 setter 方法
 public void setProgress(float progress) {
 this.progress = progress;
 invalidate(); //通知view重绘
 }
 @Override
通用功能
 public void onDraw(Canvas canvas) {
 super.onDraw(canvas);
 ......
 canvas.drawArc(arcRectF, 135, progress * 2.7f, false, paint
 ......
 }
}
......
// 创建 ObjectAnimator 对象
ObjectAnimator animator = ObjectAnimator.ofFloat(view, "progress", 0
// 执行动画
animator.start(); 


```



#### ViewPropertyAnimator和ObjectAnimator通用功能



**1.setDuratio (int duration) 设置动画时长**

单位为毫秒。



**2.setInterpolator（Interpolator interpolator）设置Interpolator**

速度设置器，在参数中填入不同的interpolator，动画就会以不同的速度模型来执行。

- **(1) AccelerateDecelerateInterpolator**

  默认Interpolator，先加速后减速，符合物理世界的模型。

- **(2) LinearInterpolator**

  匀速。

- **(3) AccelerateInterpolator**

  持续加速。

- **(4) DecelerateInterpolator**

  持续减速到0。

- **(5) AnticipateInterpolator**

  先回拉一下再进行正常动画轨迹。效果看起来像投掷物体或跳跃动作前的蓄力。

- **(6) OvershootInterpolator**

  动画会超过目标值一些，然后再会弹回来。

- **(7) AnticipateOvershootInterpolator**

  两个版本的结合，开始前会拉，最后超过一些然后回弹。

- **(8) BounceInterpolator**

  在目标值处弹跳。

- **(9) CycleInterpolator**

  正玄/余玄曲线，可自定义曲线的周期，曲线的周期由CycleInterpolator()构造方法的参数决定。

- **(10) PathInterpolator**

  自定义动画完成度/时间完成度曲线。定制的方式是使用一个Path对象来绘制出你要的完成动画度/时间完成曲线。

  ```java
  Path interpolatorPath = new Path();
  ...
  // 匀速
  interpolatorPath.lineTo(1, 1); 
  ```

  ![image-20220309133714953](C:\Users\weiwai\Desktop\笔记图片\image-20220309133714953.png)

  ```java
  
  // 先以「动画完成度 : 时间完成度 = 1 : 1」的速度匀速运行 25%
  interpolatorPath.lineTo(0.25f, 0.25f); 
  // 然后瞬间跳跃到 150% 的动画完成度
  interpolatorPath.moveTo(0.25f, 1.5f); 
  // 再匀速倒车，返回到目标点
  interpolatorPath.lineTo(1, 1); 
  ```

   Path描述的是一个 y = f(x) (0 ≤ x ≤ 1) （y 为动画完 成度，x 为时间完成度）的曲线。

- **(11) FastOutLinearInInterpolator**

  加速运动。

  FastOutLinearInInterpolator 的曲线公式是用的贝塞尔曲线，而 AccelerateInterpolator 用的是指数曲线。具体来说，它俩最主要的区别是 FastOutLinearInInterpolator 的初始阶段加速度比 AccelerateInterpolator 要快一些。

- **(12)FastOutSlowInInterpolator**

  先加速再减速。

  FastOutSlowInInterpolator 用的是贝塞尔曲 线，AccelerateDecelerateInterpolator 用的是正弦 / 余弦曲线。具体来讲， FastOutSlowInInterpolator 的前期加速度要快得多。

- **(11)LinearOutSlowInInterpolator**

  持续减速。

  FastOutSlowInInterpolator 用的是贝塞尔曲 线，AccelerateDecelerateInterpolator 用的是正弦 / 余弦曲线。具体来讲， FastOutSlowInInterpolator 的前期加速度要快得多。

#### 设置监听器

给动画设置监听器，可以在关键时刻得到反馈，从而及时做出合适的操作，例如在动画的属性更新时同步更新其他数据，或者在动画结束后回收资源等。

ViewPropertyAnimator 用的是 setListener() 和 setUpdateListener() 方 法，可以设置一个监听器，要移除监听器时通过 set[Update]Listener(null) 填 null 值来移除。

ObjectAnimator 则是用 addListener() 和 addUpdateListener() 来添加一个或多个监听器，移除监听器则是通过 remove[Update]Listener() 来指定移除对象。

ObjectAnimator 支持使用 pause() 方法暂停，所以它还多了一个 addPauseListener() / removePauseListener() 的支持。

ViewPropertyAnimator 则独有 withStartAction() 和 withEndAction() 方 法，可以设置一次性的动画开始或结束的监听。

**(1)ViewPropertyAnimator.setListener() / ObjectAnimator.addListener()**

设置的监听器数量不一样，但它们的参数类型都 是 AnimatorListener ，所以本质上其实都是一样的。

AnimatorListener共有4个回调方法：

- onAnimationStart(Animator animation) 

  当动画开始执行时，这个方法被调用。 

- onAnimationEnd(Animator animation) 

  当动画结束时，这个方法被调用。

- onAnimationCancel(Animator animation) 

  当动画被通过 cancel() 方法取消时，这个方法被调用。

  需要说明一下的是，就算动画被取消，onAnimationEnd() 也会被调用。所以当动画被取消时，如果设置了 AnimatorListener ，那么 onAnimationCancel() 和 onAnimationEnd() 都会被调用。onAnimationCancel() 会先于 onAnimationEnd() 被调用。

-  onAnimationRepeat(Animator animation) 

  当动画通过 setRepeatMode() / setRepeatCount() 或 repeat() 方法重复执行 时，这个方法被调用。

  由于 ViewPropertyAnimator 不支持重复，所以这个方法对 ViewPropertyAnimator 相当于无效。

**(2)ViewPropertyAnimator.setUpdateListener() / ObjectAnimator.addUpdateListener()**

参数都是 AnimatorUpdateListener，只有一个回调方法。

- onAnimationUpdate(ValueAnimator animation)

  当动画的属性更新时（不严谨的说，即每过10毫秒，动画的完成度更新时），这个方法被调用。

**(3)ObjectAnimator.addPauseListener()**



**(4)ViewPropertyAnimator.withStartAction/EndAction()**

它们和 set/addListener() 中回调的 onAnimationStart() / onAnimationEnd() 相比起来的不同主要有两 点： 

- withStartAction() / withEndAction() 是一次性的，在动画执行结束后就自动弃掉了，就算之后再重用 ViewPropertyAnimator来做别的动画，用它们设置的回调也不会再被调用。而 set/addListener() 所设置的 AnimatorListener 是持续有效的，当动画重复执行时，回调总会被调用。 
- withEndAction() 设置的回调只有在动画正常结束时才会被调用，而在动画被取消时不会被执行。这点和 AnimatorListener.onAnimationEnd() 的行为是不一致的。



#### TypeEvaluator

ObjectAnimator，可以用 ofInt() 来做整数的属性动画和用 ofFloat() 来做小数的属性动画，当需要对其他类型做属性动画的时候，就需要用到TypeEvaluator。

**(1) ArgbEvaluator**

TypeEvaluator最经典的用法是使用ArgbEvaluator来做颜色渐变的动画。

```java
ObjectAnimator animator = ObjectAnimator.ofInt(view, "color", 0xffff0000,0x00ff00ff)
animator.setEvaluator(new ArgbEvaluator()); 
animator.start(); 

在Android5.0（API 21）加入了新的方法 ofArgb()
ObjectAnimator animator = ObjectAnimator.ofArgb(view, "color", 0xffff0000,0x00ff00ff)

```



**(2)自定义Evaluator**

 针对颜色的渐变，可自定义TypeEvaluator

```java
// 自定义 HslEvaluator
private class HsvEvaluator implements TypeEvaluator<Integer> { 
 float[] startHsv = new float[3];
 float[] endHsv = new float[3];
 float[] outHsv = new float[3];
 @Override
 public Integer evaluate(float fraction, Integer startValue, Integer endValue)
 // 把 ARGB 转换成 HSV
 Color.colorToHSV(startValue, startHsv);
 Color.colorToHSV(endValue, endHsv);
 // 计算当前动画完成度（fraction）所对应的颜色值
 if (endHsv[0] - startHsv[0] > 180) {
 endHsv[0] -= 360;
 } else if (endHsv[0] - startHsv[0] < -180) {
 endHsv[0] += 360;
 }
 outHsv[0] = startHsv[0] + (endHsv[0] - startHsv[0]) * fraction;
 if (outHsv[0] > 360) {
 outHsv[0] -= 360;
 } else if (outHsv[0] < 0) {
 outHsv[0] += 360;
 }
 outHsv[1] = startHsv[1] + (endHsv[1] - startHsv[1]) * fraction;
 outHsv[2] = startHsv[2] + (endHsv[2] - startHsv[2]) * fraction;
ofObject()
     
 // 计算当前动画完成度（fraction）所对应的透明度
 int alpha = startValue >> 24 + (int) ((endValue >> 24 - start)
 // 把 HSV 转换回 ARGB 返回
 return Color.HSVToColor(alpha, outHsv);
 }
}
                                       
                                       
ObjectAnimator animator = ObjectAnimator.ofInt(view, "color", 0xffff0000,0x00ff00ff)
// 使用自定义的 HslEvaluator
animator.setEvaluator(new HsvEvaluator()); 
animator.start()
```



**(3)ofObject()**

借助于 TypeEvaluator ，属性动画就可以通过 ofObject()来对不限定类型的属性做动画。

- 1.为目标属性写一个自定义的 TypeEvaluator。
- 2.使用 ofObject() 来创建 Animator ，并把自定义的 TypeEvaluator 作为参数填入。

```java
private class PointFEvaluator implements TypeEvaluator<PointF> { 
 PointF newPoint = new PointF();
    
 @Override
 public PointF evaluate(float fraction, PointF startValue, PointF endValue){
 float x = startValue.x + (fraction * (endValue.x - startValue.x));
 float y = startValue.y + (fraction * (endValue.y - startValue.y));
 
 newPoint.set(x, y);
     
 return newPoint;
 }
}
ObjectAnimator animator = ObjectAnimator.ofObject(view, "position", 
 new PointFEvaluator(), new PointF(0, 0), new PointF(1, 1));
animator.start(); 

```

API 21 中，已经自带了 PointFEvaluator 这个类。

**(4)ofMultiInt() ofMultiFloat()**



#### 其他

**(1)PropertyValuesHolder 同一个动画中改 变多个属性**

如果使用 ViewPropertyAnimator ，你可以直接用连写的方式来在一个动画中同时改变多个属性，而对于 ObjectAnimator ，是不能这么用的。可 使用PropertyValuesHolder 来同时在一个动画中改变多个属性。

PropertyValuesHolder它是一个属性值的批量存放地。所以你如果有多个属性需要修改，可以把它们放在不同的 PropertyValuesHolder 中，然后使用 ofPropertyValuesHolder() 统一放进 Animator 。

```java
PropertyValuesHolder holder1 = PropertyValuesHolder.ofFloat("scaleX",1)
PropertyValuesHolder holder2 = PropertyValuesHolder.ofFloat("scaleY",1)
PropertyValuesHolder holder3 = PropertyValuesHolder.ofFloat("alpha",1)
ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(view,holder1,holder2,holder3);
animator.start(); 

```



**(2)AnimatorSet多个动画配合执行**





## 布局基础

**(1)布局过程的含义**

布局过程，就是程序在运行时利用布局文件的代码来计算出实际尺寸的过程。

(2)布局过程的工作内容

测量阶段和布局阶段

测量阶段：从上到下递归地调用每个View或者ViewGroup的measure()方法，测量他们的尺寸并计算它们的位置。

布局阶段：从上到下递归地调用每个View或者ViewGroup的layout()方法，把测得的尺寸和位置赋值给它们。

**(3)View和ViewGroup布局过程**

![image-20220309162907926](H:\knowledge\笔记图片\image-20220309162907926.png)

![image-20220309162922513](H:\knowledge\笔记图片\image-20220309162922513.png)



**(4)布局过程自定义的方式**

三种：

- 重写 onMeasure() 来修改已有的 View 的尺寸。
- 重写 onMeasure() 来全新定制自定义 View 的尺寸。
- 重写 onMeasure() 和 onLayout() 来全新定制自定义 ViewGroup 的内部布局。



**1）重写 onMeasure() 来修改已有的 View 的尺寸**

- 重写 onMeasure() 方法，并在里面调用 super.onMeasure() ，触发原有的自我测量。
- 在 super.onMeasure() 的下面用 getMeasuredWidth() 和 getMeasuredHeight() 来获取到之前的测量结果，并使用自己的算法，根据测量结果计算出新的结果。
- 调用 setMeasuredDimension() 来保存新的结果。



**2）重写onMeasure()来全新定制自定义View的尺寸**

**全新定制尺寸和修改尺寸的最重要区别：**

需要在计算的同时，保证计算结果满足父View给出的尺寸限制。

**父View的尺寸限制**

由来：开发者的要求（布局文件中layout_打头的属性）经过父View处理计算后的更精确的要求。

限制的分类：

- UNSPECIFIED：不限制
- AT_MOST：限制上限
- EXACTLY：限制固定值

全新定义自定义View尺寸的方式

- 重写onMeasure()，并计算出View的尺寸。
- 使用resolveSize()来让子View的计算结果符合父View的限制。（也可使用自己的方式）



**3）重写 onMeasure() 和 onLayout() 来全新定制自定义 ViewGroup 的内部布局**

**定制Layout内部布局的方式**

- 重写onMeasure()来计算内部布局。
- 重写onLayout()来摆放子View。

**重写onMeasure()的三个步骤**

- 调用每个子View的measure()来计算子View 的尺寸。#1
- 计算子View的位置并保存子View的位置和尺寸。
- 计算自己的尺寸并用setMeasuredDimension()保存。

**计算子View尺寸的关键**

计算子View的尺寸，关键在于measure()方法的两个参数——也就是子View 的两个MeasureSpec的计算。

**子View 的MeasureSpec的计算方式**

- 结合开发者的要求（xml中layout_打头的属性）和自己的可用空间（自己的尺寸上限 - 已用尺寸）。
- 尺寸上根据自己的MeasureSpec中的mode而定
  - EXACTLY / AT_MOST：尺寸上限为 MeasureSpec 中的 size
  - UNSPECIFIED：尺寸无上限

**重写onLayout()的方式**

在onLayout()里调用每个子View的layout()，让它们保存自己的位置和尺寸。

![image-20220312105430488](H:\knowledge\笔记图片\image-20220312105430488.png)



![image-20220312103802816](H:\knowledge\笔记图片\image-20220312103802816.png)



![image-20220312104954742](H:\knowledge\笔记图片\image-20220312104954742.png)





**自定义触摸反馈的关键： **

1. 重写 onTouchEvent() ，在里面写上你的触摸反馈算法，并返回 true （关键是 ACTION_DOWN 事件时返回 true ）。 
1. 如果是会发生触摸冲突的 ViewGroup ，还需要重写 onInterceptTouchEvent() ，在事件流开始时（Down事件）返回 false ，并在确认接管事件流时返回一次 true ，以实现对事件的拦截。 
1. 当子 View 临时需要组织父 View 拦截事件流时，可以调用父 View 的 requestDisallowInterceptTouchEvent() ，通知父 View 在当前事件流中不再尝试通过 onInterceptTouchEvent() 来拦截。



事件分发：从上到下，一个view执行后不再传递

事件拦截：会从底部view向上递归调用每一级子view的onInterceptTouchEvent()方法来询问它是否需要拦截次事件

![image-20220312110554846](H:\knowledge\笔记图片\image-20220312110554846.png)



