package com.mattcore.filmpallette

import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.palette.graphics.Palette
import com.mattcore.filmpallette.databinding.ActivityRevealBinding

class RevealActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRevealBinding
    private lateinit var path: Uri
    private lateinit var caption: String
    private lateinit var palette: Palette

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRevealBinding.inflate(layoutInflater)
        setContentView(binding.root)

        path = intent.extras!!.get(MainActivity.KEY_PATH) as Uri
        caption = intent.extras!!.getString(MainActivity.KEY_CAPTION) as String
        val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, path)
        palette = Palette.from(bitmap).maximumColorCount(4).generate()

        binding.caption.text = "Sent caption: $caption"
        binding.path.text = "Bitmap path: ${path.path}"

        binding.color1.setBackgroundColor(palette.swatches[0].rgb)
        binding.color2.setBackgroundColor(palette.swatches[1].rgb)
        binding.color3.setBackgroundColor(palette.swatches[2].rgb)
        binding.color4.setBackgroundColor(palette.swatches[3].rgb)

    }
}