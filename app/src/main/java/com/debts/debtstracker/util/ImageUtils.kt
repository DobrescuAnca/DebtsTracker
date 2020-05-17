package com.debts.debtstracker.util

import android.graphics.Bitmap
import java.net.URL


fun getBitmapFromUrl(url: String): Bitmap?{
    val newUrl = URL(url)
    var result: Bitmap? = null
//    withContext(Dispatchers.IO){
//        result =  try {
//            BitmapFactory.decodeStream(newUrl.openConnection().getInputStream())
//        } catch (e: Exception){
//            null
//        }
//    }
    return result
}