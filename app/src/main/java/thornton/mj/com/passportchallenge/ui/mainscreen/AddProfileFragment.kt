package thornton.mj.com.passportchallenge.ui.mainscreen

import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_add_hobby.*
import kotlinx.android.synthetic.main.fragment_add_profile.*
import thornton.mj.com.passportchallenge.R
import thornton.mj.com.passportchallenge.repo.Profile

// Acts as DialogFragment to allow the user to create a new profile
// Is called from ItemListActivity

// TODO: Add ability to submit when clicking enter
// TODO: EditText Fields should start with capital letter
class AddProfileFragment : DialogFragment() {

    private var mParam1: String? = null
    private var mParam2: String? = null

    private var mListener: OnFragmentInteractionListener? = null

    private var hobbies = ArrayList<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (getArguments() != null) {
            mParam1 = getArguments()!!.getString(ARG_PARAM1)
            mParam2 = getArguments()!!.getString(ARG_PARAM2)

        }
    }

    // Create the views
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_add_profile, container, false)

        val hobby_et = rootView.findViewById<EditText>(R.id.addprofile_hobbies)


        // Add a hobby to the user's hobbies
        rootView.findViewById<ImageButton>(R.id.addprofile_addhobby_btn).setOnClickListener {
            hobbies.add(hobby_et.text.toString())
            hobby_et.text.clear()
        }


        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference()

        rootView.findViewById<Button>(R.id.addprofile_button).setOnClickListener {


            // Create a profile from the entries. Update the Firebase DB
        val radioGroup = dialog.findViewById<RadioGroup>(R.id.addprofile_gender_rg)

            val selectedId  = radioGroup.checkedRadioButtonId
            val radioButton  = dialog.findViewById<RadioButton>(selectedId)
            val profileName  = dialog.findViewById<EditText>(R.id.addprofile_name).text.toString()
            val age  = dialog.findViewById<EditText>(R.id.addprofile_age).text.toString().toInt()

            val uniqueUserID = (System.currentTimeMillis()/1000).toInt()
            val dbID: String = myRef.push().key!!
            val createdProfile = Profile(dbID, uniqueUserID, profileName, age, radioButton.text.toString(), hobbies)
            myRef.child(dbID).setValue(createdProfile)
            onButtonPressed(createdProfile)

            dismiss()

        }

        return rootView
    }

    // Communicate with ItemListActivity to pass the new profile.
    fun onButtonPressed(profile : Profile) {
        if (mListener != null) {
            mListener!!.onFragmentInteraction(profile)
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }


    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(profile : Profile)
    }

    companion object {
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"
        // The request code must be 0 or greater.
        private val PLUS_ONE_REQUEST_CODE = 0

        fun newInstance(): AddProfileFragment {
            val fragment = AddProfileFragment()
            return fragment
        }
    }

}// Required empty public constructor
