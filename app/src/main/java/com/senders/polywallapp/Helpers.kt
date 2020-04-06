package com.senders.polywallapp

import com.google.android.material.bottomnavigation.BottomNavigationView

//helper functions for any part of the back end

class Helpers {

    fun isFormEmpty(user : User) : Boolean{
        var isEmpty = true
        if(!user.firstName.isNullOrBlank() && !user.lastName.isNullOrBlank() &&
            !user.email.isNullOrBlank() && !user.password.isNullOrBlank()){
            isEmpty = false
        }
        return isEmpty
    }

    fun isLoginEmpty(email : String?, password : String?) : Boolean{
        var result = true
        if(!email.isNullOrBlank() && !password.isNullOrBlank()){
            result = false
        }
        return result
    }

    fun getCheckedItem(btm: BottomNavigationView): Int {
        val menu = btm.menu
        for(i in 0 until menu.size()){
            val item = menu.getItem(i)
            if(item.isChecked){
                return i
            }
        }
        return -1
    }
}