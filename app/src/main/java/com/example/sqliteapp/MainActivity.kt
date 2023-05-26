package com.example.sqliteapp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.icu.text.CaseMap.Title
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

private operator fun Boolean.invoke(value: () -> Boolean) {

}

class MainActivity : AppCompatActivity() {
    lateinit var Edtname: EditText
    lateinit var Edtemail: EditText
    lateinit var Edtnumber: EditText
    lateinit var Btnsave: Button
    lateinit var Btnview: Button
    lateinit var Btndelete: Button
    lateinit var db: SQLiteDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Edtname = findViewById(R.id.edtname)
        Edtemail = findViewById(R.id.edtemail)
        Edtnumber = findViewById(R.id.edtID)
        Btnsave = findViewById(R.id.btnsave)
        Btnview = findViewById(R.id.btnview)
        Btndelete = findViewById(R.id.btndelete)
        //create a database
        db = openOrCreateDatabase(
            "emobilisdb",
            Context.MODE_PRIVATE, null
        )
        //create a table inside a database
        db.execSQL("CREATE TABLE IF NOT EXISTS users(name VARCHAR, email VARCHAR, id_number VARCHAR)")
        Btnsave.setOnClickListener {
            //Receive data from the user
            var name = Edtname.text.toString()
            var email = Edtemail.text.toString()
            var idnumer = Edtnumber.text.toString()
            //check if the user is submitting empty fields
            if (name.isEmpty()) {
                Edtname.setError("Please fill this input")
                Edtname.requestFocus()
            } else if (email.isEmpty()) {
                Edtemail.setError("Please fill this input")
                Edtemail.requestFocus()
            } else if (idnumer.isEmpty()) {
                Edtnumber.setError("please fill this input")
                Edtnumber.requestFocus()
            } else {
                //proceed to save the data
                db.execSQL("INSERT INTO users VALUES($name, $email, $idnumer)")
                Toast.makeText(this, "Data saved successfully", Toast.LENGTH_SHORT).show()
                Edtname.setText(null)
                Edtemail.setText(null)
                Edtnumber.setText(null)
            }
        }

        Btnview.setOnClickListener {
            //user cursor to select all the users
            var cursor = db.rawQuery(
                "SELECT * FROM users", null
            )
            //check if theres any record in the db
            if (cursor.count == 0) {
                displayUser("NO RECORDS", "Sorry, no data")
            } else {
                //Use a string buffer to append record from the db
                var buffer = StringBuffer()
                while (cursor.moveToNext()) {
                    var retrievedName = cursor.getString(0)
                    var emaildisplayed = cursor.getString(1)
                    var idNumberdisplayed = cursor.getString(2)
                    buffer.append(retrievedName + "\n")
                    buffer.append(emaildisplayed + "\n")
                    buffer.append(idNumberdisplayed + "\n")

                }
               displayUsers ("USERS", buffer.toString())
            }

        }

        Btndelete.setOnClickListener {
            //Recieve the id of the user to be deleted
            var idNumber = Edtnumber.text.toString()
            //check if the idNumber recieved is empty
            if(idNumber.isEmpty()){
                Edtnumber.setError("please fill this input")
                Edtnumber.requestFocus()
            }
            else{
            //proceed to delete
            //use cursor to select the user with the id
           var cursor = db.rawQuery(
               "SELECT * FROM users WHERE id_number=NUmber",null
           )

                //check if the user with the provided exists
                if (cursor.count == 0){
                displayUsers("NO USER","sorry, no data")
        }else{
            // Delete the user
            db.execSQL(
                "DELETE FROM users WHERE id_number=$idNumber "
            )
            displayusers("SUCCESS", "User deleted")
            Edtnumber.setText(null)

        }

        }
    }
    fun displayUsers(title: String, message: String) {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle(title)
        alertDialog.setMessage(message)
        alertDialog.setPositiveButton("close", null)
        alertDialog.create().show()
    }
}
}