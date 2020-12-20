package com.raiyansoft.darnaapp.repositories

import com.raiyansoft.darnaapp.model.register.Activation
import com.raiyansoft.darnaapp.model.setting.CallData
import com.raiyansoft.darnaapp.network.ServiceBuilder
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Part
import retrofit2.http.Query

class ApiRepository {

    suspend fun getSettings() = ServiceBuilder.apis!!.getSettings()

    suspend fun getFaq(lang: String) = ServiceBuilder.apis!!.getFaq(lang)

    suspend fun getPolicy(lang: String) = ServiceBuilder.apis!!.getPolicy(lang)

    suspend fun getTerms(lang: String) = ServiceBuilder.apis!!.getTerms(lang)

    suspend fun aboutUs(lang: String) = ServiceBuilder.apis!!.aboutUs(lang)

    suspend fun callUs(lang: String, Authorization: String, call: CallData) =
        ServiceBuilder.apis!!.callUs(lang, Authorization, call)

    suspend fun register(lang: String, params: Map<String, RequestBody>) =
        ServiceBuilder.apis!!.register(lang, params)

    suspend fun logout(lang: String, Authorization: String) =
        ServiceBuilder.apis!!.logout(lang, Authorization)

    suspend fun activate(lang: String, Authorization: String, activation: Activation) =
        ServiceBuilder.apis!!.activateAccount(lang, Authorization, activation)

    suspend fun resendActivation(lang: String, Authorization: String) =
        ServiceBuilder.apis!!.resendActivation(lang, Authorization)

    suspend fun getCities(lang: String) =
        ServiceBuilder.apis!!.getCities(lang)

    suspend fun getRegions(lang: String, cityId: Int) =
        ServiceBuilder.apis!!.getRegions(lang, cityId)

    suspend fun completeRegister(
        lang: String,
        Authorization: String,
        params: Map<String, RequestBody>
    ) =
        ServiceBuilder.apis!!.completeRegister(lang, Authorization, params)

    suspend fun getCategories(lang: String, Authorization: String) =
        ServiceBuilder.apis!!.getCategories(lang, Authorization)

    suspend fun getProfile(lang: String, Authorization: String) =
        ServiceBuilder.apis!!.getProfile(lang, Authorization)

    suspend fun getBranches(lang: String, Authorization: String) =
        ServiceBuilder.apis!!.getBranches(lang, Authorization)

    suspend fun deleteBranch(lang: String, Authorization: String, id: Int) =
        ServiceBuilder.apis!!.deleteBranch(lang, Authorization, id)

    suspend fun createBranch(
        lang: String,
        Authorization: String,
        params: Map<String, RequestBody>
    ) =
        ServiceBuilder.apis!!.createBranch(lang, Authorization, params)

    suspend fun updateMarketInfo(
        lang: String,
        Authorization: String,
        params: Map<String, RequestBody>,
        image: MultipartBody.Part
    ) =
        ServiceBuilder.apis!!.updateMarketInfo(lang, Authorization, params, image)

    suspend fun changeMarketStatus(
        lang: String,
        Authorization: String,
        params: Map<String, RequestBody>
    ) =
        ServiceBuilder.apis!!.changeMarketStatus(lang, Authorization, params)

    suspend fun getProducts(
        lang: String,
        Authorization: String,
        page: Int
    ) =
        ServiceBuilder.apis!!.getProducts(lang, Authorization, page)

    suspend fun getProductDetails(
        lang: String,
        Authorization: String,
        id: Int
    ) =
        ServiceBuilder.apis!!.getProductDetails(lang, Authorization, id)

    suspend fun getInternalCategories(
        lang: String,
        Authorization: String
    ) =
        ServiceBuilder.apis!!.getInternalCategories(lang, Authorization)

    suspend fun updateQuantity(
        lang: String,
        Authorization: String,
        id: Int
    ) =
        ServiceBuilder.apis!!.updateQuantity(lang, Authorization, id)

    suspend fun createProduct(
        lang: String,
        Authorization: String,
        map : Map<String, RequestBody>,
        images: List<MultipartBody.Part>
    ) =
        ServiceBuilder.apis!!.createProduct(lang, Authorization, map, images)

    suspend fun updateProduct(
        lang: String,
        Authorization: String,
        map : Map<String, RequestBody>,
        images: List<MultipartBody.Part>
    ) =
        ServiceBuilder.apis!!.updateProduct(lang, Authorization, map, images)

    suspend fun deleteProduct(
        lang: String,
        Authorization: String,
        id: Int
    ) =
        ServiceBuilder.apis!!.deleteProduct(lang, Authorization, id)

    suspend fun deleteImage(
        lang: String,
        Authorization: String,
        id: Int
    ) =
        ServiceBuilder.apis!!.deleteImage(lang, Authorization, id)

    suspend fun getMarketOrders(
        lang: String,
        Authorization: String
    ) =
        ServiceBuilder.apis!!.getMarketOrders(lang, Authorization)

    suspend fun marketOrderDetails(
        lang: String,
        Authorization: String,
        id: Int
    ) =
        ServiceBuilder.apis!!.marketOrderDetails(lang, Authorization, id)

    suspend fun changeOrderStatus(
        lang: String,
        Authorization: String,
        params: Map<String, RequestBody>
    ) =
        ServiceBuilder.apis!!.changeOrderStatus(lang, Authorization, params)

    suspend fun completeMarketOrder(
        lang: String,
        Authorization: String,
        from: String,
        to: String,
        page: Int
    ) =
        ServiceBuilder.apis!!.completeMarketOrder(lang, Authorization, from, to, page)

    suspend fun canceledMarketOrder(
        lang: String,
        Authorization: String,
        from: String,
        to: String,
        page: Int
    ) =
        ServiceBuilder.apis!!.canceledMarketOrder(lang, Authorization, from, to, page)

    suspend fun refusedMarketOrder(
        lang: String,
        Authorization: String,
        from: String,
        to: String,
        page: Int
    ) =
        ServiceBuilder.apis!!.refusedMarketOrder(lang, Authorization, from, to, page)

    suspend fun changeBranchStatus(
        lang: String,
        Authorization: String,
        params: Map<String, RequestBody>
    ) =
        ServiceBuilder.apis!!.changeBranchStatus(lang, Authorization, params)

    suspend fun getBranchOrders(
        lang: String,
        Authorization: String
    ) =
        ServiceBuilder.apis!!.getBranchOrders(lang, Authorization)

    suspend fun completeDeliveryRegister(
        lang: String,
        Authorization: String,
        params: Map<String, RequestBody>
    ) =
        ServiceBuilder.apis!!.completeDeliveryRegister(lang, Authorization, params)

}