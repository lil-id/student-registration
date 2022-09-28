package com.master.formpendaftaran

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.master.formpendaftaran.room.userDataModel
import java.util.*
import kotlin.collections.ArrayList

class listAllUserAdapter(private val data: ArrayList<userDataModel>, private val listener: OnAdapterListener) : RecyclerView.Adapter<listAllUserAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemViewModel= data[position]

        var byte: ByteArray? = Base64.getDecoder().decode(itemViewModel.img)
        var bitmap: Bitmap = BitmapFactory.decodeByteArray(byte, 0, byte?.size!!)

        holder.circleImageView.setImageBitmap(bitmap)
        holder.nameView.text = itemViewModel.nama
        holder.genderView.text = itemViewModel.gender

        holder.cardView.setOnClickListener {
            listener.onClick(itemViewModel)
        }

    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val circleImageView: ImageView = itemView.findViewById(R.id.circleImageView)
        val nameView: TextView = itemView.findViewById(R.id.name_user)
        val genderView: TextView = itemView.findViewById(R.id.gender_user)
        val cardView: ImageView = itemView.findViewById(R.id.imageView5)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(getData: List<userDataModel>) {
        data.clear()
        data.addAll(getData)
        notifyDataSetChanged()
    }

    interface OnAdapterListener {
        fun onClick(data: userDataModel)
    }
}