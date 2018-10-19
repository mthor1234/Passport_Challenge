package thornton.mj.com.passportchallenge.ui.detailscreen

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import com.google.gson.Gson
import thornton.mj.com.passportchallenge.dummy.DummyContent
import kotlinx.android.synthetic.main.activity_item_detail.*
import kotlinx.android.synthetic.main.item_detail.*
import kotlinx.android.synthetic.main.item_detail.view.*
import kotlinx.android.synthetic.main.item_list_content.view.*
import thornton.mj.com.passportchallenge.BR.profile
import thornton.mj.com.passportchallenge.R
import thornton.mj.com.passportchallenge.repo.Profile
import thornton.mj.com.passportchallenge.ui.mainscreen.RepositoryRecyclerViewAdapter

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a [ItemListActivity]
 * in two-pane mode (on tablets) or a [ItemDetailActivity]
 * on handsets.
 */

// TODO: Change Hobbies to a RecyclerView so it can be displayed and edited easier
// TODO: Add editing to RecyclerView

class ItemDetailFragment : Fragment(), AdapterView.OnItemClickListener {

    lateinit var profile: Profile



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            if (it.containsKey(ARG_ITEM_ID)) {
                // Load the dummy content specified by the fragment
                // arguments. In a real-world scenario, use a Loader
                // to load content from a content provider.
//                item = View.ITEM_MAP[it.getString(ARG_ITEM_ID)]

                val gson: Gson = Gson()
                profile = gson.fromJson(it.getString(ARG_ITEM_PROFILE), Profile::class.java)

                activity?.toolbar_layout?.title = profile.profileName
            }
        }
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

            rootView.profile_rv.layoutManager = LinearLayoutManager(activity)
            rootView.profile_rv.adapter = MyItemRecyclerViewAdapter(profile.hobbies, this, false)


        }
        return rootView
    }

    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    companion object {
        /**
         * The fragment argument representing the item ID that this fragment
         * represents.
         */
        const val ARG_ITEM_ID = "item_id"
        const val ARG_ITEM_NAME = "item_name"
        const val ARG_ITEM_AGE = "item_age"
        const val ARG_ITEM_GENDER = "item_gender"
        const val ARG_ITEM_HOBBIES = "item_hobbies"
        const val ARG_ITEM_PROFILE = "item_profile"
    }


//    private fun setupRecyclerView(recyclerView: RecyclerView) {
//        recyclerView.adapter = MyItemRecyclerViewAdapter(profile.hobbies, this, false)
//    }

    class MyItemRecyclerViewAdapter(
            private val values: List<String>,
            private val mListener: AdapterView.OnItemClickListener,
            private val twoPane : Boolean )
        : RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder>() {


        private val onClickListener: View.OnClickListener

        init {
            onClickListener = View.OnClickListener {
                println("Item Clicked!")
//                v ->
//                val item = v.tag as DummyContent.DummyItem
//                if (twoPane) {
//                    val fragment = ItemDetailFragment().apply {
//                        arguments = Bundle().apply {
//                            putString(ItemDetailFragment.ARG_ITEM_ID, item.id)
//                        }
//                    }
//                    supportFragmentManager
//                            .beginTransaction()
//                            .replace(R.id.item_detail_container, fragment)
//                            .commit()
//                } else {
//                    val intent = Intent(v.context, ItemDetailActivity::class.java).apply {
//                        putExtra(ItemDetailFragment.ARG_ITEM_ID, item.id)
//                    }
//                    v.context.startActivity(intent)
//                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_list_content, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = values[position]
            holder.idView.text = item
//            holder.contentView.text = item.content

            with(holder.itemView) {
                tag = item
                setOnClickListener(onClickListener)
            }
        }

        override fun getItemCount() = values.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val idView: TextView = view.id_text
//            val contentView: TextView = view.content
        }

    }
}


