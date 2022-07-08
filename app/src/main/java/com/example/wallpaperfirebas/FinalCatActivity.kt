package com.example.wallpaperfirebas

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.wallpaperfirebas.Adapter.CatImagesAdapter
import com.example.wallpaperfirebas.Model.BomModel
import com.example.wallpaperfirebas.databinding.ActivityFinalCatBinding
import com.google.firebase.firestore.FirebaseFirestore

class FinalCatActivity : AppCompatActivity() {

    lateinit var binding: ActivityFinalCatBinding
    lateinit var db: FirebaseFirestore

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFinalCatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()

        //OBTIENE LOS DATOS DEL CatAdapter de la principal CATActivity para ser listado en FinalCatActivity
        val uid = intent.getStringExtra("uid")
        val name = intent.getStringExtra("name")

        binding.catTitle.text = name.toString()
        binding.catSubtitle.text = "${name.toString()} Wallpaper"

        db.collection("categories").document(uid!!).collection("wallpaper")
            .addSnapshotListener { value, error ->

            val listOfCatWallpaper = arrayListOf<BomModel>()
            val data = value?.toObjects(BomModel::class.java)
            listOfCatWallpaper.addAll(data!!)

            binding.rcvFinal.layoutManager =
                GridLayoutManager(this@FinalCatActivity, 3)
                binding.rcvFinal.adapter = CatImagesAdapter(this, listOfCatWallpaper, uid)
        }

        binding.btnDone.setOnClickListener {
            if (binding.edtLink.text.toString().isEmpty())
            {
                Toast.makeText(this, "Please Enter Valid Data", Toast.LENGTH_SHORT).show()
            } else {
                val finalUniqID = db.collection("categories").document().id
                val finalData = BomModel(id = finalUniqID, link = binding.edtLink.text.toString())
                db.collection("categories").document(uid)
                    .collection("wallpaper").document(finalUniqID).set(finalData)
                    .addOnCompleteListener {

                        if (it.isSuccessful)
                        {
                            Toast.makeText(
                                this@FinalCatActivity,
                                "Wallpaper Added Success",
                                Toast.LENGTH_SHORT
                            ).show()
                            binding.edtLink.setText("")
                            binding.edtLink.clearFocus()

                        }else {
                            Toast.makeText(
                                this@FinalCatActivity,
                                ""+ it.exception?.localizedMessage,
                                Toast.LENGTH_SHORT
                            ).show()
                            binding.edtLink.setText("")
                            binding.edtLink.clearFocus()

                        }
                    }
            }
        }
    }
}