package com.example.wallpaperfirebas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.wallpaperfirebas.Adapter.ColorToneAdapter
import com.example.wallpaperfirebas.Model.ColorToneModel
import com.example.wallpaperfirebas.databinding.ActivityTctactivityBinding
import com.google.firebase.firestore.FirebaseFirestore

class TCTActivity : AppCompatActivity() {

    lateinit var binding: ActivityTctactivityBinding
    lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTctactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()

        db.collection("thecolortone").addSnapshotListener {value, error ->

            val listTheColorTone = arrayListOf<ColorToneModel>()
            val data = value?.toObjects(ColorToneModel::class.java)
            listTheColorTone.addAll(data!!)

            binding.rcvTCT.layoutManager =
                GridLayoutManager(this@TCTActivity, 3)
            // Recycler Horizontal
            /*LinearLayoutManager(this@BOMActivity, LinearLayoutManager.HORIZONTAL, false)*/
            binding.rcvTCT.adapter = ColorToneAdapter(this@TCTActivity, listTheColorTone)
        }

        binding.btnDone.setOnClickListener {
            if (binding.edtColor.text.toString().isEmpty() && binding.edtLink.text.toString().isEmpty()
            ) {
                Toast.makeText(this@TCTActivity, "Please enter valid", Toast.LENGTH_SHORT).show()
            } else {
                addWallpaperToDB(binding.edtColor.text.toString(), binding.edtLink.text.toString())
            }
        }
    }

    private fun addWallpaperToDB(color: String, link: String) {
        val uid = db.collection("thecolortone").document().id
        val finalData = ColorToneModel(id = uid, color = color, link = link)

        db.collection("thecolortone").document(uid).set(finalData).addOnCompleteListener {
            if (it.isSuccessful)
            {
                Toast.makeText(
                    this@TCTActivity,
                    "Wallpaper Added Success",
                    Toast.LENGTH_SHORT
                ).show()
                binding.edtLink.setText("")
                binding.edtLink.clearFocus()
            }else {
                Toast.makeText(
                    this@TCTActivity,
                    ""+ it.exception?.localizedMessage,
                    Toast.LENGTH_SHORT
                ).show()
                binding.edtLink.setText("")
                binding.edtLink.clearFocus()
            }

        }

    }
}