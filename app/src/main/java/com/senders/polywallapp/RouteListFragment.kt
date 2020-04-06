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
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso


class RouteListFragment: Fragment() {
    private var mToolBar : Toolbar? = null
    //Foundational Tools
    private lateinit var adapter: RecyclerAdapter
    private lateinit var viewModel: RoutesViewModel
    private lateinit var uViewModel: UserViewModel
    private lateinit var callback: FragmentCallback

    //UI Elements
    private lateinit var list: RecyclerView

    interface FragmentCallback {
        fun onRouteSelected(id: String)
        fun onAddRouteSelected()
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        callback = context as FragmentCallback
    }

    companion object {
        private const val ARG_COUNT = "arg_count"
        fun newInstance(id: String) = RouteListFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_COUNT, id)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View? {
        val view : View = inflater.inflate(R.layout.fragment_route_list, container, false)
        viewModel = ViewModelProvider(requireActivity()).get(RoutesViewModel::class.java)
        uViewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
        mToolBar = view.findViewById(R.id.toolbar)
        mToolBar!!.setTitle(R.string.route_list_text)
        (activity as AppCompatActivity?)!!.setSupportActionBar(mToolBar)
        list = view.findViewById(R.id.list)
        val itemDecor = DividerItemDecoration(requireActivity(), HORIZONTAL)
        list.addItemDecoration(itemDecor)
        list.layoutManager = LinearLayoutManager(requireActivity())
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val routesObserver = Observer<MutableList<Route>> { newRoutes ->
            var emptyView = view.findViewById<TextView>(R.id.empty_view)
            if (newRoutes.isEmpty()) {
                list.setVisibility(View.GONE)
                emptyView.setVisibility(View.VISIBLE)
            }
            else {
                list.setVisibility(View.VISIBLE)
                emptyView.setVisibility(View.GONE)
            }

            list.adapter = RecyclerAdapter(newRoutes)
        }
        viewModel.getRoutes().observe(viewLifecycleOwner, routesObserver)
        /*val itemTouchHelperCallback =
            object :
                ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {

                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    var position = viewHolder.adapterPosition
                    adapter.addAt(position)
                }

            }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(list)
        list.adapter = adapter*/
    }

    inner class RecyclerAdapter(routes: MutableList<Route>) : RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder>()  {
        private var routes: MutableList<Route>

        init {
            this.routes = routes
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        fun addAt(position: Int) {
            val uRoute = UserRoute(routes[position].id,"flash","January 12, 2020")
            uViewModel.addRouteToUser(uRoute)
            callback.onAddRouteSelected()
        }

        override fun getItemViewType(position: Int): Int {
            return position
        }

        override fun getItemCount() = routes.size

        override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
            val route = routes[position]
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
                    .load(route.imgUrl).fit().centerCrop()
                    .placeholder(R.drawable.loading2)
                    .into(mRouteImage)
                mRouteAdd?.setOnClickListener {
                    val uRoute = UserRoute(route.id,"flash","January 12, 2020")
                    uViewModel.addRouteToUser(uRoute)
                    callback.onAddRouteSelected()
                }
                view.setOnClickListener {
                    callback.onRouteSelected(route.id)
                }
            }
        }
    }
}