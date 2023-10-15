package com.trailblazer.eternityride

import android.content.Context
import android.graphics.drawable.Drawable
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


class DashRideAdapter(var context: Context, videoArrayList: ArrayList<Ride>) :
    RecyclerView.Adapter<DashRideAdapter.MyViewHolder>() {
    var list: ArrayList<Ride>
    var lastPosition = -1

    init {
        list = videoArrayList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v: View = LayoutInflater.from(context).inflate(R.layout.dash_item, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val ride = list[position]
        holder.name.setText(ride.name)
        holder.desc.setText(ride.desc)
        /*holder.remove.setOnClickListener(view -> {

        });
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/Glide.with(context)
            .load(ride.imgUrl)
            .placeholder(R.drawable.load)
            .transition(GenericTransitionOptions.with<Drawable>(android.R.anim.fade_in))
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
        var edit: Button
        var remove: Button

        init {
            name = itemView.findViewById(R.id.tvDashRideName)
            desc = itemView.findViewById(R.id.tvDashRideDesc)
            img = itemView.findViewById(R.id.imgDashRide)
            edit = itemView.findViewById(R.id.btnDashRideEdit)
            remove = itemView.findViewById(R.id.btnDashRideRemove)
        }
    }
}