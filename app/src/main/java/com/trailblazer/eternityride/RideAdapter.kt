package com.trailblazer.eternityride


import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class RideAdapter(var context: Context?, var list: ArrayList<Ride>) :
    RecyclerView.Adapter<RideAdapter.MyViewHolder>() {
    var lastPosition = -1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v: View = LayoutInflater.from(context).inflate(R.layout.ride_item, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val ride = list[position]
        holder.name.setText(ride.name)
        holder.desc.setText(ride.desc)
        holder.viewInMap.setOnClickListener { view: View? ->
            val uri = Uri.parse("https://www.google.com/maps/dir/" + ride.source + "/" + ride.dest)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            intent.setPackage("com.google.android.apps.maps")
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context?.startActivity(intent)
        }
        holder.like.setOnClickListener {
            holder.like.text = "Liked"
            val databaseReference: DatabaseReference =
                FirebaseDatabase.getInstance().getReference("Users")
                    .child(FirebaseAuth.getInstance().uid!!).child("LikedRides").push()
            databaseReference.setValue(ride)
        }
        Glide.with(context!!)
            .load(ride.imgUrl)
            .placeholder(R.drawable.load)
            .transition(GenericTransitionOptions.with(android.R.anim.fade_in))
            .into(holder.img)
        rvAnimation(holder.itemView, position)
    }

    private fun rvAnimation(itemView: View, position: Int) {
        if (position > lastPosition) {
            val animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left)
            itemView.animation = animation
            lastPosition = position
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView
        var desc: TextView
        var img: ImageView
        var viewInMap: Button
        var like: Button

        init {
            name = itemView.findViewById(R.id.tvRideName)
            desc = itemView.findViewById(R.id.tvRideDescription)
            img = itemView.findViewById(R.id.imgDestination)
            viewInMap = itemView.findViewById(R.id.btnViewInMap)
            like = itemView.findViewById(R.id.btnLike)
        }
    }
}