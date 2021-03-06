package com.bounswe2020group3.paperlayer.data.follow

import com.bounswe2020group3.paperlayer.data.user.User

interface ListableFollow {
    var id: Int
    fun fetchFromUser(): User
    fun fetchToUser(): User
    fun fetchCreated(): String
}