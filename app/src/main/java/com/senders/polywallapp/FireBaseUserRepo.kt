package com.senders.polywallapp

import android.provider.ContactsContract
import android.renderscript.Sampler
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


private var TAG: String = "FireBaseUserRepo"

class FireBaseUserRepo {

    var mDatabaseReference = FirebaseDatabase.getInstance().reference
    var mAuth = FirebaseAuth.getInstance()
    var helper = Helpers()

    //Data
    var routeList : MutableList<DataSnapshot>
    var userRouteList : MutableList<DataSnapshot>

    init {
        routeList = mutableListOf<DataSnapshot>()
        userRouteList = mutableListOf<DataSnapshot>()
    }

    private fun verifyEmail() : Boolean {
        var verified = false
        val mUser = mAuth.currentUser
        mUser!!.sendEmailVerification()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    verified = true
                } else {
                    Log.e(TAG, "sendEmailVerification", task.exception)
                }
            }
        return verified
    }

    private fun addChild(user : User){
        val currentUserDB = mDatabaseReference!!.child("Users").child(mAuth!!.currentUser!!.uid)
        //currentUserDB.setValue(user)
        currentUserDB.child("firstName").setValue(user.firstName)
        currentUserDB.child("lastName").setValue(user.lastName)
        currentUserDB.child("email").setValue(user.email)
        currentUserDB.child("password").setValue(user.password)
        currentUserDB.child("log").setValue("")
    }


    fun createNewAccount(user : User) : String? {
        var message : String? = null
        if (!helper.isFormEmpty(user)) {
            mAuth.createUserWithEmailAndPassword(user.email!!, user.password!!)
                .addOnCompleteListener() { task ->
                    if (task.isSuccessful) {
                        addChild(user)
                        if(verifyEmail()) {
                            message = "success"
                            Log.d(TAG, message!!)
                        }

                    } else {
                        var e = task.exception.toString()
                        message = e
                        Log.d(TAG, e)
                    }

                }
        }
        return message
    }

    fun currentUserExists() : Boolean {
        if (mAuth.currentUser!= null) {
            return true
        }
        return false
    }

    suspend fun getUser(): User =
        suspendCoroutine {cont ->
            var resumed = false
            val currentUser = mDatabaseReference!!.child("Users").child(mAuth!!.currentUser!!.uid)
            currentUser.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val logList = mutableListOf<UserRoute>()
                    if (dataSnapshot.child("log").hasChildren()) {
                        for (i in dataSnapshot.child("log").children) {
                            Log.d(TAG, i.value.toString())
                            val route = UserRoute(i.child("id").value.toString(),
                                i.child("type").value.toString(),
                                i.child("date").value.toString())
                            logList.add(route)
                        }
                    }
                    val user = User(dataSnapshot.child("firstName").value.toString(),
                        dataSnapshot.child("lastName").value.toString(),
                        dataSnapshot.child("email").value.toString(),
                        dataSnapshot.child("password").value.toString(),
                        logList)
                    if (!resumed) {
                        cont.resume(user)
                        resumed = true
                    }
                }

                override fun onCancelled(p0: DatabaseError) {
                    Log.w(TAG, "Failed to read value", p0.toException())
                }
            })
        }

    suspend fun getRoutes(): MutableList<Route> =
        suspendCoroutine {cont ->
            var resumed = false
            val routes = mDatabaseReference!!.child("Routes")
            routes.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val tempRouteList = mutableListOf<Route>()
                    for (routeSnapshot in dataSnapshot.children) {
                        val route = Route(routeSnapshot.child("id").value.toString(),
                            routeSnapshot.child("imgUrl").value.toString(),
                            routeSnapshot.child("expDate").value.toString(),
                            routeSnapshot.child("name").value.toString(),
                            routeSnapshot.child("rating").value.toString(),
                            routeSnapshot.child("type").value.toString())
                        tempRouteList.add(route)
                    }
                    if (!resumed) {
                        cont.resume(tempRouteList)
                        resumed = true
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value
                    Log.w("data", "Failed to read value.", error.toException())
                }
            })
        }


    fun addUserRouteItem(route: UserRoute): Task<Void> {
        Log.d("FirebaseRepo", "adding route to db")
        val currentUserRouteDB = mDatabaseReference!!.child("Users").child(mAuth!!.currentUser!!.uid).child("log")
        return currentUserRouteDB.push().setValue(route)
    }

    fun addRouteItem(route: Route): Task<Void> {
        Log.d("FirebaseRepo", "adding route to db")
        val currentUserRouteDB = mDatabaseReference!!.child("Routes").child(route.id)
        return currentUserRouteDB.setValue(route)
    }


    //TODO error checking
    fun getEmail() : String?{
        val user = mAuth.currentUser
        return user?.email
    }

    //TODO error checking
    fun updateEmail(newEamil : String){
        mAuth.currentUser?.updateEmail(newEamil)
    }

    //TODO error checking
    fun updatePassword(newpass : String){
        mAuth.currentUser?.updatePassword(newpass)
    }
}