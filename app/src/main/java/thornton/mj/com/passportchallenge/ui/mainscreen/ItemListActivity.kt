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
import android.os.Handler
import android.support.v4.app.FragmentManager
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import com.google.firebase.database.FirebaseDatabase
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import thornton.mj.com.passportchallenge.repo.Gender
import thornton.mj.com.passportchallenge.repo.room.AppDatabase
import thornton.mj.com.passportchallenge.repo.room.ProfileDao


/**
 * An activity representing a list of Pings. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [ItemDetailActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */

// Acts as the "Main" activity. Holds the list of profiles. Elected to use a Master/Detail layout so tablets will benefit but I did not have the time
// To build out the tablet functionality

class ItemListActivity : AppCompatActivity(), RepositoryRecyclerViewAdapter.OnItemClickListener, AddProfileFragment.OnFragmentInteractionListener {
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */

    //TODO: Move Remove and Add Proile to RepoModel
    //TODO: Store / Retrieve Photos from Firebase
    //TODO: If not connected to internet and local db is updated, need to update the FB backened

    private var twoPane: Boolean = false
    lateinit var dialog : Dialog

    // Create binding
    lateinit var binding : ActivityItemListBinding
    lateinit var viewModel : MainViewModel
    lateinit var menu : Menu

    private val repositoryRecyclerViewAdapter = RepositoryRecyclerViewAdapter(arrayListOf(), this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_item_list)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        binding.viewModel = viewModel
        binding.executePendingBindings()

        dialog = Dialog(this)

        binding.repositoryRv.layoutManager = LinearLayoutManager(this)
        binding.repositoryRv.adapter = repositoryRecyclerViewAdapter

        viewModel.copyOfProfiles.observe(this,
                Observer<ArrayList<Profile>> { it?.let{repositoryRecyclerViewAdapter.replaceData(it)} })

        setSupportActionBar(toolbar)
        toolbar.title = title


        if (item_detail_container != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            twoPane = true
        }

    }

    // Load profiles from ViewModel -> RepoModel -> RemoteRepo/LocalRepo (Depending on connection)
    override fun onStart() {
        super.onResume()
        viewModel.loadProfiles(this)
    }

    override fun onItemClick(position: Int, profile: Profile) {
        if (twoPane) {
            val fragment = ItemDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ItemDetailFragment.ARG_ITEM_DBID, profile.dbID)
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
                val profileJson = Gson().toJson(profile)
                putExtra(ItemDetailFragment.ARG_ITEM_PROFILE, profileJson)

            }
            startActivity(intent)
        }
    }

    // Display popup to add a new profile
    fun showPopup() {
        val ft = supportFragmentManager.beginTransaction()
        val prev = supportFragmentManager.findFragmentByTag("dialog")

        if (prev != null) {
            ft.remove(prev)
        }
        supportFragmentManager.beginTransaction().addToBackStack(null)

        // Create and show the dialog.
        val newFragment = AddProfileFragment.newInstance()
        newFragment.show(ft, "dialog")

        }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        this.menu = menu!!
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }


    // Handles sorting, filtering, and adding new profiles
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id : Int = item!!.itemId

        when(id) {
            R.id.menu_sort_by_name -> {
                menu.findItem(R.id.menu_sort_by_age).setIcon(null)

                if (viewModel.getnameSortState().equals(MenuState.REMOVED)) {
                    viewModel.setnameSortState(MenuState.ASCENDING)
                    viewModel.sortProfiles("NAME", MenuState.ASCENDING)
                    item.setIcon(getDrawable(R.drawable.ic_arrow_downward_white_24dp))
                } else if (viewModel.getnameSortState().equals(MenuState.ASCENDING)) {
                    viewModel.setnameSortState(MenuState.DESCENDING)
                    viewModel.sortProfiles("NAME", MenuState.DESCENDING)
                    item.setIcon(getDrawable(R.drawable.ic_arrow_upward_white_24dp))
                } else {
                    viewModel.setnameSortState(MenuState.REMOVED)
                    viewModel.sortProfiles("NAME", MenuState.REMOVED)
                    item.setIcon(null)
                }
            }


            R.id.menu_sort_by_age -> {
                menu.findItem(R.id.menu_sort_by_name).setIcon(null)

                if (viewModel.getageSortState().equals(MenuState.REMOVED)) {
                    viewModel.setageSortState(MenuState.ASCENDING)
                    viewModel.sortProfiles("AGE", MenuState.ASCENDING)
                    item.setIcon(getDrawable(R.drawable.ic_arrow_downward_white_24dp))

                } else if (viewModel.getageSortState().equals(MenuState.ASCENDING)) {
                    viewModel.setageSortState(MenuState.DESCENDING)
                    viewModel.sortProfiles("AGE", MenuState.DESCENDING)
                    item.setIcon(getDrawable(R.drawable.ic_arrow_upward_white_24dp))
                } else {
                    viewModel.setageSortState(MenuState.REMOVED)
                    viewModel.sortProfiles("AGE", MenuState.REMOVED)
                    item.setIcon(null)
                }
            }

            R.id.menu_filter -> {
                viewModel.filterProfiles()
            }


            R.id.menu_add -> {
                showPopup()
            }

        }
        repositoryRecyclerViewAdapter.notifyDataSetChanged()
        return super.onOptionsItemSelected(item)
    }


    override fun onFragmentInteraction(profile : Profile) {
        viewModel.addProfile(profile)
        viewModel.loadProfiles(this)

    }


}
