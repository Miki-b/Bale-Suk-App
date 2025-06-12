package android.example.balesuk.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build.VERSION
import android.content.ContentValues


class product_db(context: Context):SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION) {
    companion object{
        private const val DATABASE_NAME="product.db";
        private const val DATABASE_VERSION=1;
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable="""
            CREATE TABLE Product(ProductId INTEGER PRIMARY KEY AUTOINCREMENT,
                ProductName TEXT,ProductPrice REAL,
                ProductImageURL TEXT
                )""".trimIndent()
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, v1: Int, v2: Int) {
        db?.execSQL("DROP TABLE Product")
        onCreate(db)
    }
    fun insert(db:SQLiteDatabase?,productName:String,productPrice:Double,productImage:String){
        val values= ContentValues().apply{
            put("ProductName",productName)
            put("ProductPrice",productPrice)
            put("ProductImageURL",productImage)
        }
        db?.insert("Product",null,values)
    }



}