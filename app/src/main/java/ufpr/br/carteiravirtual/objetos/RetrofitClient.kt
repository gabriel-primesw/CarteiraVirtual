package ufpr.br.carteiravirtual.objetos

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ufpr.br.carteiravirtual.interfaces.AwesomeApiService

object RetrofitClient {
    private const val BASE_URL = "https://economia.awesomeapi.com.br/"

    val instance: AwesomeApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(AwesomeApiService::class.java)
    }
}