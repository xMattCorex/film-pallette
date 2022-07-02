package com.mattcore.filmpallette

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mattcore.filmpallette.databinding.ActivityRevealBinding

class RevealActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRevealBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRevealBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}