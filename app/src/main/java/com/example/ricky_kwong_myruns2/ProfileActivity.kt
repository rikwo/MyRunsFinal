package com.example.ricky_kwong_myruns2

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import java.io.ByteArrayOutputStream

class ProfileActivity : AppCompatActivity() {

    private lateinit var profilePicture: ImageView
    private lateinit var email: EditText
    lateinit var phone: EditText
    lateinit var name: EditText
    lateinit var gradClass: EditText
    lateinit var major: EditText
    lateinit var genderRadio : RadioGroup
    lateinit var male: RadioButton
    lateinit var female: RadioButton
    lateinit var encodedImage: String

    var genderCheck: Int = 5


    private fun loadProfile() {
        val sharedPref = getSharedPreferences("myProfile", Context.MODE_PRIVATE);
        genderCheck = sharedPref.getInt("genderCheck", 5)
        email.setText(sharedPref.getString("email", null))
        phone.setText(sharedPref.getString("phone", null))
        name.setText(sharedPref.getString("name", null))
        gradClass.setText(sharedPref.getString("gradClass", null))
        major.setText(sharedPref.getString("major", null))
        genderRadio.check(genderCheck)
        //load picture from https://dev.to/theimpulson/persisting-images-across-activity-s-lifecycle-using-sharedpreferences-in-android-kotlin-2o0d
        encodedImage = sharedPref.getString("encodedImage", "DEFAULT") ?: "DEFAULT"
        if (encodedImage != "DEFAULT") {
            val imageBytes = Base64.decode(encodedImage, Base64.DEFAULT)
            val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            profilePicture.setImageBitmap(decodedImage)
        }

    }

    private fun saveProfile() {
        val sharedPref = getSharedPreferences("myProfile", Context.MODE_PRIVATE);
        with (sharedPref.edit()) {
            putString("phone", phone.text.toString())
            putString("gradClass", gradClass.text.toString())
            putString("email", email.text.toString())
            putString("name", name.text.toString())
            putString("major", major.text.toString())
            putInt("genderCheck", genderCheck)
            putString("encodedImage", encodedImage)
            apply()
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        email = findViewById(R.id.emailField)
        phone = findViewById(R.id.phoneField)
        name = findViewById(R.id.nameField)
        major = findViewById(R.id.majorField)
        genderRadio = findViewById(R.id.radioButtons)
        male = findViewById(R.id.maleButton)
        female = findViewById(R.id.femaleButton)
        gradClass = findViewById(R.id.classField)
        profilePicture = findViewById(R.id.profilePicture)

        Util.checkPermissions(this)

        loadProfile()

        val save: Button = findViewById(R.id.saveButton)
        save.setOnClickListener {
            saveProfile()
            finish()
        }

        val cancel: Button = findViewById(R.id.cancelButton)
        cancel.setOnClickListener {
            finish()
        }

        genderRadio.setOnCheckedChangeListener {
                genderRadio, i->
            genderCheck = i
        }

        val change: Button = findViewById(R.id.changeButton)
        //below camera methods from https://www.youtube.com/watch?v=LsXpGES88a4
        change.setOnClickListener(View.OnClickListener { v: View? ->
            var listIndex = -1
            val profilePictureAlert = AlertDialog.Builder(this)
            profilePictureAlert.setTitle("Pick Profile Picture")
            val units = arrayOf("Open Camera", "Select from Gallery")
            profilePictureAlert.setSingleChoiceItems(units, listIndex) { dialogInterface, i ->
                if (i == 0) {
                    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(cameraIntent, 101)
                }
                else if (i == 1) {
                    val galleryIntent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(galleryIntent, 102)
                }

                dialogInterface.dismiss()
            }
            profilePictureAlert.setNeutralButton("Cancel") { dialog, which ->
                dialog.cancel()
            }
            profilePictureAlert.show()

        })


    }

    //below function from https://www.youtube.com/watch?v=LsXpGES88a4
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==101) {
            var picture = data?.getParcelableExtra<Bitmap>("data")
            profilePicture.setImageBitmap(picture)
            //save pictures from https://dev.to/theimpulson/persisting-images-across-activity-s-lifecycle-using-sharedpreferences-in-android-kotlin-2o0d
            val savePic = ByteArrayOutputStream()
            picture?.compress(Bitmap.CompressFormat.PNG, 100, savePic)
            encodedImage = Base64.encodeToString(savePic.toByteArray(), Base64.DEFAULT)
        }
        else if (requestCode==102) {
            val selectedUri: Uri? = data?.data
            val selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedUri)
            profilePicture.setImageBitmap(selectedImage)
            val saveGalleryPic = ByteArrayOutputStream()
            selectedImage?.compress(Bitmap.CompressFormat.PNG, 100, saveGalleryPic)
            encodedImage = Base64.encodeToString(saveGalleryPic.toByteArray(), Base64.DEFAULT)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState);

    }
}