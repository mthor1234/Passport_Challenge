package thornton.mj.com.passportchallenge.ui.detailscreen

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import thornton.mj.com.passportchallenge.dummy.DummyContent
import kotlinx.android.synthetic.main.activity_item_detail.*
import kotlinx.android.synthetic.main.item_detail.view.*
import thornton.mj.com.passportchallenge.BR.profile
import thornton.mj.com.passportchallenge.R
import thornton.mj.com.passportchallenge.repo.Profile

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a [ItemListActivity]
 * in two-pane mode (on tablets) or a [ItemDetailActivity]
 * on handsets.
 */
class ItemDetailFragment : Fragment() {

    /**
     * The dummy content this fragment is presenting.
     */
    lateinit var profile: Profile

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            if (it.containsKey(ARG_ITEM_ID)) {
                // Load the dummy content specified by the fragment
                // arguments. In a real-world scenario, use a Loader
                // to load content from a content provider.
//                item = View.ITEM_MAP[it.getString(ARG_ITEM_ID)]

                val gson : Gson = Gson()
                profile = gson.fromJson(it.getString(ARG_ITEM_PROFILE), Profile::class.java)


                activity?.toolbar_layout?.title = profile.profileName
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.item_detail, container, false)

        // TODO: Change Hobbies to a RecyclerView so it can be displayed and edited easier

        profile?.let {
            if(profile.gender.equals(resources.getString(R.string.male))) {
                        activity?.toolbar_layout!!.setBackgroundColor(resources.getColor(R.color.blue))
                    }else {
                activity?.toolbar_layout!!.setBackgroundColor(resources.getColor(R.color.pink))

            }
                        rootView.profile_name.text = profile.profileName
                        rootView.profile_age.text = profile.age.toString()
                        rootView.profile_gender.text = profile.gender
                        rootView.profile_hobbies.setText(profile.getStringOfHobbies())
                    }
        return rootView
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
}
