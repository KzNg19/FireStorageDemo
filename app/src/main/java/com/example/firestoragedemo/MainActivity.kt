package com.example.firestoragedemo

import android.app.Instrumentation
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import com.example.firestoragedemo.databinding.ActivityMainBinding
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var ref : StorageReference
    private lateinit var selectedUri : Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

            ref = FirebaseStorage.getInstance().reference

        binding.btnUpload.setOnClickListener {
            val fileName = "W1234.png"
            val imgRef = ref.child("images/${fileName}")
            imgRef.putFile(selectedUri)
                .addOnSuccessListener {
                    binding.imageView.setImageURI(null)
                    Toast.makeText(applicationContext,"Complete",Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener{
                    Toast.makeText(applicationContext,it.message,Toast.LENGTH_SHORT).show()
                }
        }

        binding.btnGet.setOnClickListener {
            val fileName = "W1234.png"
            val imgRef = ref.child("images/${fileName}")
            val file = File.createTempFile("temp","png")


            imgRef.getFile(file)
                .addOnSuccessListener {
                    binding.imageView.setImageURI(file.toUri())
                    //val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                    //binding.imageView.setImageBitmap(bitmap)
                }
               .addOnFailureListener{
                   Toast.makeText(applicationContext,it.message,Toast.LENGTH_SHORT).show()
               }
        }

        binding.btnBrowse.setOnClickListener {
            startForResult.launch("image/*")
        }
    }
    val startForResult = registerForActivityResult(
        ActivityResultContracts.GetContent()){ uri ->
            selectedUri = uri
            binding.imageView.setImageURI(uri)
    }
}