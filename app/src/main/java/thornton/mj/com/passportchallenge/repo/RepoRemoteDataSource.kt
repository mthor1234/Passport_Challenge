package thornton.mj.com.passportchallenge.repo

import android.net.Uri
import android.os.Handler
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.collections.ArrayList

// Holds the Remote Data
// Uses Firebase Realtime Database
class RepoRemoteDataSource() {

    //TODO: Create singleton database so there is only one instance
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference()
    var data = ArrayList<Profile>()


    // Gets profiles from FB Backend
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


    // Set data into the FB backened
    fun setData(dataSnapshot: DataSnapshot): ArrayList<Profile> {
        val profiles = ArrayList<Profile>()

        for (snapshot in dataSnapshot.children) {

            var userProfile = Profile(
                    snapshot.child("dbID").value.toString(),
                    snapshot.child("id").value.toString().toInt(),
                    snapshot.child("profileName").value.toString(),
                    snapshot.child("age").value.toString().toInt(),
                    snapshot.child("gender").value.toString(),
                    createHobbieList(snapshot)
            )

            profiles.add(userProfile)
        }
        return profiles
    }

    // Needed to convert "Hobbies" saved in FB backend from Strings -> ArrayList
    fun createHobbieList(snapshot: DataSnapshot): ArrayList<String> {
        val list = ArrayList<String>()
        for (item in snapshot.child("hobbies").children) {
            list.add(item.value.toString())
        }
        return list
    }


    interface OnRepoRemoteReadyCallback {
        fun onRemoteDataReady(data: ArrayList<Profile>)
    }
}