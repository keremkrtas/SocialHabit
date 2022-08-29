package com.keremktas.socialhabit.model

data class User(
    val id: String,
    val name: String,
    val Username: String,
    val email: String,
    val photoUrl: String,
    val availableSubjectsId: List<String>,
    val bio: String="",
    val followingsId: List<String>?,
    val followersId: List<String>?
)