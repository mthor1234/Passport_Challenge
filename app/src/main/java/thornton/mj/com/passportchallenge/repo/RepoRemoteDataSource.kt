package thornton.mj.com.passportchallenge.repo

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.ArrayList

// Holds the Local Data
class RepoRemoteDataSource(){

    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference()

    var data = ArrayList<Profile>()


    fun getProfiles(onRepositoryReadyCallback : OnRepoRemoteReadyCallback){
        var arrayList = ArrayList<Profile>()
//        arrayList.add(Profile(1, "MJ Thornton From Local", 26, Gender.MALE, arrayListOf("Boxing", "Jiu-Jitsu", "Android, Football")))
//        arrayList.add(Profile(2, "MJ Thornton From Local", 26, Gender.MALE, arrayListOf("Boxing", "Jiu-Jitsu", "Android, Snowboarding")))
//        arrayList.add(Profile(3, "MJ Thornton From Local", 26, Gender.FEMALE, arrayListOf("Boxing", "Jiu-Jitsu", "Android, MMA")))
//        arrayList.add(Profile(4, "MJ Thornton From Local", 26, Gender.FEMALE, arrayListOf("Boxing", "Jiu-Jitsu", "Android")))
//        arrayList.add(Profile(5, "MJ Thornton From Local", 26, Gender.MALE, arrayListOf("Boxing", "Jiu-Jitsu", "Android")))
//        arrayList.add(Profile(6, "MJ Thornton From Local", 26, Gender.MALE, arrayListOf("Boxing", "Jiu-Jitsu", "Android")))
//        arrayList.add(Profile(7, "MJ Thornton From Local", 26, Gender.MALE, arrayListOf("Boxing", "Jiu-Jitsu", "Android")))

        // Read from the database
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                println("OnDataChanged")

                // This method is called once with the initial value and again
                // whenever data at this location is updated.
//                val value = dataSnapshot.getValue(String::class.java)
//                Log.d("ItemListActivity", "Value is: " + value!!)

//                    showData(dataSnapshot)
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


    fun setData(dataSnapshot: DataSnapshot) : ArrayList<Profile>{

        val list = ArrayList<String>()
        val profiles = ArrayList<Profile>()
        list.add("Test")

        for (snapshot in dataSnapshot.children){


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

    fun createHobbieList(snapshot: DataSnapshot) : ArrayList<String>{
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