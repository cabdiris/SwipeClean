package com.example.swipeclean.utils
//
//import android.os.Bundle
//import android.os.Parcelable
//import androidx.navigation.NavType
//import com.google.gson.Gson
//import kotlin.reflect.KClass
//
//inline fun <reified T : Parcelable> parcelableType(isNullableAllowed: Boolean = false) =
//    object : NavType<T>(isNullableAllowed) {
//        override fun get(bundle: Bundle, key: String): T? = bundle.getParcelable(key)
//        override fun parseValue(value: String): T = Gson().fromJson(value, T::class.java)
//        override fun put(bundle: Bundle, key: String, value: T) = bundle.putParcelable(key, value)
//    }
