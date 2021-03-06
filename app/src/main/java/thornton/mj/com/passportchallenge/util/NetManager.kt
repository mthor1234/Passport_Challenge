package thornton.mj.com.passportchallenge.util

import android.content.Context
import android.net.ConnectivityManager

// Used to get status of internet connection
class NetManager (var applicationContext: Context) {
    private var status: Boolean? = false

    val isConnectedToInternet: Boolean?
        get() {
            val conManager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val ni = conManager.activeNetworkInfo
            return ni != null && ni.isConnected
        }
}