package com.awarasoft.mynotes

import android.content.ContentValues
import android.content.DialogInterface
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_note_details.*

class NoteDetails : AppCompatActivity() {

    var db: SQLiteDatabase? = null
    var noteId:Int = 0
    var cursor: Cursor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_details)

        val myNotesSQLiteOpenHelper = MyNotesSQLiteOpenHelper(this)
        db = myNotesSQLiteOpenHelper.writableDatabase

        noteId = intent.extras.get("NOTE_ID").toString().toInt()

        //Code that read a Notes Titles and Description that its
        if (noteId != 0) {
            cursor = db!!.query(
                "NOTES",
                arrayOf("TITLE", "DESCRIPTION"),
                "_id=?",
                arrayOf(noteId.toString()),
                null, null, null
            )

            if (cursor!!.moveToFirst()) //= true
            {
                editTextTitle.setText(cursor!!.getString(0))
                editTextDescription.setText(cursor!!.getString(1))
            }
        }

    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item!!.itemId == R.id.save_note)
        {
            // Inserting a new note
            val newNoteValues = ContentValues()
            if (editTextTitle.text.trim().isEmpty()) {
                newNoteValues.put("TITLE","Untitled")
            }else {
                newNoteValues.put("TITLE", editTextTitle.text.toString())
            }
            newNoteValues.put("DESCRIPTION", editTextDescription.text.toString())

            if (noteId==0){
                insertNote(newNoteValues)
            }
            else
            {
                updateNote(newNoteValues)
            }
        }
        else if (item!!.itemId == R.id.delete_note)
        {
            deleteNote()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteNote()
    {
        var dialog: AlertDialog
        val builder = AlertDialog.Builder(this)

        //Set a title for the alert dialog
        builder.setTitle("Delete note")

        //Set Alert message
        builder.setMessage("Are you sure to want to delete '${editTextTitle.text}'?")

        //Set the alert dialog positive (Yes) button
        builder.setPositiveButton("YES",dialogClickListener)

        //Set the alert dialog neutral (Cancel) button
        builder.setNegativeButton("CANCEL", dialogClickListener)

        //Initial the AlertDialog by using the builder object
        dialog = builder.create()

        //Display the dialog show

        dialog.show()
    }

    private val dialogClickListener = DialogInterface.OnClickListener{_, which ->
        if (which == DialogInterface.BUTTON_POSITIVE){
            db!!.delete("NOTES","_id=?",arrayOf(noteId.toString()))
            Toast.makeText(this,"Note Deleted!",Toast.LENGTH_SHORT).show()

            finish()//Close activity
        }
    }


    private fun updateNote(noteValues:ContentValues)
    {
        db!!.update("NOTES",noteValues,"_id=?",arrayOf(noteId.toString()))
        Toast.makeText(this,"Note Updated!", Toast.LENGTH_SHORT).show()
    }

    private fun insertNote(newNoteValues:ContentValues) {
        db!!.insert("NOTES", null, newNoteValues)
        Toast.makeText(this, "Note Saved!", Toast.LENGTH_SHORT).show()
        editTextTitle.setText("")
        editTextDescription.setText("")
        editTextTitle.requestFocus()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.note_details_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }
}
