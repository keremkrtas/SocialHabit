package com.keremktas.socialhabit.model

data class Habit(
    val id: String,
    val mainHabitId: String,
    val publisherId: Int,
    val subjectId: Int,
    val title: String,
    val body: String,
    val photoUrl: String,
    val likesCount: Int,
    val likerId: List<String>,
    val whichDay: Int
)