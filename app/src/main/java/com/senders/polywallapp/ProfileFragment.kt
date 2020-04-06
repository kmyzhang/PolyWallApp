package com.senders.polywallapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.profile.*
import java.lang.StringBuilder


class ProfileFragment: Fragment() {

    private var TAG: String = "ProfileFragment"
    private lateinit var viewModel: UserViewModel
    private lateinit var nameTxt: TextView
    private lateinit var emailTxt: TextView
    private lateinit var logoutBtn: Button

    private var mToolBar : Toolbar? = null

    companion object {
        private const val ARG_COUNT = "arg_count"
        fun newInstance(count: Int) = ProfileFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_COUNT, count)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View {
        viewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
        //viewModel.setup()
        val view = inflater.inflate(R.layout.profile, container, false)
        nameTxt = view.findViewById<TextView>(R.id.name_text)
        emailTxt = view.findViewById<TextView>(R.id.email_text)
        logoutBtn = view.findViewById<Button>(R.id.logoutBtn)

        mToolBar = view.findViewById(R.id.toolbar)
        mToolBar!!.setTitle(R.string.profile_text)
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userObserver = Observer<User> { newUser ->
            val firstName = newUser.firstName
            val lastName = newUser.lastName
            val fullName = "$firstName $lastName"
            nameTxt.text = fullName
            emailTxt.text = newUser.email

        }
        viewModel.getUser().observe(this, userObserver)

        /*val firstNameObserver = Observer<String> { newName ->
            val lastName = viewModel.getLastName().value.toString()
            val fullName = "$newName $lastName"
            nameTxt!!.text = fullName
        }
        viewModel.getFirstName().observe(this, firstNameObserver)

        val lastNameObserver = Observer<String> { newName ->
            val firstName = viewModel.getFirstName().value.toString()
            val fullName = "$firstName $newName"
            nameTxt!!.text = fullName
        }
        viewModel.getLastName().observe(this, lastNameObserver)*/

        logoutBtn.setOnClickListener{
            (activity as MainActivity).logout()
        }
    }
}