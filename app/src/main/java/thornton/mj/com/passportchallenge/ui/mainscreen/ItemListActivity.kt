package thornton.mj.com.passportchallenge.ui.mainscreen

import android.app.Dialog
import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.net.Uri
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_item_list.*
import kotlinx.android.synthetic.main.item_list.*
import thornton.mj.com.passportchallenge.R
import thornton.mj.com.passportchallenge.databinding.ActivityItemListBinding
import thornton.mj.com.passportchallenge.repo.Profile
import thornton.mj.com.passportchallenge.ui.MainViewModel
import thornton.mj.com.passportchallenge.ui.detailscreen.ItemDetailActivity
import thornton.mj.com.passportchallenge.ui.detailscreen.ItemDetailFragment
import android.view.Menu
import android.view.MenuItem
import android.graphics.drawable.ColorDrawable
import android.graphics.Color
import android.view.View
import android.widget.Button
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_add_profile.*




/**
 * An activity representing a list of Pings. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [ItemDetailActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
class ItemListActivity : AppCompatActivity(), RepositoryRecyclerViewAdapter.OnItemClickListener, AddProfileFragment.OnFragmentInteractionListener {
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */

    //TODO: Load Data on Creation

    private var twoPane: Boolean = false

    lateinit var dialog : Dialog

    // Create binding
    lateinit var binding : ActivityItemListBinding
    private val repositoryRecyclerViewAdapter = RepositoryRecyclerViewAdapter(arrayListOf(), this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_item_list)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_item_list)
        val viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        binding.viewModel = viewModel
        binding.executePendingBindings()

        dialog = Dialog(this)


        binding.repositoryRv.layoutManager = LinearLayoutManager(this)
        binding.repositoryRv.adapter = repositoryRecyclerViewAdapter
        viewModel.profiles.observe(this,
                Observer<ArrayList<Profile>> { it?.let{repositoryRecyclerViewAdapter.replaceData(it)} })

// Write a message to the database

        setSupportActionBar(toolbar)
        toolbar.title = title


//        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show()
//        }

        if (item_detail_container != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            twoPane = true
        }

    }

    override fun onItemClick(position: Int, profile: Profile) {

//        val item = v.tag as profile
        if (twoPane) {
            val fragment = ItemDetailFragment().apply {
                arguments = Bundle().apply {
                    putInt(ItemDetailFragment.ARG_ITEM_ID, profile.id)
                    putString(ItemDetailFragment.ARG_ITEM_NAME, profile.profileName)
                    putInt(ItemDetailFragment.ARG_ITEM_AGE, profile.age)
                    putString(ItemDetailFragment.ARG_ITEM_GENDER, profile.gender)
                    putString(ItemDetailFragment.ARG_ITEM_HOBBIES, profile.getStringOfHobbies())
                }
            }
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.item_detail_container, fragment)
                    .commit()
        } else {
            val intent = Intent(this, ItemDetailActivity::class.java).apply {
                putExtra(ItemDetailFragment.ARG_ITEM_ID, profile.id)

//                val gson = Gson()
//                val type = object : TypeToken<List<Student>>() {
//                }.getType()

                val profileJson = Gson().toJson(profile)

                println("Profile: $profileJson ")

//                val json = gson.toJson(students, type)
//                val intent = Intent(baseContext, YourActivity::class.java)
                putExtra(ItemDetailFragment.ARG_ITEM_PROFILE, profileJson)

            }
            startActivity(intent)
        }

    }

//    fun showDialog() {
//        val newFragment : Fragment = AddProfileFragment.newInstance("test", "test")
//        supportFragmentManager.beginTransaction().add(R.id.overlay_fragment_container, newFragment).commit()
//    }

    fun showPopup() {
//        val txtclose: TextView
//        val btnFollow: Button

        dialog.setContentView(R.layout.fragment_add_profile)

        dialog.findViewById<Button>(R.id.addprofile_button).setOnClickListener {
            val database = FirebaseDatabase.getInstance()
                val myRef = database.getReference()

             val userId : String = myRef.push().key!!

            val list : ArrayList<String> = arrayListOf("Football", "Medicine", "Sleeping")

            val al = Profile(4, "Alex Ward", 26, "Male", list)
//                users.put("5", Profile(5, "Lauren M", 27, "Female", list))

                dialog.dismiss()
                myRef.child(userId).setValue(al)
        }

//        txtclose = myDialog.findViewById(R.id.txtclose)
//        txtclose.text = "M"
//        btnFollow = myDialog.findViewById(R.id.btnfollow) as Button

//        addprofile_name.setText("Testing")

//        addprofile_button.setOnClickListener(View.OnClickListener() {
//            println("Add Profile")
//        })

//        addprofile_button.setOnClickListener {
//                val database = FirebaseDatabase.getInstance()
//                val myRef = database.getReference()
//                val users = HashMap<String, Profile>()
//
//
//                users.put("4", Profile(4, "Alex Ward", 26, "Male", listOf("Football", "Medicine", "Sleeping") as ArrayList<String>))
//                users.put("5", Profile(5, "Lauren M", 27, "Female", listOf("Real Estate", "West Hartford", "Vodka") as ArrayList<String>))
//
//                myDialog.dismiss()
//                myRef.setValue(users)
//            }


        dialog.getWindow().setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id : Int = item!!.itemId


        when(id){
            R.id.menu_sort ->  {
                println("Sort")
//                var sortedListByName = DummyContent.ITEMS.sortedWith(compareBy { it.age })
            }
            R.id.menu_filter ->  println("Filter")
            R.id.menu_add ->  {
                println("Add Profile")
                showPopup()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        println("Add Profile Interaction")
    }

}
