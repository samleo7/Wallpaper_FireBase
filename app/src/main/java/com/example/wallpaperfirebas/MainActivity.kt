package com.example.wallpaperfirebas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.wallpaperfirebas.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cardBOM.setOnClickListener {
            startActivity(Intent(this@MainActivity, BOMActivity::class.java))
        }

        binding.cardCAT.setOnClickListener {
            startActivity(Intent(this@MainActivity, CATActivity::class.java))
        }

        binding.cardTCT.setOnClickListener {
            startActivity(Intent(this@MainActivity, TCTActivity::class.java))

        }
    }
}