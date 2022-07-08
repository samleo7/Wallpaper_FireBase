package com.example.wallpaperfirebas.Adapter

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wallpaperfirebas.FinalCatActivity
import com.example.wallpaperfirebas.Model.CatModel
import com.example.wallpaperfirebas.R
import com.google.firebase.firestore.FirebaseFirestore

class CatAdapter(val  requireContext:Context, val listOfCat:ArrayList<CatModel>):
    RecyclerView.Adapter<CatAdapter.bomViewHolder>(){

    val db = FirebaseFirestore.getInstance()

   inner class bomViewHolder (itemView:View): RecyclerView.ViewHolder(itemView){
       val imageview = itemView.findViewById<ImageView>(R.id.cat_image)
       val name = itemView.findViewById<TextView>(R.id.cat_name)
       val btnDelete = itemView.findViewById<ImageView>(R.id.btn_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): bomViewHolder {
        return bomViewHolder(
            LayoutInflater.from(requireContext).inflate(R.layout.item_cat, parent, false)
        )
    }

    override fun onBindViewHolder(holder: bomViewHolder, position: Int) {
        holder.name.text = listOfCat[position].name
        Glide.with(requireContext).load(listOfCat[position].link).into(holder.imageview)

        holder.itemView.setOnClickListener {
            val intent = Intent(requireContext, FinalCatActivity::class.java)
            intent.putExtra("uid", listOfCat[position].id)
            intent.putExtra("name",listOfCat[position].name)
            requireContext.startActivity(intent)

        }

        holder.btnDelete.setOnClickListener {
            val dialog = AlertDialog.Builder(requireContext)

            dialog.setMessage("Are you sure want to delete?")
            dialog.setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->

                db.collection("categories").document(listOfCat[position].id).delete()
                    .addOnCompleteListener {
                        if (it.isSuccessful)
                        {
                            Toast.makeText(
                                requireContext,
                                "Category Deleted Successfully",
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

    override fun getItemCount() = listOfCat.size
}