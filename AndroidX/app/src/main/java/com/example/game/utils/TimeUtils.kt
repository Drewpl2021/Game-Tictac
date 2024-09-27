package com.example.game.utils

import android.content.Context
import java.util.Calendar

fun isNight():Boolean{
    val hora=
        Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    return (hora<=6 || hora>=18)
}
object conttexto{
    lateinit var CONTEXTO_APPX:Context
}