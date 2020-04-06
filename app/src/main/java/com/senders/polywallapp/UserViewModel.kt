package com.senders.polywallapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch


private var TAG: String = "UserViewModel"

class UserViewModel : ViewModel() {

    var mUser = MutableLiveData<User>()
    private var mUserRepo = FireBaseUserRepo()

    init {
        if (mUserRepo.currentUserExists()) {
            Log.d(TAG, "confirmed user exists")
            //setup()
        }
    }

    /*fun setup() {
        viewModelScope.launch {
            val user  = mUserRepo.getUser()
            mUser.postValue(user)
        }

    }*/

    fun getUser() : LiveData<User> {
        viewModelScope.launch {
            val user  = mUserRepo.getUser()
            mUser.postValue(user)
        }
        return mUser
    }

    fun addRouteToUser(route : UserRoute) {
        if (!isRouteInLog(route.id)) {
            mUserRepo.addUserRouteItem(route)
        }
    }

    fun isRouteInLog(id: String) : Boolean {
        //var routeStatus = false
        /*viewModelScope.launch {
            val user  = mUserRepo.getUser()
            routeStatus = mUserRepo.hasRoute(user, id)

        }*/
        for (r in mUser.value!!.log) {
            if (r.id == id) {
                return true
            }
        }
        return false
        //return routeStatus
    }

    fun createNewAccount(user : User){
        mUserRepo.createNewAccount(user)
    }

    /*fun updateEmail(newEmail : String){
        mUserRepo.updateEmail(newEmail)
    }

    fun updatePassword(newpass : String){
        mUserRepo.updatePassword(newpass)
    }



    fun getFirstName() : MutableLiveData<String> {
        if (firstName.value == null) {
            loadFirstName()
        }
        return firstName
    }

    fun loadFirstName() {
        mUserRepo.getUserRef().addValueEventListener(object : ValueEventListener {
            var tempName : String = ""
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.d("checking data snapshot", dataSnapshot.toString())
                tempName = dataSnapshot.child("firstName").value as String
                firstName.postValue(tempName)
            }

            override fun onCancelled(p0: DatabaseError) {
                Log.w(TAG, "Failed to read value", p0.toException())
            }
        })
    }

    fun getLastName() : MutableLiveData<String> {
        if (lastName.value == null) {
            loadLastName()
        }
        return lastName
    }

    fun loadLastName() {
        mUserRepo.getUserRef().addValueEventListener(object : ValueEventListener {
            var tempName : String = ""
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.d("checking data snapshot", dataSnapshot.toString())
                tempName = dataSnapshot.child("lastName").value as String
                lastName.postValue(tempName)
            }

            override fun onCancelled(p0: DatabaseError) {
                Log.w(TAG, "Failed to read value", p0.toException())
            }
        })
    }

    fun getEmail() : String?{
        return mUserRepo.getEmail()
    }*/


}