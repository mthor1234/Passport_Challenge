package thornton.mj.com.passportchallenge.ui


import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableField
import thornton.mj.com.passportchallenge.repo.*
import thornton.mj.com.passportchallenge.repo.RepoModel.*
import thornton.mj.com.passportchallenge.ui.mainscreen.MenuState
import thornton.mj.com.passportchallenge.util.NetManager

import kotlin.collections.ArrayList

class MainViewModel : AndroidViewModel {

    constructor(application: Application) : super(application)

   var repoModel : RepoModel = RepoModel(NetManager(getApplication()))

    var text = ObservableField("old data")

    // Used to display Progress Dialog
    var isLoading = ObservableField(false)

    var profiles = MutableLiveData<ArrayList<Profile>>()

    private var nameSortState : MenuState = MenuState.REMOVED
    private var ageSortState : MenuState = MenuState.REMOVED



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

// Sort the profiles based on user selection
    fun sortProfiles(sortBy : String , modifier : MenuState){

    lateinit var sortedList : List<Profile>

    // TODO: Need to make it so the sorts are exclusive. You shouldn't be able to sort by Name and Age
        when(sortBy){
            // SORT BY AGE
            "AGE" -> {
                println("SORT BY: AGE!")
                // SORT ASCENDING, DESCENDING, DEFAULT
                if (modifier.equals(MenuState.ASCENDING)) {
                    profiles.value!!.sortBy { it.age }
                    for (obj in profiles.value!!) {
                        println(obj.printProfile())
                    }
                }
                else if( modifier.equals(MenuState.DESCENDING)){
                    profiles.value!!.sortByDescending{it.age }
                }
                else{
                    profiles.value!!.sortBy{it.id}
                }
            }
            "NAME" ->{
                println("SORT BY: NAME!")
                // SORT ASCENDING, DESCENDING, DEFAULT
                if (modifier.equals(MenuState.ASCENDING)) {
                    profiles.value!!.sortBy { it.profileName }
                    for (obj in profiles.value!!) {
                        println(obj.printProfile())
                    }
                }
                else if( modifier.equals(MenuState.DESCENDING)){
                    profiles.value!!.sortByDescending{it.profileName }
                }
                else{
                    profiles.value!!.sortBy{it.id}
                }
            }
            else ->{
                 println("SORT BY: NOTHING!")
                profiles.value!!.sortBy{it.id}
            }
        }

//        when(modifier){
//            "ASCENDING" ->{
//
//            }
//            "ASCENDING" ->{
//
//            }
//            else ->{
//
//            }
//        }


//    profiles = sortedList as MutableLiveData<ArrayList<Profile>>
    }

    // GETTERS / SETTERS
    fun getnameSortState() : MenuState = nameSortState
    fun setnameSortState(state : MenuState) { nameSortState = state }
    fun getageSortState() : MenuState = ageSortState
    fun setageSortState(state : MenuState) { ageSortState = state }


}