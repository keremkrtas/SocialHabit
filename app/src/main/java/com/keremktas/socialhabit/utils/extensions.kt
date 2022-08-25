package com.keremktas.socialhabit.utils

import android.content.Context
import android.content.Intent

fun Intent.startAct(context: Context, activity : Class<*>? ){
    context.startActivity(Intent(context,activity))
}
