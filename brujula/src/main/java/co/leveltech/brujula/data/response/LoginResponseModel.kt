package co.leveltech.brujula.data.response


import com.google.gson.annotations.SerializedName

data class LoginResponseModel(
    @SerializedName("ips")
    val ips: Ips,
    @SerializedName("user")
    val user: User
) {
    data class Ips(
        @SerializedName("building")
        val building: String,
        @SerializedName("token")
        val token: String,
        @SerializedName("user")
        val user: String
    )

    data class User(
        @SerializedName("availablePrizes")
        val availablePrizes: Int,
        @SerializedName("name")
        val name: String,
        @SerializedName("prizes")
        val prizes: List<Any>,
        @SerializedName("score")
        val score: Int
    )
}