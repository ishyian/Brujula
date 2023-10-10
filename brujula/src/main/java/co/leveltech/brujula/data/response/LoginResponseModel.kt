package co.leveltech.brujula.data.response

import com.google.gson.annotations.SerializedName

internal data class LoginResponseModel(
    @SerializedName("ips")
    val ips: Ips,
    @SerializedName("user")
    val user: User
) {
    internal data class Ips(
        @SerializedName("token")
        val token: String,
        @SerializedName("user")
        val user: String
    )

    internal data class User(
        @SerializedName("name")
        val name: String,
        @SerializedName("token")
        val token: String
    )
}