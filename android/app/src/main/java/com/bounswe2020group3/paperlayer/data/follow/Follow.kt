package com.bounswe2020group3.paperlayer.data.follow

import com.bounswe2020group3.paperlayer.data.user.User
import com.squareup.moshi.Json

class Follow(
    @field:Json(name = "from_user")
    var fromUser: User,
    @field:Json(name = "to_user")
    var toUser: User,
    @field:Json(name = "created")
    var created: String,
) {}