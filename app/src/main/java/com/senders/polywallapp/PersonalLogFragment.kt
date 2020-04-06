package com.senders.polywallapp

import android.app.Activity
import android.graphics.drawable.ClipDrawable.HORIZONTAL
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso


class PersonalLogFragment: Fragment() {
    private var mToolBar : Toolbar? = null
    //Foundational Tools
    private lateinit var adapter: RecyclerAdapter
    private lateinit var viewModel: RoutesViewModel
    private lateinit var uViewModel: UserViewModel
    private lateinit var callback: RouteListFragment.FragmentCallback

    //UI Elements
    private lateinit var list: RecyclerView

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        callback = context as RouteListFragment.FragmentCallback
    }

    companion object {
        private const val ARG_COUNT = "arg_count"
        fun newInstance(count: Int) = PersonalLogFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_COUNT, count)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View? {
        uViewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
        viewModel = ViewModelProvider(requireActivity()).get(RoutesViewModel::class.java)
        val view : View = inflater.inflate(R.layout.fragment_route_list, container, false)
        mToolBar = view.findViewById(R.id.toolbar)
        mToolBar!!.setTitle(R.string.personal_log_text)
        list = view.findViewById(R.id.list)
        list.layoutManager = LinearLayoutManager(requireActivity())
        val itemDecor = DividerItemDecoration(requireActivity(), HORIZONTAL)
        list.addItemDecoration(itemDecor)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var emptyView = view.findViewById<TextView>(R.id.empty_view)
        val userObserver = Observer<User> { newUser ->
            if (uViewModel.mUser.value?.log!!.isEmpty()) {
                list.setVisibility(View.GONE)
                emptyView.setVisibility(View.VISIBLE)
            }
            else {
                list.setVisibility(View.VISIBLE)
                emptyView.setVisibility(View.GONE)
            }
            list.adapter = RecyclerAdapter(newUser.log)
        }
        uViewModel.getUser().observe(viewLifecycleOwner, userObserver)
    }

    inner class RecyclerAdapter(routes: MutableList<UserRoute>) : RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder>()  {
        private var routes: MutableList<UserRoute>

        init {
            this.routes = routes
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getItemViewType(position: Int): Int {
            return position
        }

        override fun getItemCount() = routes.size

        override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
            val route = viewModel.getRouteById(routes[position].id)
            holder.bindRoute(route)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
            val inflatedView = LayoutInflater.from(parent?.context).inflate(R.layout.route_item, parent,false)
            return RecyclerViewHolder(inflatedView)
        }

        inner class RecyclerViewHolder(v: View) : RecyclerView.ViewHolder(v) {
            private var view: View = v
            private var mRouteName: TextView? = null
            private var mRouteRating: TextView? = null
            private var mRouteDate: TextView? = null
            private var mRouteImage: ImageView
            private var mRouteAdd: Button? = null


            init {
                mRouteName = view.findViewById(R.id.route_name_text)
                mRouteRating = view.findViewById(R.id.route_rating_text)
                mRouteDate = view.findViewById(R.id.route_exp_date_text)
                mRouteImage = view.findViewById(R.id.route_image)
                mRouteAdd = view.findViewById(R.id.add_button)
            }

            fun bindRoute(route: Route) {
                mRouteName?.text = route.name
                mRouteRating?.text = route.rating
                mRouteDate?.text = route.expDate
                //Glide.with(requireActivity()).load(route.imgUrl).into(mRouteImage)
                Picasso.get()
                    .load(route.imgUrl)
                    .fit()
                    .centerCrop()
                    .placeholder(R.drawable.user_placeholder)
                    .into(mRouteImage);
                view.setOnClickListener {
                    callback.onRouteSelected(route.id)
                }
                mRouteAdd?.visibility = View.GONE
            }
        }
    }
}