# 运行时权限申请
所有的危险权限需要进行运行时权限申请


# ContentProvider
访问其他程序中的数据

## 使用现有的ContentProvide
- 通过Context中的getContentResolver()方法获取ContentResolver类的实例
- 使用ContentResolver中的一系列方法对数据进行增删查改操作（insert,update,delete,query）
- 不同于SQLiteDatabase，ContentProvide中的增删查改方法使用Uri，称为内容URL。

**URL**
头部协议+authority+path
头部协议为content://
authority用于对不同的应用程序做区分，一般采用包名
path对同一应用程序中不同表作区分

content://com.example.app.provider/table

（1）获取URL后，将它解析成Uri对象
```kotlin
val uri= Uri.parse("content://com.example.app.provider/table");
```

（2）使用query()查询
```kotlin

val cursor=contentResolver.query(
    uri,          //指定查询某个应用程序下的某一张表
    projection,   //指定查询的列名
    selection,    //指定where的约束条件
    selectionArgs, //为where中的占位符提供具体的值
    sortOrder)     //查询结果的排序方式
```

通过移动游标的位置遍历Cursor的所有行，取出每一行中相应列的数据
```kotlin
while(cursor.moveToNext()){
    val column1=cursor.getString(cursor.getColumnIndex("column1"))
    val column2=cursor.getInt(cutsor.getColumnIndex("column2"))
}

cursor.close()
```

（3）使用insert()插入
```kotlin
val values=contentValuesOf("column1" to "text","column2" to 1)
contentResolver.update(uri,values)
```

(4) 使用update()更新
```kotlin
val values=contentValuesOf("column1" to "")
contentResolver.update(uri,values,"column1=? and column2=?",arrayOf("text",1))
```

（5）使用delete()删除
```kotlin
contentResolver.delete(uri,"column2=?",arrayOf("1))
```

## 创建自己的ContentProvider
新建一个类去继承ContentProvider，实现其中的6个抽象方法

```kotlin

class MyProvider:ContentProvider(){
    
    //完成对数据库的创建和升级操作
    override fun onCreate(): Boolean {
        TODO("Not yet implemented")
    }


    override fun query(
        p0: Uri,
        p1: Array<out String>?,
        p2: String?,
        p3: Array<out String>?,
        p4: String?
    ): Cursor? {
        TODO("Not yet implemented")
    }

    override fun getType(p0: Uri): String? {
        TODO("Not yet implemented")
    }

    override fun insert(p0: Uri, p1: ContentValues?): Uri? {
        TODO("Not yet implemented")
    }

    override fun delete(p0: Uri, p1: String?, p2: Array<out String>?): Int {
        TODO("Not yet implemented")
    }

    override fun update(p0: Uri, p1: ContentValues?, p2: String?, p3: Array<out String>?): Int {
        TODO("Not yet implemented")
    }

}
```