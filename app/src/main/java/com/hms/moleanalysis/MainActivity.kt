// Copyright 2022. Explore in HMS. All rights reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at

// http://www.apache.org/licenses/LICENSE-2.0

// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.hms.moleanalysis

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class MainActivity : AppCompatActivity() {

    private var detector: ModelDetector? = null
    private var bitmap: Bitmap? = null
    private var runButton: Button? = null
    private var choose: Button? = null
    private var capturedImage: ImageView? = null
    private var resultText: TextView? = null
    private var textHeader: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        runButton = findViewById(R.id.Run)
        choose = findViewById(R.id.choosePicture)
        capturedImage = findViewById(R.id.capturedImageView)
        resultText = findViewById(R.id.resultText)
        textHeader = findViewById(R.id.textHeader)

        runButton?.setOnClickListener {
            detector = ModelDetector(this)
            detector!!.loadModelFromAssets()
            runOnClick()
        }

        choose?.setOnClickListener {
            choosePhoto()
        }
    }


    override fun onStop() {
        super.onStop()
        if (detector != null) {
            detector!!.close()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        bitmap = processIntent(requestCode, resultCode, data)
        capturedImage!!.setImageBitmap(bitmap)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                choosePhoto()
            } else {
                Toast.makeText(this, "no permission", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun processIntent(requestCode: Int, resultCode: Int, data: Intent?): Bitmap? {
        if (requestCode == RC_CHOOSE_PHOTO) {
            if (data == null) {
                return null
            }
            val uri: Uri? = data.data
            val filePath = FileUtil.getFilePathByUri(this, uri!!)
            if (!TextUtils.isEmpty(filePath)) {
                Log.e(TAG, "file is $filePath")
                return BitmapFactory.decodeFile(filePath)
            }
        }
        return null
    }

    private fun checkPermissionIfNecessary() {
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_DENIED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ),
                REQUEST_PERMISSION_CODE
            )
        } else {
            val intentToPickPic = Intent(Intent.ACTION_PICK, null)
            intentToPickPic.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
            startActivityForResult(intentToPickPic, RC_CHOOSE_PHOTO)
        }
    }

    private fun choosePhoto() {
        checkPermissionIfNecessary()
    }

    private fun runOnClick() {
        detector!!.predict(bitmap,
            { mlModelOutputs ->
                Log.i(TAG, "interpret get result")
                val result = detector!!.resultPostProcess(mlModelOutputs!!)
                showResult(result)
                Log.i(TAG, "result: $result")
            }) { e ->
            e.printStackTrace()
            Log.e(TAG, "interpret failed, because " + e.message)
            Toast.makeText(
                this@MainActivity,
                "interpret failed, because" + e.message,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun showResult(result: String?) {
        resultText!!.text = result
        textHeader!!.visibility = View.VISIBLE
       // Toast.makeText(this, result, Toast.LENGTH_LONG).show()
    }

    companion object {
        private const val TAG = "HMS_HIAI_MINDSPORE"
        private const val REQUEST_PERMISSION_CODE = 10
        private const val RC_CHOOSE_PHOTO = 2
    }


}