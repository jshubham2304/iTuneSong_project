package com.android.music.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.music.R
import com.android.music.model.Songs
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import java.util.concurrent.TimeUnit


class SongAdapter internal constructor(
    var context: Context
) : RecyclerView.Adapter<SongAdapter.SongViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context);
    private var songs = emptyList<Songs>() // Cached copy of words

    inner class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.trackName);
        val image: CircleImageView = itemView.findViewById(R.id.image);
        val type: TextView = itemView.findViewById(R.id.type);
        val time: TextView = itemView.findViewById(R.id.time);
        val collectioName: TextView = itemView.findViewById(R.id.CollectionName);

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val itemView = inflater.inflate(R.layout.item_card, parent,false)
        return SongViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val current = songs[position]
        holder.name.text = current.trackName ;
        holder.type.text = current.primaryGenreName;
        val millis =current.trackTimeMillis.toLong()
        val hms = String.format(
            "%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
            TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(
                TimeUnit.MILLISECONDS.toHours(millis)
            ),
            TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(
                TimeUnit.MILLISECONDS.toMinutes(millis)
            )
        )
        holder.time.text = hms
        Glide.with(context).load(current.image).centerCrop().into(holder.image)
        holder.collectioName.text = current.collectionName;
    }

    internal fun setWords(list: List<Songs>) {
        this.songs = list
        notifyDataSetChanged()
    }

    override fun getItemCount() = songs.size
}


