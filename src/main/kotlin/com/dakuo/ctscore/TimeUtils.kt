package com.dakuo.ctscore

import java.text.SimpleDateFormat
import java.util.Date

object TimeUtils {

    fun formatDate(date: Date):String{
        return SimpleDateFormat("yyyy-MM-dd HH:mm:dd").format(date)
    }

}