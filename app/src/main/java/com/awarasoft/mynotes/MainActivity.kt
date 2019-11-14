package com.awarasoft.mynotes

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.system.OsConstants
import android.widget.SimpleCursorAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var db: SQLiteDatabase? = null
    var cursor: Cursor? = null
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Open then NoteDetail activity
        fabAddNotes.setOnClickListener{
            openNoteDetailActiviy(0)
        }

        listViewNotes.setOnItemClickListener {parent, view, position, id ->
            openNoteDetailActiviy(id)
        }
    }

    fun openNoteDetailActiviy(noteId: Long){
        val intent = Intent(this, NoteDetails::class.java)
        intent.putExtra("NOTE_ID",noteId)
        startActivity(intent)
    }

    override fun onStart() {
        super.onStart()

        //Create the database and 'notes table
        var objToCreateDB = MyNotesSQLiteOpenHelper(this)
        db = objToCreateDB.readableDatabase //Access to the database

        cursor = db!!.query("NOTES", arrayOf("_id","title"),//Read data
            null,null,null,null,null)

        val listAdapter = SimpleCursorAdapter(this,
            android.R.layout.simple_list_item_1,
            cursor,
            arrayOf("title"),
            intArrayOf(android.R.id.text1),
            0
        )
        listViewNotes.adapter = listAdapter
    }
    override fun onDestroy() {
        super.onDestroy()
        cursor!!.close()
        db!!.close()
    }

}
