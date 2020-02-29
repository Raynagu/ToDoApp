package com.admin.todoapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.admin.todoapp.DTO.ToDo

class DBHandler(val context: Context): SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val createToDoTable = "  CREATE TABLE $TABLE_TODO (" +
                "$COL_ID integer PRIMARY KEY AUTOINCREMENT," +
                "$COL_CREATED_AT datetime DEFAULT CURRENT_TIMESTAMP," +
                "$COL_NAME varchar);"
        val createToDoItemTable =
            "CREATE TABLE $TABLE_TODO_ITEM (" +
                    "$COL_ID integer PRIMARY KEY AUTOINCREMENT," +
                    "$COL_CREATED_AT datetime DEFAULT CURRENT_TIMESTAMP," +
                    "$COL_TODO_ID integer," +
                    "$COL_ITEM_NAME varchar," +
                    "$COL_IS_COMPLETED integer);"

        db.execSQL(createToDoTable)
        db.execSQL(createToDoItemTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }


    // addToDo() add new To do to th DB
    fun addToDo(toDo: ToDo): Boolean{
        //get writeableDatabase object to write DB
        val db = writableDatabase
        //get contentValues() object to store the col & values
        val cv = ContentValues()
        //put the values in ContentValues object
        cv.put(COL_NAME, toDo.name)

        //insert opreation
        val result = db.insert(TABLE_TODO, null, cv)

        //return result
        return result!=(-1).toLong()
    }

    // getToDo() read all To do to th DB
    fun getToDo(): MutableList<ToDo>{
        //create arrayList
        val result: MutableList<ToDo> = ArrayList()
        //create readableDatabase object
        val db = readableDatabase
        //read DB/ rawQuery()
        val qResult = db.rawQuery("SELECT * FROM $TABLE_TODO", null)

        //get db result by iterating, before check for result enpty or not
        if(qResult.moveToFirst()){
            do{
                //create To Do class object store the data
                val toDo = ToDo()
                //get the columns by getColumnIndex()
                toDo.id = qResult.getLong(qResult.getColumnIndex(COL_ID))
                toDo.name = qResult.getString(qResult.getColumnIndex(COL_NAME))

                //add object to result list
                result.add(toDo)
            }while(qResult.moveToNext())
        }

        //close the connection and return result
        qResult.close()
        return result
    }
}