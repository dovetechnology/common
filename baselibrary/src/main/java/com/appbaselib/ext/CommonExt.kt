package com.appbaselib.ext


import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.view.WindowManager
import android.widget.Toast
import com.appbaselib.app.BaseApplication
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import java.io.Serializable
import java.util.ArrayList


/** toast相关 **/
fun Any.toast(msg: CharSequence?) {
    Toast.makeText(BaseApplication.instance, msg, Toast.LENGTH_SHORT).show()
}

fun Any.longToast(msg: CharSequence) {
    Toast.makeText(BaseApplication.instance, msg, Toast.LENGTH_LONG).show()
}

/** json相关 **/
fun Any.toJson() = Gson().toJson(this)

fun <T> String.toList(cls:Class<T>): List<T> {

    val list = ArrayList<T>()
    try {
        val gson = Gson()
        val arry = JsonParser().parse(this).asJsonArray
        for (jsonElement in arry) {
            list.add(gson.fromJson(jsonElement, cls))
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return list

}
