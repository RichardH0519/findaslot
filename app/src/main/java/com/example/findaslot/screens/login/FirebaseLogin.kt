package com.example.findaslot.screens.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

object FirebaseDatabaseManager {
    // specify "accounts" node in realtime db
    private val database = FirebaseDatabase.getInstance().reference.child("accounts")

    // Global variable to store current logged-in user's account info
    var currentUserAccount: Account? = null

    var loggedInStatus by mutableStateOf(false)

    // function to handle user login logic
    fun loginUser(
        username: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        // listens to db changes
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            // takes snapshot of data after db changes
            override fun onDataChange(snapshot: DataSnapshot) {
                val accounts =
                    snapshot.children.mapNotNull { it.getValue(Account::class.java) } // get all accounts
                val matchingAccount =
                    accounts.find { it.username == username && it.password == password } // get matching account
                // if matching account found
                if (matchingAccount != null) {
                    println("Account found: $matchingAccount") // console output (for internal use)
                    currentUserAccount = matchingAccount  // Save the current user account globally
                    loggedInStatus = true
                    onSuccess() // refs LoginScreen.kt (move to main screen)
                } else {
                    onFailure() // refs LoginScreen.kt (message/no action)
                    println("Account doesn't exist") // console output (for internal use)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                onFailure()
            }
        })
    }

    data class Account(
        val username: String = "",
        val password: String = ""
    )
}


