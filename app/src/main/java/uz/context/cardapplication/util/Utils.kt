package uz.context.cardapplication.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import uz.context.cardapplication.R
import java.util.*
import kotlin.collections.ArrayList

object Utils {
    fun randomColor(): Int {
        val arrayColor = ArrayList<Int>()
        arrayColor.add(R.color.color1)
        arrayColor.add(R.color.color2)
        arrayColor.add(R.color.color3)
        arrayColor.add(R.color.color5)
        arrayColor.add(R.color.color7)
        arrayColor.add(R.color.color9)

        val random = Random()
        val randomColor = random.nextInt(arrayColor.size)
        return arrayColor[randomColor]
    }

    fun randomString(): String {
        val stringList = ArrayList<String>()
        val random = Random()

        stringList.add("Visa")
        stringList.add("Walmart")
        stringList.add("Bank")
        stringList.add("UzCard")

        return stringList[random.nextInt(stringList.size)]
    }

    fun log(msg: String) {
        Log.d("@@@@@", msg)
    }

    fun checkInternet(context: Context): Boolean {
        val manager = context.getSystemService(AppCompatActivity.CONNECTIVITY_SERVICE) as ConnectivityManager
        val infoMobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
        val infoWifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        return infoMobile!!.isConnected || infoWifi!!.isConnected
    }

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val nw = connectivityManager.activeNetwork ?: return false
            val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
            return when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                //for other device how are able to connect with Ethernet
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                //for check internet over Bluetooth
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
                else -> false
            }
        } else {
            val nwInfo = connectivityManager.activeNetworkInfo ?: return false
            return nwInfo.isConnected
        }
    }
}