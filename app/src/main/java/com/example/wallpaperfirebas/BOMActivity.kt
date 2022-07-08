package com.example.wallpaperfirebas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wallpaperfirebas.Adapter.BomAdapter
import com.example.wallpaperfirebas.Model.BomModel
import com.example.wallpaperfirebas.databinding.ActivityBomactivityBinding
import com.google.firebase.firestore.FirebaseFirestore

class BOMActivity : AppCompatActivity() {

    lateinit var binding:ActivityBomactivityBinding
    lateinit var db:FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBomactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()

        db.collection("bestofmonth").addSnapshotListener {value, error ->
            val listBestOfTheMonth = arrayListOf<BomModel>()
            val data = value?.toObjects(BomModel::class.java)
            listBestOfTheMonth.addAll(data!!)

            binding.rcvBOM.layoutManager =
                GridLayoutManager(this@BOMActivity, 3)
                // Recycler Horizontal
                /*LinearLayoutManager(this@BOMActivity, LinearLayoutManager.HORIZONTAL, false)*/
            binding.rcvBOM.adapter = BomAdapter(this@BOMActivity, listBestOfTheMonth)
        }

        binding.btnDone.setOnClickListener {
            if (binding.edtLink.text.toString().isEmpty())
            {
                Toast.makeText(this@BOMActivity,"Paste Your Link", Toast.LENGTH_SHORT).show()
            } else {
                addLinkToDatabase(binding.edtLink.text.toString())
            }
        }
    }

    private fun addLinkToDatabase(wallpaperLink: String) {

        val uid = db.collection("bestofmonth").document().id
        val  finalData = BomModel(uid, wallpaperLink)

        db.collection("bestofmonth").document(uid).set(finalData)
            .addOnCompleteListener {
                if (it.isSuccessful)
                {
                    Toast.makeText(
                        this@BOMActivity,
                        "Wallpaper Added Success",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.edtLink.setText("")
                    binding.edtLink.clearFocus()
                }else {
                    Toast.makeText(
                        this@BOMActivity,
                        ""+ it.exception?.localizedMessage,
                        Toast.LENGTH_SHORT
                        ).show()
                    binding.edtLink.setText("")
                    binding.edtLink.clearFocus()
                }
            }
    }
}