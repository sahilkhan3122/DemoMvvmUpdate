package retrofit

import modal.DefaultResponse
import modal.LoginResponse
import modal.ProfileModal
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface Service {
    // request body will use form URL encoding. Fields should be declared as parameters and annotated with @Field.
    @FormUrlEncoded
    @POST("register.php")
    fun createUser(
        @FieldMap hashMap: HashMap<String, String>
    ): Call<DefaultResponse>

    @FormUrlEncoded
    @POST("login.php")
    fun loginUser(
        @FieldMap hashMap: HashMap<String, String>
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("profile.php")
    fun profileUser(
        @Field("user_id") user_id: Int
    ): Call<ProfileModal>
}

