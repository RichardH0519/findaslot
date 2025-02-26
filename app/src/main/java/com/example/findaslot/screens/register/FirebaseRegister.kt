package com.example.findaslot.screens.register

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

object FirebaseDatabaseManager {
    // specify "accounts" node in realtime db
    private val database = FirebaseDatabase.getInstance().reference.child("accounts")

    // function to handle user register logic
    fun registerUser(
        username: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        // check for blank inputs
        if (username.isBlank() || password.isBlank()) {
            onFailure("Username or password can't be left empty")
            return
        }
        // listens to db changes
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            // takes snapshot of data after db changes
            override fun onDataChange(snapshot: DataSnapshot) {
                // getting all accounts from db
                val accounts = snapshot.children.mapNotNull { it.getValue(Account::class.java) }
                // checking if username already exists in db
                val existingAccount = accounts.find { it.username == username }
                // if username already in db
                if (existingAccount != null) {
                    onFailure("Username already exists")
                }
                // if username not in db, write to db
                else {
                    // preps data to write to db (hash key, username, password)
                    val userMap = hashMapOf<String, Any>(
                        "username" to username,
                        "password" to password
                    )
                    // writing to db
                    database.push().setValue(userMap)
                        .addOnCompleteListener { task ->
                            // if successfully written to db
                            if (task.isSuccessful) {
                                onSuccess() // refs RegisterScreen.kt (move to login screen)
                            }
                            // if failure to write to db
                            else {
                                onFailure("Failed in writing to db") // refs RegisterScreen.kt (message/no action)
                            }
                        }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                onFailure("Failed in writing to db")
            }
        })
    }

    data class Account(
        val username: String = "",
        val password: String = ""
    )
}