package com.dtl.smilep

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.FirebaseApp
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.face.FirebaseVisionFace
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions
import java.lang.reflect.Executable

class MainActivity : AppCompatActivity() {
    private lateinit var takeButton : Button
    private val PHOTO_REQUEST_CODE : Int = 9
    private lateinit var image : FirebaseVisionImage
    private lateinit var detector : FirebaseVisionFaceDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FirebaseApp.initializeApp(this)
        takeButton = findViewById(R.id.takeButton)

        takeButton.setOnClickListener {
            var takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if(takePicture.resolveActivity(packageManager)!=null){
                startActivityForResult(takePicture, PHOTO_REQUEST_CODE)
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == PHOTO_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            var extras = data?.extras
            var bitmap = extras?.get("data") as Bitmap
            detectFace(bitmap)

        }
    }
    private fun detectFace(bitmap : Bitmap){
        val highAccuracyOpts = FirebaseVisionFaceDetectorOptions.Builder()
            .setPerformanceMode(FirebaseVisionFaceDetectorOptions.ACCURATE)
            .setLandmarkMode(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
            .setClassificationMode(FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS)
            .build()

        try {
            image = FirebaseVisionImage.fromBitmap(bitmap)
        }
        catch (e:Exception){
            e.printStackTrace()
        }
        detector = FirebaseVision.getInstance()
            .getVisionFaceDetector(highAccuracyOpts)
        detector.detectInImage(image).addOnSuccessListener { firebaseVisionFaces ->
            var i = 0
            var resultText = ""
            if (firebaseVisionFaces != null) {
                if(firebaseVisionFaces.isEmpty()){
                    Toast.makeText(this,"No face detected",Toast.LENGTH_SHORT).show()
                }
                else{
                    for (face in firebaseVisionFaces) {
                        var prob = face.smilingProbability * 100
                        if(prob >= 50){
                            resultText = resultText.plus("\nface $i : ").plus("smile : $prob %").plus("\nlooks like someones happy \n")
                        }
                        else{
                            resultText = resultText.plus("\nface $i : ").plus("smile : $prob %").plus("\nlooks like someones not happy \n")
                        }
                        resultText = resultText.plus("\n\nwant to smile a bit? \n")

                        i++
                    }
                    val bundle : Bundle = Bundle()
                    bundle.putString(LCOfaceDetection.RESULT_TEXT,resultText)
                    val resultdialog = resultDialog()
                    resultdialog.arguments = bundle
                    resultdialog.show(supportFragmentManager , LCOfaceDetection.RESULT_DIALOG)
                }

            }
        }.addOnFailureListener{ e ->
            Toast.makeText(this,e.message,Toast.LENGTH_SHORT).show()
        }
    }
}