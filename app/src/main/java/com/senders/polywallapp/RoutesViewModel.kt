package com.senders.polywallapp

import androidx.lifecycle.ViewModel
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.launch

//rand
class RoutesViewModel : ViewModel() {
    private var routes = MutableLiveData<MutableList<Route>>()
    private var firebaseRepo: FireBaseUserRepo

    init {
        firebaseRepo = FireBaseUserRepo()
    }

    fun getRoutes(): MutableLiveData<MutableList<Route>> {
        viewModelScope.launch {
            val routeList  = firebaseRepo.getRoutes()
            routes.postValue(routeList)
        }
        return routes
    }

    fun addRoute(route: Route) {
        firebaseRepo.addRouteItem(route)
    }

    fun getRouteById(id: String) : Route {
        var route = Route("","","","")
        for (r in routes.value!!.listIterator()) {
            if (r.id == id) {
                route = r
            }
        }
        return route
    }
}