package com.trailblazer.eternityride.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.trailblazer.eternityride.AddRideActivity
import com.trailblazer.eternityride.DashRideAdapter
import com.trailblazer.eternityride.Ride
import com.trailblazer.eternityride.databinding.FragmentDashboardBinding


class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    var databaseReference: DatabaseReference? = null
    var dashRideAdapter: DashRideAdapter? = null
    var list: ArrayList<Ride>? = null
    var recyclerView: RecyclerView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.addFab.setOnClickListener {
            val intent = Intent(context, AddRideActivity::class.java)
            startActivity(intent)
        }
        recyclerView = binding.dashRecyclerView
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
            .child(FirebaseAuth.getInstance().uid!!).child("UploadedRides")
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.layoutManager = LinearLayoutManager(context)
        list = ArrayList()
        dashRideAdapter = DashRideAdapter(requireContext(), list!!)
        recyclerView!!.adapter = dashRideAdapter
        databaseReference!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dataSnapshot in snapshot.children) {
                    val ride: Ride? = dataSnapshot.getValue(Ride::class.java)
                    if (ride != null) {
                        list!!.add(ride)
                    }
                }
                dashRideAdapter!!.notifyItemInserted(list!!.size - 1)
            }

            override fun onCancelled(error: DatabaseError) {}
        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}