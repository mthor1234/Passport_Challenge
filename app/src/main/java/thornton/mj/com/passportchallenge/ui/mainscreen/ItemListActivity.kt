package thornton.mj.com.passportchallenge.ui.mainscreen

import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

import kotlinx.android.synthetic.main.activity_item_list.*
import kotlinx.android.synthetic.main.item_list.*
import thornton.mj.com.passportchallenge.R
import thornton.mj.com.passportchallenge.databinding.ActivityItemListBinding
import thornton.mj.com.passportchallenge.repo.Profile
import thornton.mj.com.passportchallenge.ui.MainViewModel
import thornton.mj.com.passportchallenge.ui.detailscreen.ItemDetailActivity
import thornton.mj.com.passportchallenge.ui.detailscreen.ItemDetailFragment
import android.databinding.adapters.NumberPickerBindingAdapter.setValue
import android.support.v4.app.FragmentActivity
import android.util.Log
import com.google.firebase.database.*
import thornton.mj.com.passportchallenge.BR.profile
import com.google.firebase.database.DataSnapshot




/**
 * An activity representing a list of Pings. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [ItemDetailActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
class ItemListActivity : AppCompatActivity(), RepositoryRecyclerViewAdapter.OnItemClickListener {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */

    //TODO: Load Data on Creation

    private var twoPane: Boolean = false

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

}
