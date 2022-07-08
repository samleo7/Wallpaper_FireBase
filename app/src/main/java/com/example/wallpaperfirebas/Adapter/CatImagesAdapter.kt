package com.example.wallpaperfirebas.Adapter

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wallpaperfirebas.Model.BomModel
import com.example.wallpaperfirebas.R
import com.google.firebase.firestore.FirebaseFirestore

class CatImagesAdapter (
    val requireContext: Context,
    val listBestOfTheMonth:ArrayList<BomModel>,
    val uid: String
    ):
    RecyclerView.Adapter<CatImagesAdapter.bomViewHolder>(){

    val db = FirebaseFirestore.getInstance()

        inner class bomViewHolder(itemView:View): RecyclerView.ViewHolder(itemView){
            val imageview = itemView.findViewById<ImageView>(R.id.cat_image)
            val btnDelete = itemView.findViewById<ImageView>(R.id.btn_delete)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): bomViewHolder {
        return bomViewHolder(
            LayoutInflater.from(requireContext).inflate(R.layout.item_wallpaper, parent, false)
        )
    }

    override fun onBindViewHolder(holder: bomViewHolder, position: Int) {
        Glide.with(requireContext).load(listBestOfTheMonth[position].link).into(holder.imageview)

        holder.btnDelete.setOnClickListener {
            val dialog = AlertDialog.Builder(requireContext)

            dialog.setMessage("Are you sure want to delete?")
            dialog.setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->

                db.collection("categories")
                    .document(uid).collection("wallpaper")
                    .document(listBestOfTheMonth[position].id).delete().addOnCompleteListener {
                        if (it.isSuccessful)
                        {
                            Toast.makeText(
                                requireContext,
                                "Wallpaper Deleted Successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                        }else {
                            Toast.makeText(
                                requireContext,
                                ""+ it.exception?.localizedMessage,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                dialog.dismiss()
            })
            dialog.setNegativeButton("No", DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()
            })
            dialog.show()

        }
    }

    override fun getItemCount() = listBestOfTheMonth.size


}