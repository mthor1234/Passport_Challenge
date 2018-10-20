package thornton.mj.com.passportchallenge.repo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ViewDatabase : AppCompatActivity(){

    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference()

    var data = ArrayList<Profile>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

            // Read from the database
            myRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
//                val value = dataSnapshot.getValue(String::class.java)
//                Log.d("ItemListActivity", "Value is: " + value!!)

//                    showData(dataSnapshot)
                    data = setData(dataSnapshot)

                }

                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value
                    Log.w("ItemListActivity", "Failed to read value.", error.toException())
                }
            })

        }
    fun showData(dataSnapshot: DataSnapshot){
        println("Data : " + dataSnapshot.child("0").child("ProfileName").getValue())
        println("Data : " + dataSnapshot.child("0").child("Age").getValue())
        println("Data : " + dataSnapshot.child("0").child("Gender").getValue())
        println("Data : " + dataSnapshot.child("0").child("Hobbies").getValue())

        val list = ArrayList<String>()

        for (snapshot in dataSnapshot.child("0").child("Hobbies").children) {
            list.add(snapshot.value.toString())
        }

    }

    fun setData(dataSnapshot: DataSnapshot) : ArrayList<Profile>{

        val list = ArrayList<String>()
        val profiles = ArrayList<Profile>()
        list.add("Test")

        for (snapshot in dataSnapshot.children){
            var userProfile = Profile(
                    snapshot.value.toString().toInt(),
                    snapshot.child("ProfileName").value.toString(),
                    snapshot.child("Age").value.toString().toInt(),
                    snapshot.child("Gender").value.toString(),
                    list)

            profiles.add(userProfile)
            //childToArrayList(snapshot.child("Hobbies").children)
        }
        return profiles
    }

//    fun childToArrayList(hobbies : Iterable<DataSnapshot>){
//

//        for (snapshot in dataSnapshot.child("0").child("Hobbies").children) {
//            list.add(snapshot.value.toString())
//        }

//        child("0").child("Hobbies").children) {
//            list.add(snapshot.value.toString())
//        }
//
//        for(item in hobbies) {
//            list.add(item.value.toString())
//            print("ITEM: ${item.value.toString()}")
//        }
//
//
//    }
}