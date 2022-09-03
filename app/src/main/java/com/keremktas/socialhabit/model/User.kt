package com.keremktas.socialhabit.model

data class User(
    val id: String,
    val name: String,
    val Username: String,
    val email: String,
    val photoUrl: String,
    val bio: String="",
    val availableSubjectsId: List<String>?= listOf(),
    val followingsId: List<String>?= listOf(),
    val followersId: List<String>?=listOf()
)