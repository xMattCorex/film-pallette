package com.mattcore.filmpallette

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.Toast
import androidx.palette.graphics.Palette
import com.github.dhaval2404.imagepicker.ImagePicker
import com.mattcore.filmpallette.databinding.ActivityMainBinding
import com.royrodriguez.transitionbutton.TransitionButton

class MainActivity : AppCompatActivity() {
    companion object {
        const val REQUEST_CODE_PICTURE_PICKER = 1012
        const val KEY_PATH = "path"
        const val KEY_CAPTION = "caption"
    }

    private lateinit var binding: ActivityMainBinding
    var bitmap: Bitmap? = null
    private lateinit var picturePath: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cardViewPicturePreview.setOnClickListener {
            ImagePicker.with(this)
                .galleryOnly()
                .start(REQUEST_CODE_PICTURE_PICKER)
        }

        binding.generateButton.setOnClickListener {
            if (bitmap != null) {
                transitionToActivity(it)
            } else if (binding.caption.text.isNullOrEmpty()) {
                Toast.makeText(this, "Provide a caption", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Load an image first", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val button = binding.generateButton

        button.text = "Generate"
        button.isClickable = true
        button.clearAnimation()
    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_PICTURE_PICKER) {
            val uri: Uri = data?.data!!
            picturePath = uri
            bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
            binding.picturePreview.setImageURI(uri)
            binding.noPictureIcon.visibility = View.GONE
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        }
    }

    private fun transitionToActivity(view: View) {
        val button = view as TransitionButton
        button.startAnimation()
        Handler().postDelayed({
            button.stopAnimation(
                TransitionButton.StopAnimationStyle.EXPAND
            ) {
                val intent = Intent(this, RevealActivity::class.java)
                intent.putExtra(KEY_PATH, picturePath)
                intent.putExtra(KEY_CAPTION, binding.caption.text.toString())
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(intent)
            }
        }, 2000)
    }
}