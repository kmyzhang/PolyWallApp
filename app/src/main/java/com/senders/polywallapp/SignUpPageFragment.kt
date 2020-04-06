package com.senders.polywallapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

private var TAG = "CreateAccountActivity"

class SignUpPageFragment : Fragment() {
    //UI elements
    private var etFirstName: EditText? = null
    private var etLastName: EditText? = null
    private var etEmail: EditText? = null
    private var etPassword: EditText? = null
    private var btnCreateAccount: Button? = null

    private var mToolBar : Toolbar? = null

    //Firebase USER reference
    private var userRepo = FireBaseUserRepo()

    //Our own user object
    private lateinit var mUser : User

    private var callback : FragmentCallback? = null

    private var helper = Helpers()

    private lateinit var model : UserViewModel

    companion object{
        fun newInstance() : SignUpPageFragment{

            return SignUpPageFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?)
            = inflater.inflate(R.layout.fragment_signup, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialise(view)
        //view model??

        model = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
        mToolBar = view.findViewById(R.id.toolbar)
        mToolBar!!.setTitle(R.string.sign_up_text)
        (activity as AppCompatActivity?)!!.setSupportActionBar(mToolBar)
    }

    interface FragmentCallback{
        fun createBtnClick()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = context as FragmentCallback
    }

    override fun onDetach() {
        super.onDetach()
        callback = null
    }

    private fun initialise(view: View) {
        etFirstName = view.findViewById<View>(R.id.et_first_name) as EditText
        etLastName = view.findViewById<View>(R.id.et_last_name) as EditText
        etEmail = view.findViewById<View>(R.id.et_email) as EditText
        etPassword = view.findViewById<View>(R.id.et_password) as EditText
        btnCreateAccount = view.findViewById<View>(R.id.btn_register) as Button

        btnCreateAccount!!.setOnClickListener {
            mUser = User(etFirstName?.text.toString(),
                etLastName?.text.toString(),
                etEmail?.text.toString(),
                etPassword?.text.toString(),
                mutableListOf())

            if(helper.isFormEmpty(mUser)){
                Toast.makeText(requireActivity(), "Incomplete Form", Toast.LENGTH_SHORT).show()
            }
            else{
                val result = model.createNewAccount(mUser)
                if(result.equals("success")){
                    Toast.makeText(requireActivity(), result.toString(), Toast.LENGTH_SHORT).show()
                    callback?.createBtnClick()
                }
            }


        }
    }

    /*
    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        etFirstName?.setText(model.mUser.firstName)
    }*/


}
