package com.trailblazer.eternityride

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.UUID


class AddRideActivity : AppCompatActivity() {
    private var storageReference: StorageReference? = null
    private var databaseReference: DatabaseReference? = null
    private var progressIndicator: LinearProgressIndicator? = null
    private var imageUri: Uri? = null
    private var editRideName: TextInputEditText? = null
    private var editRideDescription: TextInputEditText? = null
    private var editRideSource: TextInputEditText? = null
    private var editRideDest: TextInputEditText? = null
    private lateinit var btnSelectImage: Button
    private lateinit var uploadImageView: ImageView
    private lateinit var btnSaveRide: Button
    private val galleryLauncher = registerForActivityResult (ActivityResultContracts.GetContent()) {
            imageUri = it
            try {
                Glide.with(this@AddRideActivity).load(imageUri).into(uploadImageView)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_ride)
        FirebaseApp.initializeApp(this)
        storageReference = FirebaseStorage.getInstance().reference
        setContentView(R.layout.activity_add_ride)
        btnSelectImage = findViewById(R.id.btnChooseVideo)
        btnSaveRide = findViewById(R.id.btnSaveRide)
        editRideDescription = findViewById(R.id.editRideDescription)
        editRideDest = findViewById(R.id.editRideDest)
        editRideSource = findViewById(R.id.editRideSource)
        editRideName = findViewById(R.id.editRideName)
        progressIndicator = findViewById(R.id.process)
        uploadImageView = findViewById(R.id.uploadRideImageView)
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
            .child(FirebaseAuth.getInstance().uid!!).child("UploadedRides").push()
        btnSelectImage.setOnClickListener {
                galleryLauncher.launch("image/*")
            }
        btnSaveRide.setOnClickListener {
                uploadVideo(imageUri!!)
        }

    }

    private fun uploadVideo(uri: Uri) {
        val reference: StorageReference? = storageReference?.child(UUID.randomUUID().toString())
        reference?.putFile(uri)?.addOnSuccessListener {
            reference.downloadUrl.addOnSuccessListener { uri ->
                val ride = Ride()
                ride.name = editRideName?.text.toString()
                ride.imgUrl = uri.toString()
                ride.source = editRideSource?.text.toString()
                ride.dest = editRideDest?.text.toString()
                ride.desc = editRideDescription?.text.toString()
                databaseReference?.setValue(ride)
            }
            Toast.makeText(this@AddRideActivity, "Ride uploaded successfully!", Toast.LENGTH_SHORT)
                .show()
        }?.addOnFailureListener {
            Toast.makeText(
                this@AddRideActivity,
                "Failed to upload ride",
                Toast.LENGTH_SHORT
            ).show()
        }?.addOnProgressListener { snapshot ->
            progressIndicator?.visibility = View.VISIBLE
            progressIndicator?.max = Math.toIntExact(snapshot.totalByteCount)
            progressIndicator?.progress = Math.toIntExact(snapshot.bytesTransferred)
        }
    }
}


