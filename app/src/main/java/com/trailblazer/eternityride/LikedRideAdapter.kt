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


class LikedRideAdapter(var context: Context, likedRideArrayList: ArrayList<Ride>) :
    RecyclerView.Adapter<LikedRideAdapter.MyViewHolder>() {
    var list: ArrayList<Ride>
    var lastPosition = -1

    init {
        list = likedRideArrayList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v: View =
            LayoutInflater.from(context).inflate(R.layout.liked_ride_item, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val likedRide = list[position]
        holder.name.setText(likedRide.name)
        holder.desc.setText(likedRide.desc)
        holder.viewInMapLiked.setOnClickListener { view: View? ->
            val uri = Uri.parse("https://www.google.com/maps/dir/" + likedRide.source + "/" + likedRide.dest)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            intent.setPackage("com.google.android.apps.maps")
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context?.startActivity(intent)
        }
        holder.chat.setOnClickListener { }
        Glide.with(context)
            .load(likedRide.imgUrl)
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
        var viewInMapLiked: Button
        var chat: Button

        init {
            name = itemView.findViewById(R.id.tvLikedName)
            desc = itemView.findViewById(R.id.tvLikedDesc)
            img = itemView.findViewById(R.id.imgLikedRide)
            viewInMapLiked = itemView.findViewById(R.id.btnViewInMapLiked)
            chat = itemView.findViewById(R.id.btnChat)
        }
    }
}