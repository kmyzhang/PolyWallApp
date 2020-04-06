package com.senders.polywallapp

data class User(var firstName : String? = "",
                var lastName : String?= "",
                var email : String? = "",
                var password : String? = "",
                var log : MutableList<UserRoute>)
