package com.example.antonsapunov.quiz

import android.util.Log
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET("api.php")
    fun readQuestions(@Query("amount") amount: Int = 10, @Query("category") category: Int,
                      @Query("difficulty") complexity: String, @Query("type") type: String = "multiple"): Call<Entity>
}
object QuestionsRepository {
    private val api: Api

    private val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://opentdb.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    init {
        api = retrofit.create(Api::class.java)
    }

    suspend fun readQuestions(category: Int, complexity: String): Call<Entity> {
        Log.d("asf", api.readQuestions(10, category, complexity).request().url().toString())
        return api.readQuestions(10, category, complexity.toLowerCase())
    }
}
