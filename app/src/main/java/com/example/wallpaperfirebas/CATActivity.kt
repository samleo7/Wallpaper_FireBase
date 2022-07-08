package com.example.wallpaperfirebas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.wallpaperfirebas.Adapter.CatAdapter
import com.example.wallpaperfirebas.Model.CatModel
import com.example.wallpaperfirebas.databinding.ActivityCatactivityBinding
import com.google.firebase.firestore.FirebaseFirestore

class CATActivity : AppCompatActivity() {

    lateinit var binding:ActivityCatactivityBinding
    lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCatactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()

        db.collection("categories").addSnapshotListener { value, error ->
            val listOfCategory = arrayListOf<CatModel>()
            val data = value?.toObjects(CatModel::class.java)
            listOfCategory.addAll(data!!)

            binding.rcvCAT.layoutManager = GridLayoutManager(this@CATActivity,2)
            binding.rcvCAT.adapter = CatAdapter(this@CATActivity, listOfCategory)
        }

        binding.btnDone.setOnClickListener {
            if (binding.edtName.text.toString().isEmpty() && binding.edtLink.text.toString().isEmpty()
            ){
                Toast.makeText(this, "Please Enter Valid Data", Toast.LENGTH_SHORT).show()
            } else {
                addDatatoDB(binding.edtName.text.toString(), binding.edtLink.text.toString())
            }
        }
    }

    private fun addDatatoDB(name: String, link: String) {
        val uid = db.collection("categories").document().id
        val finalData = CatModel (id = uid, name = name, link = link)

        db.collection("categories").document(uid).set(finalData).addOnCompleteListener {

            if (it.isSuccessful)
            {
                Toast.makeText(
                    this@CATActivity,
                    "Wallpaper Added Success",
                    Toast.LENGTH_SHORT
                ).show()
                binding.edtName.setText("")
                binding.edtName.clearFocus()
                binding.edtLink.setText("")
                binding.edtLink.clearFocus()

            }else {
                Toast.makeText(
                    this@CATActivity,
                    ""+ it.exception?.localizedMessage,
                    Toast.LENGTH_SHORT
                ).show()
                binding.edtName.setText("")
                binding.edtName.clearFocus()
                binding.edtLink.setText("")
                binding.edtLink.clearFocus()

            }
        }

    }
}