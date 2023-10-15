package com.trailblazer.eternityride.ui.liked

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
import com.trailblazer.eternityride.LikedRideAdapter
import com.trailblazer.eternityride.Ride
import com.trailblazer.eternityride.databinding.FragmentLikedBinding


class LikedFragment : Fragment() {

    private var _binding: FragmentLikedBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    var databaseReference: DatabaseReference? = null
    var likedRideAdapter: LikedRideAdapter? = null
    var list: ArrayList<Ride>? = null
    var recyclerView: RecyclerView? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentLikedBinding.inflate(inflater, container, false)
        val root: View = binding.root
        recyclerView = binding.likedRecyclerView
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
            .child(FirebaseAuth.getInstance().uid!!).child("LikedRides")
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.layoutManager = LinearLayoutManager(context)
        list = ArrayList()
        likedRideAdapter = LikedRideAdapter(requireContext(), list!!)
        recyclerView!!.adapter = likedRideAdapter
        databaseReference!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dataSnapshot in snapshot.children) {
                    val likedRide: Ride? = dataSnapshot.getValue(Ride::class.java)
                    if (likedRide != null) {
                        list!!.add(likedRide)
                    }
                }
                likedRideAdapter!!.notifyItemInserted(list!!.size - 1)
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