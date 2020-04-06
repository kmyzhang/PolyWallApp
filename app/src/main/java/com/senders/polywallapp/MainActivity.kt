package com.senders.polywallapp

import android.content.ClipData
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.replace
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity(), StartupFragment.FragmentCallback,
    SignUpPageFragment.FragmentCallback, RouteListFragment.FragmentCallback {

    val SIGN_IN_FRAG = "sign_in_frag"
    val helpers = Helpers()

    private lateinit var viewModel: RoutesViewModel
    private lateinit var uViewModel: UserViewModel
    private lateinit var authStateListener : FirebaseAuth.AuthStateListener
    private lateinit var mAuth: FirebaseAuth

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->

        when (item.itemId) {
            R.id.routes_list -> {
                setupViewRouteList()
                //Log.d("checked", item.)
                // val albumsFragment = RoutesListFragment.newInstance()
                // openFragment(albumsFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.personal_log -> {
                setupViewPersonalLog()
                Log.d("checked", item.order.toString())
                return@OnNavigationItemSelectedListener true
            }
            R.id.profile -> {
                setupViewProfile()
                Log.d("checked", item.order.toString())
                // openFragment(ProfileFragment)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        uViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        viewModel = ViewModelProvider(this).get(RoutesViewModel::class.java)

        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)

        val pos = helpers.getCheckedItem(bottomNavigation)
        Log.d("current POS: ", pos.toString())
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

            mAuth = FirebaseAuth.getInstance()
            authStateListener = FirebaseAuth.AuthStateListener {
                if (mAuth.currentUser != null) { //user logged in and is the fragment  the route list?
                    bottomNavigation.visibility = VISIBLE
                    setupViewRouteList()
                } else {
                    bottomNavigation.visibility = GONE
                    setupViewStartup()
                }
            }
        /*setupViewStartup()
        if(savedInstanceState != null){
            Log.d("TAG", "there is bundle")
            onSignInClick()
        }*/
        /*
        if(savedInstanceState != null){
            val frag = supportFragmentManager.getFragment(savedInstanceState, "ACTIVE_FRAGMENT")
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container_view, frag!!).commit()
        }*/



        mAuth = FirebaseAuth.getInstance()
        authStateListener = FirebaseAuth.AuthStateListener {
            if (mAuth.currentUser != null) { //user logged in
                bottomNavigation.selectedItemId = R.id.routes_list
                bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
                bottomNavigation.visibility = VISIBLE
                setupViewRouteList()
            } else {
                bottomNavigation.visibility = GONE
                setupViewStartup()
            }
        }

    }


    override fun onStart() {
        super.onStart()
        mAuth.addAuthStateListener(authStateListener)
    }


    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        onCreate(savedInstanceState)
    }

    override fun onSignInClick(){

        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left,
                R.anim.enter_left_to_right, R.anim.exit_left_to_right)
            //.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            .replace(R.id.fragment_container_view, SignUpPageFragment.newInstance(),SIGN_IN_FRAG)
            .addToBackStack(null)
            .commit()

    }

    override fun onConfirmedLoginClick() {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left,
                    R.anim.enter_left_to_right, R.anim.exit_left_to_right)
            //.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            .replace(R.id.fragment_container_view, RouteListFragment())
            .commit()
    }

    fun setupViewStartup() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_view, StartupFragment.newInstance())
            .commit()
    }

    fun setupViewRouteList() {
        supportFragmentManager.beginTransaction()
            //.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            .replace(R.id.fragment_container_view, RouteListFragment())
            .addToBackStack(null)
            .commit()
    }

    fun setupViewProfile() {
        supportFragmentManager.beginTransaction()
            //.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            .replace(R.id.fragment_container_view, ProfileFragment.newInstance(0))
            .addToBackStack(null)
            .commit()
    }

    fun setupViewPersonalLog() {
        supportFragmentManager.beginTransaction()
            //.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            .replace(R.id.fragment_container_view, PersonalLogFragment.newInstance(0))
            .addToBackStack(null)
            .commit()
    }

    override fun onRouteSelected(id: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_view, RouteProfileFragment.newInstance(id))
            .addToBackStack(null)
            .commit()
    }

    override fun onAddRouteSelected() {
        setupViewPersonalLog()
    }

    /*override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (mAuth.currentUser != null) {
            val inflater = menuInflater
            inflater.inflate(R.menu.menu_main, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.personal_log -> {
                //add the function to perform here
                Log.d("tag", "Hello, World")
                setupViewPersonalLog()
            }
            R.id.profile -> {
                Log.d("tag", "Go do something")
                setupViewProfile()
            }
            R.id.route_list -> {
                setupViewRouteList()
            }
        }
        return super.onOptionsItemSelected(item)
    }
*/
    override fun createBtnClick(){
        invalidateOptionsMenu()
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left,
                R.anim.enter_left_to_right, R.anim.exit_left_to_right)
            .replace(R.id.fragment_container_view, RouteListFragment())
            .commit()
    }

    fun logout() {
        //go to next frame
        FirebaseAuth.getInstance().signOut()
        this.invalidateOptionsMenu()
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        setupViewStartup()
    }

/*
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val f : Fragment?  = supportFragmentManager.fragments.first()
        supportFragmentManager.putFragment(outState, "ACTIVE_FRAGMENT", f!!)
    }


    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
            val mFragment = supportFragmentManager.getFragment(savedInstanceState, "ACTIVE_FRAGMENT")
            supportFragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.fragment_container_view, mFragment!!)
                .commit()


    }*/


}
