package com.trailblazer.eternityride.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.trailblazer.eternityride.Ride
import com.trailblazer.eternityride.RideAdapter
import com.trailblazer.eternityride.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    var databaseReference: DatabaseReference? = null
    var rideAdapter: RideAdapter? = null
    var list: ArrayList<Ride>? = null
    var recyclerView: RecyclerView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        recyclerView = binding.ridesRecyclerView
        databaseReference = FirebaseDatabase.getInstance().getReference("Rides")
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.setLayoutManager(LinearLayoutManager(context))
        list = ArrayList()
        rideAdapter = RideAdapter(context, list!!)
        recyclerView!!.setAdapter(rideAdapter)
        databaseReference!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dataSnapshot in snapshot.children) {
                    val ride: Ride? = dataSnapshot.getValue(Ride::class.java)
                    if (ride != null) {
                        list!!.add(ride)
                    }
                }
                rideAdapter!!.notifyItemInserted(list!!.size - 1)
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