package com.keremktas.socialhabit.model

data class Subject(
    val id: String,
    val name: String,
    val iconUrl: String,
    val registeredUserCount: Int ,
    val registeredUsersId: List<String>?
)