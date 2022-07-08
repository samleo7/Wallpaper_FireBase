package com.example.wallpaperfirebas.Adapter

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
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

class BomAdapter (val requireContext:Context, val listBestOfTheMonth:ArrayList<BomModel>):
    RecyclerView.Adapter<BomAdapter.bomViewHolder>(){

    val db = FirebaseFirestore.getInstance()

   inner class bomViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
       val imageview = itemView.findViewById<ImageView>(R.id.bom_image)
       val btnDelete = itemView.findViewById<ImageView>(R.id.btn_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): bomViewHolder {
        return bomViewHolder(
            LayoutInflater.from(requireContext).inflate(R.layout.item_bom, parent, false)
        )
    }

    override fun onBindViewHolder(holder: bomViewHolder, position: Int) {
        Glide.with(requireContext).load(listBestOfTheMonth[position].link).into(holder.imageview)

        holder.btnDelete.setOnClickListener {
            val dialog = AlertDialog.Builder(requireContext)

            dialog.setMessage("Are you sure want to delete?")
            dialog.setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->

                db.collection("bestofmonth").document(listBestOfTheMonth[position].id).delete()
                    .addOnCompleteListener {
                        if (it.isSuccessful)
                        {
                            Toast.makeText(
                                requireContext, "Wallpaper Deleted Successfully",
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
            dialog.setNegativeButton("No", DialogInterface.OnClickListener { dialog, i ->
                dialog.dismiss()
            })
            dialog.show()
        }
       /*holder.itemView.setOnClickListener {
            val intent = Intent(requireContext, FinalWallpaper::class.java)
            intent.putExtra("link", listBestOfTheMonth[position].link)
            requireContext.startActivity(intent)
        }*/
    }

    override fun getItemCount() = listBestOfTheMonth.size

}