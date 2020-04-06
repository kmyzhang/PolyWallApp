package com.senders.polywallapp

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

const val GALLERY_REQUEST_CODE = 1
class HomePageFragment: Fragment() {
    //Firebase references
    private var mDatabaseReference: DatabaseReference? = null
    private var mDatabase: FirebaseDatabase? = null
    private var mAuth: FirebaseAuth? = null

    private var TAG: String = "HomePageFragment"
    private lateinit var imageView: ImageView
    private var nameTxt: TextView? = null


    companion object {
        private const val ARG_COUNT = "arg_count"
        fun newInstance(count: Int) = SignUpPageFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_COUNT, count)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?)
            = inflater.inflate(R.layout.test_page, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase!!.reference!!.child("Users")
        mAuth = FirebaseAuth.getInstance()

        nameTxt = view.findViewById<TextView>(R.id.nameTxt)
        val btnCamera = view.findViewById<Button>(R.id.btnTakePic)
        val btnUpload = view.findViewById<Button>(R.id.btnUpload)
        imageView = view.findViewById<ImageView>(R.id.image)
        btnCamera.setOnClickListener{
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent,0)
        }
        btnUpload.setOnClickListener{
            val intent = Intent(Intent.ACTION_PICK)
            intent.setType("image/*")
            val mimeTypes = arrayOf("image/jpeg", "image/png")
            intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes)
            startActivityForResult(intent,GALLERY_REQUEST_CODE)
        }

        println("${arguments?.getInt(ARG_COUNT)}")
    }

    override fun onStart() {
        super.onStart()
        val mUser = mAuth!!.currentUser
        val mUserReference = mDatabaseReference!!.child(mUser!!.uid)
        mUserReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                nameTxt!!.text = snapshot.child("firstName").value as String
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            Log.e("requestCode val", requestCode.toString())
            when (requestCode) {
                GALLERY_REQUEST_CODE -> imageView.setImageURI(data?.data) // handle chosen image
                0 -> {val bitmap = data?.extras?.get("data") as Bitmap
                    imageView.setImageBitmap(bitmap)}
            }
        }
    }


    /*override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }*/
}