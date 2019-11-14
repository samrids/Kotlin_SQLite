package com.awarasoft.mynotes

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyNotesSQLiteOpenHelper(context: Context) : SQLiteOpenHelper(context, "MyNoteDB",null,1){
    override fun onCreate(db: SQLiteDatabase?) {

        db!!.execSQL("CREATE TABLE notes(" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "title TEXT,"+
                    "description TEXT)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // TODO
    }

}