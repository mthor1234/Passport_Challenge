package thornton.mj.com.passportchallenge.ui


import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableField
import thornton.mj.com.passportchallenge.repo.*
import thornton.mj.com.passportchallenge.repo.RepoModel.*
import thornton.mj.com.passportchallenge.ui.mainscreen.FilterState
import thornton.mj.com.passportchallenge.ui.mainscreen.MenuState
import thornton.mj.com.passportchallenge.util.NetManager
import thornton.mj.com.passportchallenge.util.equalsIgnoreCase

import kotlin.collections.ArrayList

class MainViewModel : AndroidViewModel {

    constructor(application: Application) : super(application)

   var repoModel : RepoModel = RepoModel(NetManager(getApplication()))

    var text = ObservableField("old data")

    // Used to display Progress Dialog
    var isLoading = ObservableField(false)

    var profiles = MutableLiveData<ArrayList<Profile>>()

    // Used so data can be filtered and sorted without doing numerous database calls
    var copyOfProfiles = MutableLiveData<ArrayList<Profile>>()


    // TODO: Change SORT ENUM to have NameSortAscending, NameSortDescending, AGESORTASCENDING, AGESORTDESCENDING, NONE. Then create a toggle() method that will toggle between these

    private var nameSortState : MenuState = MenuState.REMOVED
    private var ageSortState : MenuState = MenuState.REMOVED
    private var filterState : FilterState = FilterState.NONE


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

                // Make a copy of profiles
//                copyOfProfiles.value = data

               // profiles.value!!.sortBy{it.id}
                copyOfProfiles.value =  profiles.value


                copyOfProfiles.value!!.forEach {
                    println("Copy of Profiles: " + it.hobbies)
                }
            }
        })
    }

// Sort the profiles based on user selection
    fun sortProfiles(sortBy : String , modifier : MenuState){
        when(sortBy){
            // SORT BY AGE
            "AGE" -> {
                println("SORT BY: AGE!")
                // SORT ASCENDING, DESCENDING, DEFAULT
                if (modifier.equals(MenuState.ASCENDING)) {
                    copyOfProfiles.value!!.sortBy { it.age }
//                    for (obj in profiles.value!!) {
//                        println(obj.printProfile())
//                    }
                }
                else if( modifier.equals(MenuState.DESCENDING)){
                    copyOfProfiles.value!!.sortByDescending{it.age }
                }
                else{
                    copyOfProfiles.value!!.sortBy{it.id}
                }
            }
            "NAME" ->{
                println("SORT BY: NAME!")
                // SORT ASCENDING, DESCENDING, DEFAULT
                if (modifier.equals(MenuState.ASCENDING)) {
                    copyOfProfiles.value!!.sortBy { it.profileName }
//                    for (obj in profiles.value!!) {
//                        println(obj.printProfile())
//                    }
                }
                else if( modifier.equals(MenuState.DESCENDING)){
                    copyOfProfiles.value!!.sortByDescending{it.profileName }
                }
                else{
                    copyOfProfiles.value!!.sortBy{it.id}
                }
            }
            else ->{
                 println("SORT BY: NOTHING!")
                copyOfProfiles.value!!.sortBy{it.id}
            }
        }

    }

    // Filter results by Gender
    // TODO: NEED TO KEEP ON THE FILTER / SORT if a SORT / FILTER IS CALLED ON THE LIST. BY  SETTING CopyOfProfiles = Profiles, you lose the filters / sorts
    fun filterProfiles(){

            if(filterState.equals(FilterState.MALE)) {
                copyOfProfiles.value = ArrayList(profiles.value!!.filter { it.gender.equals("Male", true) })
                filterState = FilterState.FEMALE
            }
            else if(filterState.equals(FilterState.FEMALE)) {
                copyOfProfiles.value = ArrayList(profiles.value!!.filter { it.gender.equals("Female", true) })
                filterState = FilterState.NONE
            }else{
                copyOfProfiles.value = ArrayList(profiles.value!!.filter { it.gender.equals("Female", true) })
                filterState = FilterState.MALE
            }

//       profiles.value.forEach {
//            println("FILTERED: ${it.printProfile()}")
//        }

    }

    // GETTERS / SETTERS
    fun getnameSortState() : MenuState = nameSortState
    fun setnameSortState(state : MenuState) { nameSortState = state }
    fun getageSortState() : MenuState = ageSortState
    fun setageSortState(state : MenuState) { ageSortState = state }


}