package thornton.mj.com.passportchallenge.ui


import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import thornton.mj.com.passportchallenge.repo.*
import thornton.mj.com.passportchallenge.repo.RepoModel.*

import java.util.*

class MainViewModel : ViewModel() {
   var repoModel : RepoModel = RepoModel()

    var text = ObservableField<String>()

    // Used to display Progress Dialog
    var isLoading = ObservableField<Boolean>()

    // Callback to communicate with Repo
//    val onDataReadyCallback = object : OnDataReadyCallback{
//        override fun onDataReady(data: String) {
//            isLoading = false
//            text = data
//        }
//    }

    // Refresh data is called, update isLoading, set text, and make callback
    fun refresh(){
        isLoading.set(true)
        repoModel.refreshData(object : OnDataReadyCallback {
            override fun onDataReady(data: String) {
                isLoading.set(false)
                text.set(data)
            }
        })
    }


}