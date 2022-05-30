前言
EventBus是一款针对Android优化的发布/订阅事件总线。简化了应用程序内各组件间、组件与后台线程间的通信。优点是开销小，代码更优雅，以及将发送者和接收者解耦。如果Activity和Activity进行交互还好说，如果Fragment和Fragment进行交互着实令人头疼，我们会使用广播来处理，但是使用广播稍显麻烦并且效率也不高，如果传递的数据是实体类需要序列化，那么很显然成本会有点高。今天我们就来学习下EventBus3.0的使用方法。

## EventBus概述
**EventBus的三要素**

EventBus有三个主要的元素需要我们先了解一下：

- Event：事件，可以是任意类型的对象。
- Subscriber：事件订阅者，在EventBus3.0之前消息处理的方法只能限定于onEvent、onEventMainThread、onEventBackgroundThread和onEventAsync，他们分别代表四种线程模型。而在EventBus3.0之后，事件处理的方法可以随便取名，但是需要添加一个注解@Subscribe，并且要指定线程模型（默认为POSTING），四种线程模型下面会讲到。
- Publisher：事件发布者，可以在任意线程任意位置发送事件，直接调用EventBus的post(Object)方法。可以自己实例化EventBus对象，但一般使用EventBus.getDefault()就好了，根据post函数参数的类型，会自动调用订阅相应类型事件的函数。

**EventBus的四种ThreadMode（线程模型）**
EventBus3.0有以下四种ThreadMode：

- POSTING(默认)：如果使用事件处理函数指定了线程模型为POSTING，那么该事件在哪个线程发布出来的，事件处理函数就会在这个线程中运行，也就是说发布事件和接收事件在同一个线程。在线程模型为POSTING的事件处理函数中尽量避免执行耗时操作，因为它会阻塞事件的传递，甚至有可能会引起ANR。
- MAIN: 事件的处理会在UI线程中执行。事件处理时间不能太长，长了会ANR的。
- BACKGROUND：如果事件是在UI线程中发布出来的，那么该事件处理函数就会在新的线程中运行，如果事件本来就是子线程中发布出来的，那么该事件处理函数直接在发布事件的线程中执行。在此事件处理函数中禁止进行UI更新操作。
- ASYNC：无论事件在哪个线程发布，该事件处理函数都会在新建的子线程中执行，同样，此事件处理函数中禁止进行UI更新操作。

## 2.EventBus基本用法
EventBus使用起来很简单，分为五个步骤：

**1.自定义一个事件类**
```java
 public class MessageEvent {
     ...
 }
```

2.在需要订阅事件的地方注册事件
```java
EventBus.getDefault().register(this);
```
3.发送事件
```java
EventBus.getDefault().post(messageEvent);
```
4.处理事件
```java
@Subscribe(threadMode = ThreadMode.MAIN)
public void XXX(MessageEvent messageEvent) {
    ...
}
```
前面我们说过，消息处理的方法可以随便取名，但是需要添加一个注解@Subscribe，并且要指定线程模型（默认为POSTING）。

5.取消事件订阅
```java
EventBus.getDefault().unregister(this);
```

## 3.EventBus3.0应用
上面讲到了基本用法，有的同学可能还是有点晕，这里举一个例子来应用EventBus3.0。

**添加依赖库**

Android Studio 配置gradle：
```xml
compile 'org.greenrobot:eventbus:3.0.0'
```

**定义消息事件类**
```java
public class MessageEvent {
    private String message;
    public MessageEvent(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}
```

**注册和取消订阅事件**

MainActivity中注册和取消订阅事件（MainActivity.java）：
```java
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
public class MainActivity extends AppCompatActivity {
    private TextView tv_message;
    private Button bt_message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_message=(TextView)this.findViewById(R.id.tv_message);
        tv_message.setText("MainActivity");
        bt_message=(Button)this.findViewById(R.id.bt_message);
        bt_message.setText("跳转到SecondActivity");
        bt_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,SecondActivity.class));
            }
        });
        //注册事件
        EventBus.getDefault().register(this);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消注册事件
        EventBus.getDefault().unregister(this);
    }
}

```

**事件订阅者处理事件**

这里我们的ThreadMode设置为MAIN，事件的处理会在UI线程中执行，用TextView来展示收到的事件消息：
```java
@Subscribe(threadMode = ThreadMode.MAIN)
public void onMoonEvent(MessageEvent messageEvent){
    tv_message.setText(messageEvent.getMessage());
}
```

**事件发布者发布事件**

这里创建了SecondActivity来发布消息：
```java
public class SecondActivity extends AppCompatActivity {
    private Button bt_message;
    private TextView tv_message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_message=(TextView)this.findViewById(R.id.tv_message);
        tv_message.setText("SecondActivity");
        bt_message=(Button)this.findViewById(R.id.bt_message);
        bt_message.setText("发送事件");
        bt_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new MessageEvent("欢迎关注刘望舒的博客"));
                finish();
            }
        });

    }
}
```

好了运行程序，我们看到MainActivity的TextView显示MainActivity字样：
![avatar](https://img-blog.csdnimg.cn/20200103181232232.png)
接下来我们点击按钮进入SecondActivity并点击该界面中的发送事件按钮，这时SecondActivity被finish掉，MainActivity的TextView显示”欢迎关注刘望舒的博客”：
![avatar](https://img-blog.csdnimg.cn/20200103181216109.png)
![](https://img-blog.csdnimg.cn/20200103181222182.png)

## 4.EventBus3.0粘性事件
除了上面讲的普通事件外，EventBus还支持发送黏性事件，就是在发送事件之后再订阅该事件也能收到该事件，跟黏性广播类似。为了验证粘性事件我们修改以前的代码：

**订阅粘性事件**

在MainActivity中我们将注册事件添加到button的点击事件中：
```java
bt_subscription.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
          //注册事件
          EventBus.getDefault().register(MainActivity.this);
      }
  });
```

**订阅者处理粘性事件**

在MainActivity中新写一个方法用来处理粘性事件：
```java
@Subscribe(threadMode = ThreadMode.POSTING，sticky = true)
public void ononMoonStickyEvent(MessageEvent messageEvent){
    tv_message.setText(messageEvent.getMessage());
}
```

**发送黏性事件**

在SecondActivity中我们定义一个Button来发送粘性事件：
```java
bt_subscription.setOnClickListener(new View.OnClickListener() {
     @Override
     public void onClick(View v) {
         EventBus.getDefault().postSticky(new MessageEvent("粘性事件"));
         finish();
     }
 });
```

好了运行代码再来看看效果，首先我们在MainActivity中并没有订阅事件，而是直接跳到SecondActivity中点击发送粘性事件按钮，这时界面回到MainActivity，我们看到TextView仍旧显示着MainActivity的字段，这是因为我们现在还没有订阅事件。
![](https://img-blog.csdnimg.cn/20200103181153929.png)
![](https://img-blog.csdnimg.cn/20200103181153373.png)
接下来我们点击订阅事件，TextView发生改变显示“粘性事件”，大功告成。
![](https://img-blog.csdnimg.cn/20200103181137659.png)
EventBus3.0的使用就暂时分析到这，不明白的同学可以下载源码来研究下，下一讲将讲解EventBus3.0的源码。

## 5.ProGuard 混淆规则
```XML
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}
```