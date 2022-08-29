package com.keremktas.socialhabit.model


data class MainHabit(
    val id: String,
    val userId: String,
    val SubjectId: String,
    val dayDuration: Int,
    val whichDay: Int,
    val startingDate: Long,
    )
