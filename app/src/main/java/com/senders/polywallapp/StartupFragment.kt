package com.senders.polywallapp

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso

class StartupFragment : Fragment() {

    private var etEmail: EditText? = null
    private var etPassword: EditText? = null

    private lateinit var loginBtn : Button
    private lateinit var signupBtn : Button
    private lateinit var img : ImageView
    private var callback : FragmentCallback? = null

    private lateinit var mlogin : Login
    private var userRepo = FireBaseUserRepo()
    private lateinit var model : UserViewModel

    companion object{
        fun newInstance() : StartupFragment{
            return StartupFragment()
        }
    }

    interface FragmentCallback{
        fun onConfirmedLoginClick()
        fun onSignInClick()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = context as FragmentCallback
    }

    override fun onDetach() {
        super.onDetach()
        callback = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.startup_page, container, false)
        img = view.findViewById<ImageView>(R.id.picture)

        Picasso.get().load(R.mipmap.full_logo).fit().centerInside().into(img)
        etEmail = view.findViewById(R.id.et_email)
        etPassword = view.findViewById(R.id.et_password)
        loginBtn = view.findViewById<Button>(R.id.btn_login)
        signupBtn = view.findViewById<Button>(R.id.btn_sign_up)
        model = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)


        loginBtn!!.setOnClickListener {
            mlogin = Login(etEmail?.text.toString(), etPassword?.text.toString(), userRepo.mAuth)
            mlogin.loginUser {
                if(it){
                    (activity as MainActivity).invalidateOptionsMenu()
                    callback?.onConfirmedLoginClick()
                }
                else{
                    Toast.makeText(requireContext(), "Failed to login", Toast.LENGTH_SHORT).show()
                }
            }
        }

        signupBtn.setOnClickListener { view ->
            callback?.onSignInClick()
        }

        return view
    }



}