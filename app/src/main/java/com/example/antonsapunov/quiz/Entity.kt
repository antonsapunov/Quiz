package com.example.antonsapunov.quiz

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class Entity(

        @SerializedName("response_code")
        val responseCode: Int = 11,

        @SerializedName("results")
        val questions: List<Question>
)

data class Question (
        @SerializedName("category")
        val category: String,

        @SerializedName("difficulty")
        val difficulty: String,

        @SerializedName("question")
        val question: String,

        @SerializedName("correct_answer")
        val correct_answer: String,

        @SerializedName("incorrect_answers")
        val incorrect_answers: List<String>,

        var answers: List<String>
): Serializable
