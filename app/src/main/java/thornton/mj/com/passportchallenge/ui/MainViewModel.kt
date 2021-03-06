package thornton.mj.com.passportchallenge.ui


import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.databinding.ObservableField
import thornton.mj.com.passportchallenge.repo.*
import thornton.mj.com.passportchallenge.repo.RepoModel.*
import thornton.mj.com.passportchallenge.ui.mainscreen.FilterState
import thornton.mj.com.passportchallenge.ui.mainscreen.MenuState
import thornton.mj.com.passportchallenge.util.NetManager
import thornton.mj.com.passportchallenge.util.equalsIgnoreCase

import kotlin.collections.ArrayList

// ViewModel for the MVVM pattern
// Acts as the layer to communicate between the Activity and RepoModel
// Lifecycle aware
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
    // TODO: On Network status change, if connected, then ping remote data pull / update

    private var nameSortState : MenuState = MenuState.REMOVED
    private var ageSortState : MenuState = MenuState.REMOVED
    private var filterState : FilterState = FilterState.NONE


    // Update isLoading, set text, and make callback to get profiles

    fun loadProfiles(context : Context){
        isLoading.set(true)
        repoModel.getProfiles(object : OnRepositoryReadyCallback{
            override fun onDataReady(data: ArrayList<Profile>) {
                isLoading.set(false)
                profiles.value = data
                copyOfProfiles.value =  profiles.value

            }
        }, context)
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

            if(filterState.equals(FilterState.NONE)) {
                copyOfProfiles.value = ArrayList(profiles.value!!.filter { it.gender.equals("Male", true) })
                filterState = FilterState.MALE
            }
            else if(filterState.equals(FilterState.MALE)) {
                copyOfProfiles.value = ArrayList(profiles.value!!.filter { it.gender.equals("Female", true) })
                filterState = FilterState.FEMALE
            }else{
                copyOfProfiles.value = profiles.value
                filterState = FilterState.NONE
            }
    }

    fun addProfile(profile : Profile){
        profiles.value!!.add(profile)
        copyOfProfiles.value = profiles.value
    }

    fun removeProfile(profile : Profile){
        profiles.value!!.add(profile)
        copyOfProfiles.value = profiles.value
    }

    // GETTERS / SETTERS
    fun getnameSortState() : MenuState = nameSortState
    fun setnameSortState(state : MenuState) { nameSortState = state }
    fun getageSortState() : MenuState = ageSortState
    fun setageSortState(state : MenuState) { ageSortState = state }


}