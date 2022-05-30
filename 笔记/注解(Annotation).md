# 注解(Annotation)

Annotation是JDK5.0引入的，注解是代码里的特殊标记，可以在编译、类加载、运行时被读取，并执行响应的处理。我们在不改变代码原有逻辑的情况下，可使用注解在源文件中嵌入一些补充信息。通过反射机制编程实现对这些元数据的访问

- 可以对程序作出解释
- 可以被其他程序读取



## 内置注解

java内置的注解有4种，直接使用

| @Override         | 对覆盖超类中的方法进行标注                                   |
| ----------------- | ------------------------------------------------------------ |
| @Deprecated       | 对不鼓励使用或者已经过时的方法添加注解（已不推荐使用）       |
| @SuppressWarnings | 取消特定代码段中的警告                                       |
| @SafeVarargs      | 声明使用了可变长参数的方法，在与泛型类一起使用时不会出现类型安全问题 |



## 元注解

用来注解其他注解，从而创建新的注解

| @Targe      | 注解所修饰的对象范围                 |
| ----------- | ------------------------------------ |
| @Inherited  | 表示注解可以被继承                   |
| @Documented | 表示这个注解应该被JavaDoc工具记录    |
| @Retention  | 用来声明注解的保留策略               |
| @Repeatable | 允许一个注解在同一声明类型中多次使用 |



@Targe注解的取值是一个ElementType类型的数组

| ElementType.TYPE            | 能修饰类、接口或者枚举类型 |
| --------------------------- | -------------------------- |
| ElementType.FIELD           | 能修饰成员变量             |
| ElementType.METHOD          | 能修饰方法                 |
| ElementType.PARAMETER       | 能修饰参数                 |
| ElementType.CONSTRUCTOR     | 能修饰构造方法             |
| ElementType.LOCAL_VARIABLE  | 能修饰局部变量             |
| ElementType.ANNOTATION_TYPE | 能修饰注解                 |
| ElementType.PACKAGE         | 能修饰包                   |
| ElementType.TYPE_PARAMETER  | 类型参数声明               |
| ElementType.TYPE_USE        | 使用类型                   |



@Retention注解有3中类型，分别表示不同级别的保留策略

| RetentionPolicy.SOURCE  | 注解信息只保留在.java源码中                         |
| ----------------------- | --------------------------------------------------- |
| RetentionPolicy.CLASS   | 编译时注解，注解信息可保留在.java源码中以及.class中 |
| RetentionPolicy.RUNTIME | 运行时注解，java程序运行时，JVM可会保留注解信息     |





## 自定义注解

使用@interface关键字自定义注解，自动继承java.lang.annotation.Annotation接口

1.定义注解

```java
@Target(ElementType.METHOD)   //能修饰方法
@Retention(RetentionPolicy.RUNTIME)   //运行时注解
@interface MyAnnotation{
    String name();
}
```

上面的代码定义了一个修饰方法的运行时注解，其中有一个成员变量name，**注解中只有成员变量，没有方法**，成员变量形式为：参数类型+参数名()，方法名定义了该成员变量的名字，返回值定义了该成员变量的类型。



2.使用该注解

```java
@MyAnnotation(name="张三")
public void test(){}
```

由于该注解存在成员变量，所以在使用时必须为注解赋值

**也可用default为成员变量添加默认值**

```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@interface MyAnnotation{
    String name();
    int age() default 18; 
}
```

这里的age就进行了默认赋值，使用注解时可不为其赋值，使用默认值



**注解需要配合反射机制才能发挥作用，下面我们了解注解是如何配合反射使用的**



# 反射机制(Reflection)

反射机制允许程序在执行期借助于Reflection API取得任何类的内部信息，并能直接操作任意对象的内部属性及方法，可在程序运行期间进行相应操作，是Java被视为动态语言的关键。


**反射所提供的一些功能**

- 在运行时判断任意一个对象所属的类

- 在运行时构造任意一个类的对象
- 在运行时判断任意一个类所具有的成员变量和方法
- 在运行时获取泛型信息
- 在运行时调用任意一个对象的成员变量和方法
- 在运行时处理注解
- 生成动态代理



**优点** 

- 可以实现动态创建对象和编译，体现出很大的灵活性
- 实现程序的解耦

**缺点**

- 使用功能反射基本上是一种解释操作，需要通过JVM来执行，性能会下降



**Reflection的api**

- java.lang.Class:代表一个类
- java.lang.reflect.Method :代表类的方法
- java.lang.reflect.Field : 代表类的成员变量
- java.lang.reflect.Constructor :代表类的构造器



**首先我们需要获取Class类的实例**

- Class clazz = A.class; 若已知具体的类，通过类的class属性获取，该方法最为安全可靠，程序性能最高。
- Class clazz= A.getClass(); 已知某个类的实例，调用该实例的getClass()方法获取Class对象
- （**常用**）Class clazz=Class.forName(""demo.b"); 已知一个类的全类名，且该类在类路径下，可通过Class类的静态方法forName()获取
- 内置基本数据类型可以直接用类名.Type
- 还可以利用ClassLoader我们之后讲解



获取了Class类的实例后，通过反射的方法来获取类的内部信息，如Field、Method、Constructor、Superclass、Interface、Annotation。



**常用的反射方法**

成员变量

- `public Field getField(String name)`：获取指定名称的成员变量(public)。
- `public Field[] getFields()`：获取全部成员变量(public)。
- `public Field getDeclaredField(String name)`
- `public Field[] getDeclaredFields()`

成员方法

- `public Method getMethod(String name,Class<?>... parameterTypes)`
- `public Method[] getMethods()`
- `public Method getDeclaredMethod(String name,Class<?>... parameterTypes）`
- `public Method[] getDeclaredMethods()`

构造方法

- `public Constructor<T> getConstructor(Class<?>... parameterTypes)`
- `public Constructor<?>[] getConstructors()`
- `public Constructor<T> getDeclaredConstructor(Class<?>... parameterTypes)`
- `public Constructor<?>[] getDeclaredConstructors()`

其余获取其它信息的方法都类似

setAccessible() 启动和禁用访问安全检查的开关





# 操作运行时注解

通过反射操作运行时注解

**（1）定义注解**

这里假设定义一个数据库的注解，包括表的注解和列的注解

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@interface TableAnnotation{
    String value();
}


@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@interface FieldAnnotation{
    String columnName();
    String type();
}

```



**（2）使用注解**

通过反射指定表名、列名和类型

```java
@TableAnnotation("user.db")
class User{

    @FieldAnnotation(columnName = "name",type = "int")
    String id;
    @FieldAnnotation(columnName = "password",type = "varchar")
    String password;

    public User(){

    }

    public User(String id, String password) {
        this.id = id;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
```



**（3）使用反射操作注解**

通过反射让我们在运行时获取到注解所呈现的表名、列名和每列对应类型

```
public static void main(String[] args) throws ClassNotFoundException, NoSuchFieldException {

    Class user1=Class.forName("com.wei.service.network.User");

    //通过反射获取类所有的注解
    Annotation[] annotations=user1.getAnnotations();

    for (Annotation annotation : annotations) {
        System.out.println(annotation);
    }

    //获取注解的值
    //首先获取指定的注解
    TableAnnotation tableAnnotation= (TableAnnotation) user1.getAnnotation(TableAnnotation.class);
    String value=tableAnnotation.value();
    System.out.println(value);

    //获取类中指定字段的注解
    //首先获取字段
    Field field=user1.getDeclaredField("id");
    //获取字段的注解
    FieldAnnotation fieldAnnotation=field.getAnnotation(FieldAnnotation.class);
    System.out.println(fieldAnnotation.columnName());
    System.out.println(fieldAnnotation.type());
}
```

运行结果：

![image-20210802095555441](C:\Users\weiwai\AppData\Roaming\Typora\typora-user-images\image-20210802095555441.png)



