package com.aemm.libreria.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aemm.libreria.databinding.ActivityLibraryBinding
import com.google.firebase.auth.FirebaseAuth

class LibraryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLibraryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}