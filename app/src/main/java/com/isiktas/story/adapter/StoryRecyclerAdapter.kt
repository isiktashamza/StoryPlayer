package com.isiktas.story.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.isiktas.story.R
import com.isiktas.story.model.StoryList
import de.hdodenhof.circleimageview.CircleImageView

class StoryRecyclerAdapter(
    val stories: ArrayList<StoryList>,
    private val context: Context,
    private val itemClick: StoryClickListener
) : RecyclerView.Adapter<StoryRecyclerAdapter.ViewHolder>() {

    interface StoryClickListener{
        fun storyClicked(index: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_story, parent, false))

    }

    override fun getItemCount(): Int {
        return stories.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = stories[position].stories.elementAt(0)
        holder.username.text = item.username
        Glide.with(context).load(item.userPP).into(holder.profilePic)
        holder.bindData(position, itemClick)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profilePic = itemView.findViewById<CircleImageView>(R.id.row_story_profile)
        val username = itemView.findViewById<TextView>(R.id.row_story_username)

        fun bindData(position: Int, clickListener: StoryClickListener){
            itemView.setOnClickListener {
                clickListener.storyClicked(position)
            }
        }
    }


}