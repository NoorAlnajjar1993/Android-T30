package com.dominate.thirtySecondsChallenge.utils

import android.content.Context
import android.os.Environment
import java.io.File

object CommonFile {

    fun getAppPath(context: Context):String{

        val dir = File(Environment.DIRECTORY_DOWNLOADS
        + File.separator
        +File.separator)
        if (!dir.exists())
            dir.mkdir()
        return dir.path+File.separator


    }


}