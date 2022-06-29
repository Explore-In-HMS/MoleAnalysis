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

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.util.Log
import android.widget.Toast
import com.huawei.hmf.tasks.OnFailureListener
import com.huawei.hmf.tasks.OnSuccessListener
import com.huawei.hms.mlsdk.common.MLException
import com.huawei.hms.mlsdk.custom.*
import com.hms.moleanalysis.R
import java.io.IOException
import java.text.DecimalFormat
import java.util.*

class ModelDetector
    (val mContext: Context) {

    private var labelList: List<String>? = mContext.resources.getStringArray(R.array.labels).asList()

    @Volatile
    private var loadOk = false

    @Volatile
    var modelExecutor: MLModelExecutor? = null
    private val mModelName = "mindspore"
    private val mModelFullName = "last_model.ms"

    private fun isLoadOk(): Boolean {
        return loadOk
    }

    fun loadModelFromAssets() {
        val localModel: MLCustomLocalModel =
            MLCustomLocalModel.Factory(mModelName).setAssetPathFile(mModelFullName).create()
        val settings: MLModelExecutorSettings = MLModelExecutorSettings.Factory(localModel).create()
        try {
            modelExecutor = MLModelExecutor.getInstance(settings)
            loadOk = true
        } catch (error: MLException) {
            error.printStackTrace()
            Log.i(TAG, "executor cannot be performed")
        }
    }

    fun predict(
        bitmap: Bitmap?,
        successCallback: OnSuccessListener<MLModelOutputs?>?,
        failureCallback: OnFailureListener?
    ) {
        if (!isLoadOk()) {
            Toast.makeText(mContext, "the model does not init", Toast.LENGTH_LONG).show()
            return
        }
        if (bitmap == null) {
            Toast.makeText(mContext, "please select an image to process!", Toast.LENGTH_LONG).show()
            return
        }
        val inputBitmap: Bitmap = resizeBitMap(bitmap)
        val input = preprocess(inputBitmap)
        Log.d(TAG, "interpret pre process")
        var inputs: MLModelInputs? = null
        try {
            inputs = MLModelInputs.Factory().add(input).create()
        } catch (e: MLException) {
            Log.e(TAG, "add inputs failed! " + e.message)
        }
        var inOutSettings: MLModelInputOutputSettings? = null
        try {
            val settingsFactory: MLModelInputOutputSettings.Factory =
                MLModelInputOutputSettings.Factory()
            settingsFactory.setInputFormat(
                0,
                MLModelDataType.FLOAT32,
                intArrayOf(1, BITMAP_SIZE, BITMAP_SIZE, 3)
            )
            val outputSettingsList = ArrayList<IntArray>()
            val outputShape = intArrayOf(1, labelList!!.size)
            outputSettingsList.add(outputShape)
            for (i in outputSettingsList.indices) {
                settingsFactory.setOutputFormat(i, MLModelDataType.FLOAT32, outputSettingsList[i])
            }
            inOutSettings = settingsFactory.create()
        } catch (e: MLException) {
            Log.e(TAG, "set input output format failed! " + e.message)
        }
        Log.d(TAG, "interpret start")
        modelExecutor?.exec(inputs, inOutSettings)?.addOnSuccessListener(successCallback)
            ?.addOnFailureListener(failureCallback)
    }

    fun close() {
        try {
            modelExecutor?.close()
        } catch (error: IOException) {
            error.printStackTrace()
        }
    }

    fun resultPostProcess(output: MLModelOutputs): String {
        val result: Array<FloatArray> = output.getOutput(0)
        val probabilities = result[0]
        return getPredictLabelInf(probabilities)
    }

    private fun resizeBitMap(bitmap: Bitmap?): Bitmap {
        val cropSize: Int = (bitmap?.width!!).coerceAtMost(bitmap.height)
        val cropBitmap: Bitmap = getCropBitmap(bitmap, cropSize, cropSize)
        return Bitmap.createScaledBitmap(cropBitmap, BITMAP_WIDTH, BITMAP_HEIGHT, false)
    }

    private fun getCropBitmap(input: Bitmap?, targetWidth: Int, targetHeight: Int): Bitmap {
        val output: Bitmap = Bitmap.createBitmap(targetWidth, targetHeight, Bitmap.Config.ARGB_8888)
        val srcL: Int
        val srcR: Int
        val srcT: Int
        val srcB: Int
        val dstL: Int
        val dstR: Int
        val dstT: Int
        val dstB: Int
        val w: Int? = input?.width
        val h: Int? = input?.height
        if (targetWidth > w!!) { // padding
            srcL = 0
            srcR = w
            dstL = (targetWidth - w) / 2
            dstR = dstL + w
        } else { // cropping
            dstL = 0
            dstR = targetWidth
            srcL = (w - targetWidth) / 2
            srcR = srcL + targetWidth
        }
        if (targetHeight > h!!) { // padding
            srcT = 0
            srcB = h
            dstT = (targetHeight - h) / 2
            dstB = dstT + h
        } else { // cropping
            dstT = 0
            dstB = targetHeight
            srcT = (h - targetHeight) / 2
            srcB = srcT + targetHeight
        }
        val src = Rect(srcL, srcT, srcR, srcB)
        val dst = Rect(dstL, dstT, dstR, dstB)
        Canvas(output).drawBitmap(input, src, dst, null)
        return output
    }

    private fun preprocess(inputBitmap: Bitmap): Any {
        val input = Array(1) { Array(BITMAP_SIZE) { Array(BITMAP_SIZE) { FloatArray(3) } } }
        for (h in 0 until BITMAP_SIZE) {
            for (w in 0 until BITMAP_SIZE) {
                val pixel: Int = inputBitmap.getPixel(w, h)
                input[0][h][w][0] = (Color.red(pixel) - IMAGE_MEAN[0]) / IMAGE_STD[0]
                input[0][h][w][1] = (Color.green(pixel) - IMAGE_MEAN[1]) / IMAGE_STD[1]
                input[0][h][w][2] = (Color.blue(pixel) - IMAGE_MEAN[2]) / IMAGE_STD[2]
            }
        }
        return input
    }


    private fun getPredictLabelInf(result: FloatArray): String {
        return processResult(labelList, result)
    }

    private class ValueComparator(var base: Map<String, Float>) :
        Comparator<String> {
        override fun compare(o1: String, o2: String): Int {
            return if (base[o1]!! >= base[o2]!!) {
                -1
            } else {
                1
            }
        }
    }

    companion object {
        private const val TAG = "HMS_HIAI_MINDSPORE"
        private const val BITMAP_SIZE = 224
        private const val BITMAP_WIDTH = 224
        private const val BITMAP_HEIGHT = 224
        private val IMAGE_MEAN = floatArrayOf(0.485f * 255f, 0.456f * 255f, 0.406f * 255f)
        private val IMAGE_STD = floatArrayOf(0.229f * 255f, 0.224f * 255f, 0.225f * 255f)
        private const val PRINT_LENGTH = 10
        private fun processResult(labelList: List<String>?, probabilities: FloatArray): String {
            val localResult: MutableMap<String, Float> = HashMap()
            val compare = ValueComparator(localResult)
            for (i in probabilities.indices) {
                localResult[labelList!![i]] = probabilities[i]
            }
            val result = TreeMap<String, Float>(compare)
            result.putAll(localResult)
            val builder = StringBuilder()
            var total = 0
            val df = DecimalFormat("0.00%")
            for ((key, value) in result) {
                if (total == PRINT_LENGTH || value <= 0) {
                    break
                }
                builder.append("No ")
                    .append(total)
                    .append("：")
                    .append(key)
                    .append(" / ")
                    .append(df.format(value))
                    .append(System.lineSeparator())
                total++
            }
            return builder.toString()
        }

    }
}