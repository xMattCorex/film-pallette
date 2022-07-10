package com.mattcore.filmpallette

import android.R
import android.graphics.Bitmap
import android.graphics.drawable.ColorDrawable
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.palette.graphics.Palette
import androidx.transition.Fade
import androidx.transition.Transition
import androidx.transition.TransitionManager
import com.mattcore.filmpallette.databinding.ActivityRevealBinding
import java.io.File
import java.io.FileOutputStream


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
        palette = Palette.from(bitmap).maximumColorCount(24).generate()

        binding.caption.text = " $caption "

        binding.color1.setImageDrawable(ColorDrawable(palette.dominantSwatch!!.rgb))
        binding.color2.setImageDrawable(ColorDrawable(palette.swatches[0].rgb))
        binding.color3.setImageDrawable(ColorDrawable(palette.swatches[1].rgb))
        binding.color4.setImageDrawable(ColorDrawable(palette.swatches[2].rgb))

        binding.container.layoutParams.height = binding.container.width
        binding.picture.setImageURI(path)
        binding.saveButton.setOnClickListener {
            binding.container.apply {
                isDrawingCacheEnabled = true
                buildDrawingCache()
                saveFile(this)
            }
        }
        fadeReveal(binding.container, 1000)
        fadeReveal(binding.saveButton,1000, 800)
    }

    private fun saveFile(layout: ConstraintLayout) {
        val picture = createFile()
        val cache = layout.drawingCache
        try {
            val fileOutputStream =
                FileOutputStream(picture)
            cache.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
            fileOutputStream.flush()
            fileOutputStream.close()
        } catch (e: Exception) {
            Log.e("Reveal Activity", "Whoops! Problem: $e")
        } finally {
            layout.destroyDrawingCache()
            Toast.makeText(this@RevealActivity, "Picture saved!", Toast.LENGTH_LONG).show()
            MediaScannerConnection.scanFile(
                this@RevealActivity,
                arrayOf(picture.path),
                null,
                null
            )
        }
    }

    private fun createFile(): File {
        val file =
            File("${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)}/Palette/")
        if (!file.exists()) {
            file.mkdirs()
        }
        return File(file.absolutePath + "/palette_${System.currentTimeMillis()}" + ".png")
    }

    private fun fadeReveal(view: View, time: Long, delay: Long =0) {
        view.apply {
            alpha = 0f
            visibility = View.VISIBLE
            animate()
                .alpha(1f)
                .setDuration(time)
                .setListener(null)
                .startDelay = delay
        }
    }
}