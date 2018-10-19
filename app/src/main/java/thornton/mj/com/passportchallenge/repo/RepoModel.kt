package thornton.mj.com.passportchallenge.repo
import android.os.Handler

class RepoModel {

//    private val localDataSource = RepoLocalDataSource()

//    fun getProfiles() : Observable<ArrayList<Profile>> {
//
//        return localDataSource.getProfiles()
//    }

    // Refresh data with a 2 second delay to simiulate networking
    fun refreshData(onDataReadyCallBack : OnDataReadyCallback){
        Handler().postDelayed({ onDataReadyCallBack.onDataReady("new data")} , 2000)
    }

    // Interface used for communication between VM
    interface OnDataReadyCallback {
        fun onDataReady(data : String)
    }

}