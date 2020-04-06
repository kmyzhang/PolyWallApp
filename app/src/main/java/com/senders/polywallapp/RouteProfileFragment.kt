package com.senders.polywallapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso

//rand
class RouteProfileFragment: Fragment() {
    //Data Tools
    private lateinit var viewModel: RoutesViewModel
    private lateinit var uViewModel: UserViewModel
    //private lateinit var callback: FragmentCallback

    //UI Elements
    private lateinit var routeNameText : TextView
    private lateinit var routeRatingText : TextView
    private lateinit var routeTypeText : TextView
    private lateinit var routeDateText : TextView
    private lateinit var routeImage : ImageView
    //private lateinit var addButton : Button


    /*interface FragmentCallback {
        fun onAddRouteSelected(id: String)
    }*/

    companion object {
        private const val ROUTE_ID = "route"
        fun newInstance(id: String) = RouteProfileFragment().apply {
            arguments = Bundle().apply {
                putString(ROUTE_ID, id)
            }
        }
    }

    /*override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        callback = context as FragmentCallback
    }*/

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View? {
        val view: View = inflater.inflate(R.layout.fragment_route_profile, container, false)
        viewModel = ViewModelProvider(requireActivity()).get(RoutesViewModel::class.java)
        uViewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
        routeNameText = view.findViewById<TextView>(R.id.route_name_text)
        routeRatingText = view.findViewById<TextView>(R.id.route_rating_text)
        routeTypeText = view.findViewById<TextView>(R.id.route_type_text)
        routeDateText = view.findViewById<TextView>(R.id.route_exp_date_text)
        routeImage = view.findViewById<ImageView>(R.id.route_image)
        //addButton = view.findViewById<Button>(R.id.add_button)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val route = viewModel.getRouteById(arguments?.get(ROUTE_ID).toString())
        routeNameText.text = route.name
        routeRatingText.text = route.rating
        routeTypeText.text = route.type
        routeDateText.text = route.expDate
        Picasso.get()
            .load(route.imgUrl).fit().centerCrop()
            .placeholder(R.drawable.loading2)
            .into(routeImage)
        /*addButton.setOnClickListener {
            val uRoute = UserRoute(arguments?.get(ROUTE_ID).toString(),"flash","January 12, 2020")
            uViewModel.addRouteToUser(uRoute)
        }*/
    }
}