package com.senders.polywallapp

import android.content.ContentValues
import android.util.Log
import com.google.firebase.auth.FirebaseAuth

class Login(var email : String, var password : String?, var mAuth : FirebaseAuth){

    private var helper = Helpers()

    fun loginUser(callback: (Boolean)->Unit) : Boolean{
        var result = false
        if (!helper.isLoginEmpty(email, password)) {
            Log.d(ContentValues.TAG, "Logging in user.")
            mAuth.signInWithEmailAndPassword(email, password!!)
                .addOnCompleteListener() { task ->
                    if (task.isSuccessful) {
                        result = true
                    }
                    callback(task.isSuccessful)
                }
        }
        return result
    }

}