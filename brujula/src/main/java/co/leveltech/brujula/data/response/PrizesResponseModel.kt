package co.leveltech.brujula.data.response

import com.google.gson.annotations.SerializedName

internal data class PrizesResponseModel(
    @SerializedName("nonce")
    val nonce: Int,
    @SerializedName("prizes")
    val prizes: List<Prize>
) {
    internal data class Prize(
        @SerializedName("id")
        val id: String?,
        @SerializedName("name")
        val name: String?,
        @SerializedName("received")
        val received: Long
    )
}