package thornton.mj.com.passportchallenge.ui


import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableField
import com.google.firebase.database.FirebaseDatabase
import thornton.mj.com.passportchallenge.repo.*
import thornton.mj.com.passportchallenge.repo.RepoModel.*
import thornton.mj.com.passportchallenge.util.NetManager

import java.util.*

class MainViewModel : AndroidViewModel {

    constructor(application: Application) : super(application)

   var repoModel : RepoModel = RepoModel(NetManager(getApplication()))

    var text = ObservableField("old data")

    // Used to display Progress Dialog
    var isLoading = ObservableField(false)

    var profiles = MutableLiveData<ArrayList<Profile>>()

    // Callback to communicate with Repo
//    val onDataReadyCallback = object : OnDataReadyCallback{
//        override fun onDataReady(data: String) {
//            isLoading = false
//            text = data
//        }
//    }

    // Update isLoading, set text, and make callback to get profiles

    fun loadProfiles(){
        isLoading.set(true)
        repoModel.getProfiles(object : OnRepositoryReadyCallback{
            override fun onDataReady(data: ArrayList<Profile>) {
                isLoading.set(false)
                profiles.value = data
            }
        })
    }

}