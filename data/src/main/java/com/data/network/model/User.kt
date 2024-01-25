package com.data.network.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class User(
    @SerializedName("id") val id: String,
    @SerializedName("error") val error: String? = null
) : Serializable
