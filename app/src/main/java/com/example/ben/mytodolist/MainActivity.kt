package com.example.ben.mytodolist

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var task = findViewById<EditText>(R.id.txtTask)
        var names = findViewById<EditText>(R.id.txtMessage)
        var btnMessage = findViewById<Button>(R.id.btnMessage)
        var messages = findViewById<TextView>(R.id.txtNotes)

        val ref = FirebaseDatabase.getInstance().getReference("message")

        btnMessage.setOnClickListener{
            txtTask.requestFocus()
            var messageid = ref.push().key
            var messageg = Message(messageid,task.text.toString(),names.text.toString())
            hideKeyboard()
            task.setText("")
            names.setText("")
            txtTask.requestFocus()
            ref.child(messageid).setValue(messageg).addOnCompleteListener{
                Toast.makeText(this, "Task Added!", 3).show()
            }
        }
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot){
                messages.text = ""
                val children = dataSnapshot.children
                children.forEach {
                    println("data: " + it.toString())
                    if (messages.text.toString() !="") {
                        messages.text = messages.text.toString() + "\n" + "Task: " + it.child("name").value.toString() + " " + "Desc: " + it.child("message").value.toString()
                    }
                    else
                    {
                        messages.text = "My Tasks"
                        messages.text = messages.text.toString() + "\n" + "Task: " + it.child("name").value.toString() + " " + "Desc: " + it.child("message").value.toString()
                    }
                }
            }
            override fun onCancelled(error : DatabaseError) {
                Log.w("Message", "Failed to read value.", error.toException())
            }
        })

    }
    fun hideKeyboard(){
        try{
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken,0)
        } catch (e: Exception){

        }
    }

}
