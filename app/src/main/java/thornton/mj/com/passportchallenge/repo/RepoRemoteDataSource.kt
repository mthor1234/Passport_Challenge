package thornton.mj.com.passportchallenge.repo

import android.net.Uri
import android.os.Handler
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.collections.ArrayList

// Holds the Local Data
class RepoRemoteDataSource() {

    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference()

    var data = ArrayList<Profile>()


    fun getProfiles(onRepositoryReadyCallback: OnRepoRemoteReadyCallback) {
        var arrayList = ArrayList<Profile>()


        // Read from the database
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                println("OnDataChanged")
                arrayList = setData(dataSnapshot)

            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("ItemListActivity", "Failed to read value.", error.toException())
            }
        })


        Handler().postDelayed({ onRepositoryReadyCallback.onRemoteDataReady(arrayList) }, 2000)
    }

//        fun saveProfiles(arrayList: ArrayList<Profile>) : Completable {
//            //The Completable class represents a deferred computation without any value but only indication for completion or exception.
//            return Single.just(1).delay(1,TimeUnit.SECONDS).toCompletable()
//        }


    fun setData(dataSnapshot: DataSnapshot): ArrayList<Profile> {

        val list = ArrayList<String>()
        val profiles = ArrayList<Profile>()
        list.add("Test")

        for (snapshot in dataSnapshot.children) {


            var userProfile = Profile(
            snapshot.child("ID").value.toString().toInt(),
                    snapshot.child("ProfileName").value.toString(),
                    snapshot.child("Age").value.toString().toInt(),
                    snapshot.child("Gender").value.toString(),
                    createHobbieList(snapshot)
            )

            profiles.add(userProfile)
            //childToArrayList(snapshot.child("Hobbies").children)
        }
        return profiles
    }

    fun createHobbieList(snapshot: DataSnapshot): ArrayList<String> {
        val list = ArrayList<String>()
        for (item in snapshot.child("Hobbies").children) {

            println("Item: " + item.value.toString())

            list.add(item.value.toString())
        }
        return list
    }


    interface OnRepoRemoteReadyCallback {
        fun onRemoteDataReady(data: ArrayList<Profile>)
    }


}