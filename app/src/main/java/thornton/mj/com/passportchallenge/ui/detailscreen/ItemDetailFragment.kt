package thornton.mj.com.passportchallenge.ui.detailscreen

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v4.app.NavUtils.navigateUpTo
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_item_detail.*
import kotlinx.android.synthetic.main.item_detail.*
import kotlinx.android.synthetic.main.item_detail.view.*
import kotlinx.android.synthetic.main.item_list_content.view.*
import org.jetbrains.anko.support.v4.toast
import thornton.mj.com.passportchallenge.R
import thornton.mj.com.passportchallenge.repo.Profile
import thornton.mj.com.passportchallenge.ui.mainscreen.ItemListActivity

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a [ItemListActivity]
 * in two-pane mode (on tablets) or a [ItemDetailActivity]
 * on handsets.
 */

// TODO: Delete Profiles

class ItemDetailFragment : Fragment(){

    lateinit var profile: Profile
    lateinit var simpleAdapter : SimpleAdapter

    lateinit var dialog : Dialog
    lateinit var database: FirebaseDatabase
    lateinit var myRef: DatabaseReference



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        database = FirebaseDatabase.getInstance()
        myRef = database.getReference()

        arguments?.let {
            if (it.containsKey(ARG_ITEM_ID)) {
                // Load the dummy content specified by the fragment
                // arguments. In a real-world scenario, use a Loader
                // to load content from a content provider.
                val gson: Gson = Gson()
                profile = gson.fromJson(it.getString(ARG_ITEM_PROFILE), Profile::class.java)
                activity?.toolbar_layout?.title = profile.profileName
            }
        }

        dialog = Dialog(context)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.item_detail, container, false)

        profile?.let {
            // Sets background color of Toolbar depending on Gender
            if (profile.gender.equals(resources.getString(R.string.male))) {
                activity?.toolbar_layout!!.setBackgroundColor(resources.getColor(R.color.blue))
            } else {
                activity?.toolbar_layout!!.setBackgroundColor(resources.getColor(R.color.pink))
            }
            rootView.profile_name.text = profile.profileName
            rootView.profile_age.text = profile.age.toString()
            rootView.profile_gender.text = profile.gender
            rootView.profile_id.text = profile.id.toString()

        }

        var addItemBtn = rootView.findViewById<FloatingActionButton>(R.id.addItemBtn)

        var deleteProfileBtn = rootView.findViewById<ImageButton>(R.id.profile_deleteprofile).setOnClickListener {
            showDeleteProfilePopUp()
        }

        rootView.profile_rv.addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
        rootView.profile_rv.layoutManager = LinearLayoutManager(this.context)

        simpleAdapter = SimpleAdapter(profile.hobbies)
        rootView.profile_rv.adapter = simpleAdapter


        val swipeHandler = object : SwipeToDeleteCallback(context!!) {


            override fun onMove(p0: RecyclerView, p1: RecyclerView.ViewHolder, p2: RecyclerView.ViewHolder): Boolean {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                // Don't think I Need to add anything here
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = rootView.profile_rv.adapter as SimpleAdapter
                adapter.removeAt(viewHolder.adapterPosition)

                val al = Profile(profile.dbID, profile.id, profile.profileName, profile.age, profile.gender, adapter.getItems())
                myRef.child(profile.dbID).setValue(al)
            }
        }


        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(rootView.profile_rv)

        addItemBtn.setOnClickListener{
            showPopup()

        }

        return rootView
    }

    fun showPopup() {

        dialog.setContentView(R.layout.fragment_add_hobby)

        dialog.findViewById<Button>(R.id.addhobby_btn).setOnClickListener {
            val hobbyText = dialog.findViewById<TextView>(R.id.addhobby_et).text.toString()

            //TODO: Add text checks to ensure hobby is long enough. Disable submit button if it is not long enough

            if(hobbyText.length > 0) {
                simpleAdapter.addItem(hobbyText)
                simpleAdapter.notifyDataSetChanged()

                dialog.dismiss()

                val al = Profile(profile.dbID, profile.id, profile.profileName, profile.age, profile.gender, simpleAdapter.getItems())
                myRef.child(profile.dbID).setValue(al)

                dialog.dismiss()

            }else{
                Toast.makeText(context, "Text Length Must Be Greater Than 0",Toast.LENGTH_SHORT).show()
            }
        }

        dialog.getWindow().setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    fun showDeleteProfilePopUp(){
        // Late initialize an alert dialog object
        lateinit var dialog:AlertDialog


        // Initialize a new instance of alert dialog builder object
        val builder = AlertDialog.Builder(context)

        // Set a title for alert dialog
        builder.setTitle("Delete User Profile?")

        // Set a message for alert dialog
        builder.setMessage("Are you sure you want to delete this profile?")


        // On click listener for dialog buttons
        val dialogClickListener = DialogInterface.OnClickListener{_,which ->
            when(which){
                DialogInterface.BUTTON_POSITIVE -> {
                    toast("Yes button clicked.")
                    // TODO: Run this in bg thread???

                    println("REMOVE PROFILE: " + profile.dbID)

                    myRef.child(profile.dbID).removeValue()

                    val intent = Intent(context, ItemListActivity::class.java)
                    startActivity(intent)
                    onDestroy()

                }
                DialogInterface.BUTTON_NEGATIVE -> toast("No button clicked.")
                DialogInterface.BUTTON_NEUTRAL -> toast("Cancel button clicked.")
            }
        }


        // Set the alert dialog positive/yes button
        builder.setPositiveButton("YES",dialogClickListener)

        // Set the alert dialog negative/no button
        builder.setNegativeButton("NO",dialogClickListener)

        // Set the alert dialog neutral/cancel button
        builder.setNeutralButton("CANCEL",dialogClickListener)


        // Initialize the AlertDialog using builder object
        dialog = builder.create()

        // Finally, display the alert dialog
        dialog.show()
    }



    companion object {
        /**
         * The fragment argument representing the item ID that this fragment
         * represents.
         */
        const val ARG_ITEM_DBID = "item_dbid"
        const val ARG_ITEM_ID = "item_id"
        const val ARG_ITEM_NAME = "item_name"
        const val ARG_ITEM_AGE = "item_age"
        const val ARG_ITEM_GENDER = "item_gender"
        const val ARG_ITEM_HOBBIES = "item_hobbies"
        const val ARG_ITEM_PROFILE = "item_profile"
    }



    class MyItemRecyclerViewAdapter(
            val values: List<String>,
            private val twoPane : Boolean )
        : RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder>() {


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_list_content, parent, false)

            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = values[position]
            holder.idView.text = item

            with(holder.itemView) {
                tag = item
            }
        }

        override fun getItemCount() = values.size



        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val idView: TextView = view.id_text

        }
    }

}


