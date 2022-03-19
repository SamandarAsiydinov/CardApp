package uz.context.cardapplication.networking

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import uz.context.cardapplication.database.CardEntity

interface ApiService {

    @GET("cards")
    fun getAllCards(): Call<ArrayList<CardEntity>>

    @POST("cards")
    fun postCards(@Body cards: CardEntity): Call<CardEntity>
}