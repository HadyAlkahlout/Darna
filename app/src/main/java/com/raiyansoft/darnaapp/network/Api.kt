package com.raiyansoft.darnaapp.network

import com.raiyansoft.darnaapp.model.branch.Branch
import com.raiyansoft.darnaapp.model.categories.Category
import com.raiyansoft.darnaapp.model.city.City
import com.raiyansoft.darnaapp.model.general.FullGeneral
import com.raiyansoft.darnaapp.model.general.FullPagingGeneral
import com.raiyansoft.darnaapp.model.general.General
import com.raiyansoft.darnaapp.model.product.Product
import com.raiyansoft.darnaapp.model.profile.Profile
import com.raiyansoft.darnaapp.model.register.Activation
import com.raiyansoft.darnaapp.model.register.Register
import com.raiyansoft.darnaapp.model.setting.*
import com.raiyansoft.darnaapp.model.setting.faq.Faq
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface Api {

    @GET("setting")
    suspend fun getSettings(): Response<FullGeneral<Settings>>

    @GET("faq")
    suspend fun getFaq(
        @Header("lang") lang: String,
    ): Response<FullGeneral<Faq>>

    @GET("privacy")
    suspend fun getPolicy(
        @Header("lang") lang: String,
    ): Response<FullGeneral<Privacy>>

    @GET("about")
    suspend fun aboutUs(
        @Header("lang") lang: String,
    ): Response<FullGeneral<About>>

    @GET("conditions")
    suspend fun getTerms(
        @Header("lang") lang: String,
    ): Response<FullGeneral<Conditions>>

    @POST("contactUs")
    suspend fun callUs(
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String,
        @Body call: CallData
    ): Response<General>

    @Multipart
    @POST("user/register")
    suspend fun register(
        @Header("lang") lang: String,
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>
    ): Response<FullGeneral<Register>>

    @GET("user/logout")
    suspend fun logout(
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String
    ): Response<General>

    @POST("user/activateAccount")
    suspend fun activateAccount(
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String,
        @Body activation: Activation
    ): Response<General>

    @POST("user/resendActivation")
    suspend fun resendActivation(
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String
    ): Response<General>

    @GET("cities")
    suspend fun getCities(
        @Header("lang") lang: String
    ) : Response<FullGeneral<List<City>>>

    @GET("regions/{id}")
    suspend fun getRegions(
        @Header("lang") lang: String,
        @Path("id") cityId: Int
    ) : Response<FullGeneral<List<City>>>

    @Multipart
    @POST("user/completeRegister")
    suspend fun completeRegister(
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String,
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>
    ) : Response<General>

    @Multipart
    @POST("user/completeDeliveryRegister")
    suspend fun completeDeliveryRegister(
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String,
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>
    ) : Response<General>

    @GET("products/categories")
    suspend fun getCategories(
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String
    ) : Response<FullGeneral<List<Category>>>

    @GET("user/profile")
    suspend fun getProfile(
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String
    ) : Response<FullGeneral<Profile>>

    @GET("user/getBranches")
    suspend fun getBranches(
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String
    ) : Response<FullGeneral<List<Branch>>>

    @GET("user/deleteBranche/{id}")
    suspend fun deleteBranch(
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String,
        @Path("id") id: Int
    ) : Response<General>

    @Multipart
    @POST("user/createBranche")
    suspend fun createBranch(
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String,
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>
    ) : Response<General>

    @Multipart
    @POST("user/marketInfo")
    suspend fun updateMarketInfo(
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String,
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part image: MultipartBody.Part
    ) : Response<General>

    @Multipart
    @POST("user/update/status")
    suspend fun changeMarketStatus(
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String,
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>
    ) : Response<General>

    @GET("user/myProducts")
    suspend fun getProducts(
        @Header("lang") lang: String,
        @Header("Authorization") Authorization: String,
        @Query("page") page: Int
    ) : Response<FullPagingGeneral<Product>>

}