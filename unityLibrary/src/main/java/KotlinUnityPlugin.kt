//package com.eleuterlcon.KotlinUnityPlugin

class KotlinUnityPlugin {
    companion object {
        @JvmStatic
        fun getUserData(): String {
            val userData = """{"User_id":"user-id-01","User_name":"Test User","User_email":"testuser@gmail.com","User_points":"0"}"""

            return userData
        }

        @JvmStatic
        fun getPricesList(): Array<String> {
            return arrayOf(
                "1 Points",
                "3 Points",
                "10 Points",
                "50 Points",
                "20 Points",
                "3 Points",
                "0 Points",
                "1 Points",
                "20 Points",
                "1 Points"
            )
        }

        @JvmStatic
        fun informPointsUpdate(newBalance: Int) {
            //Do something in kotlin
        }
    }
}
