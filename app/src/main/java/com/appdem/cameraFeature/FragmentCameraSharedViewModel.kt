package com.appdem.cameraFeature

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import java.io.File

class FragmentCameraSharedViewModel : ViewModel() {
    private var imageList: MutableList<Bitmap> = mutableListOf()

    fun addImageToList(file: File) {
        val bitmap = BitmapFactory.decodeFile(file.absolutePath)
        imageList.add(bitmap)
    }

    fun getAllImages() = imageList
}