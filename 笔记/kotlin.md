# 高阶函数

## 函数类型：

在我们熟知的编程语言中没有函数类型这个概论，Kotlin中提出了这个概念

函数类型的基本语法规则：

(String,Int)->Unit    

->的左边部分是函数类型的参数，用一个括号放入参数，多个参数用逗号隔开

->的右边部分是函数类型的返回值类型

## 高阶函数

### 高阶函数的定义

如果一个函数接收另一个函数作为参数，或者返回值的类型是另一个函数，那么该函数就称为高阶函数

我们函数接收的这个函数参数就是上述的函数类型

### 举例说明

下面我们看一个高阶函数的例子：

```kotlin
fun highOrderFun(func:(String,Int)->Unit){
    func("abc","123");   //函数参数的调用和普通参数一样，传入括号中对应的参数就行
}
```

使用高阶函数，我们就可以让传入的参数类型来决定函数的执行逻辑，下面我举例说明

- 首先我定义一个高阶函数

```kotlin
//此高阶函数中的函数类型，它会实现两个正整数的某种算数操作	 	 	
fun num1AndNum2(num1:Int,num2:Int,calculate:(n1:Int,n2:Int)->Int):Int{
        return calculate(num1,num2)
}
```



- 调用高阶函数的方法

(1)定义一个与函数类型匹配的函数A，然后在调用时将该函数传入函数类型，参入时的写法为::A	

```kotlin
//两数相加
fun add(n1:Int,n2:Int):Int{
    return n1+n2
}
//两数相乘
fun multiplication(n1:Int,n2:Int):Int{
    return n1*n2
}


//调用高阶函数，其具体的运算逻辑就是我们传入的add和multiplication，分别执行两个数相加和两个数相乘
val result1=num1AndNum2(2,5,::add)
val result2=num1AndNum2(2,5,::multiplication)
```

(2)直接使用Lambda高阶函数

```kotlin
val result1=num1AndNum2{n1,n2->n1+n2}
val result2=num1AndNum2{n1,n2->n1*n2}
```



### 一个常用的简化代码实例

```kotlin
fun StringBuilder.build(block:StringBuilder.()->Unit):StringBuilder{
    block()
    return this
}
```

上述代码中我们给StringBuilder类定义了一个build扩展函数，这个扩展函数接收了一个函数类型，函数类型前面加上了StringBuilder.，表明我们的函数类型定义在StringBuilder这个类中。

然后当我们调用这个扩展函数时，Lambda表达式中就会拥有StringBuilder的上下文，且函数的返回值为StringBuilder，所以我们可以使用build这个扩展函数来简化StringBuilder对字符串的构建。

```kotlin
val stringBuiler=StringBuilder().buid{
    append("abc")
    append("qwe")
}
```









# 内联函数

我们在使用Lambda表达式调用高阶函数时，实际上Lambda表达式在底层会被转换成匿名类的实现方式，所以每当我们调用一次就会创建一个匿名类，这回造成额外的内存和性能开销，然而内联函数就是解决这一问题的方法，它可以将这种运行时开销完全消除。

内联函数的基本使用方法：在定义高阶函数时给它加上inline关键字声明

例如

```kotlin
inline fun num1AndNum2(num1:Int,num2:Int,calculate:(n1:Int,n2:Int)->Int):Int{
        return calculate(num1,num2)
}
```

工作原理：Kotlin编译器会将内联函数中的代码在编译的时候自动替换到调用它的地方

下面我举例说明

首先Lambda表达式中的代码会被替换到内联函数当中函数类型参数调用的地方

然后内联函数中所有的代码会被替换到内联函数调用的地方

最终的代码就是这样了

###  noinline(不内联)

内联的函数类型参数在编译的时候代码会进行替换，所以它没有真正意义上的参数属性，只能传递给另一个内联函数；而非内联函数有真正的参数属性，它可以传递给任何函数。由于内联的函数存在这种局限性，Kotlin就提出了noinline来处理这种尴尬的情况。

如果我们内联函数中有多个函数类型参数，但是我们只想内联其中某些，这是就使用oninline来达到我们的目的

```
inline fun test(do1:(Int)->Unit,noinline do2:()->Unit){     
}
```

在这个内联函数中我们给do2参数生命了noinline关键字，所以只有do1会被内联

**注意：**内联函数可以使用return关键字进行函数返回，非内联函数()只能使用*return@函数名*的方式进行局部返回

### crossinline

保证内联函数中的Lambda表达式中一定不会使用return

如果我们在声明为内联函数的高阶函数中创建了另外的Lambda或者匿名类的实现，并且在这些实现中调用了函数类型参数，就会提示错误，原因如下

- Lambda表达式在编译的时候会被转换成匿名类的实现方式，这里就是将函数类型参数传入匿名类调用
- 内联函数是允许使用return的
- 但是由于是在匿名类中调用的函数类型参数(类似与非内联函数，不能使用return)，所以不能使用return进行外层调用返回



internal 同一模块中可见

object：静态

object修饰的类是Singleton（单例）。



companion object 就是 Java 中的 static 变量

companion object 只能定义在对应的类中



## 静态内部类的单例写法

**加载一个类时，其内部类不会同时被加载。一个类被加载，当且仅当其某个静态成员（静态域、构造器、静态方法等）被调用时发生**，也就是说内部类：`SingleTon`只有我们调用`getInstance()`的时候才会被加载。

    private static class SingleTon{
        private static SingleTon uniqueInstance = new SingleTon();
    }
    
    public static SingleTon getInstance(){
        return SingleTon.uniqueInstance;
    }
    
    private SingleTon(){
    	
    }


对应的kotlin版本

```
class SingleTon {
    private object Holder{
        internal val INSTANCE=SingleTon()
    }

    companion object{
        val instance:SingleTon
        get() = Holder.INSTANCE
    }
}
```





init

所有主构造函数的逻辑写在init里面

![image-20210604182854340](C:\Users\weiwai\AppData\Roaming\Typora\typora-user-images\image-20210604182854340.png)

