package com.appdem.cameraFeature

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import java.io.File

class CameraHelper(private val context: Context) {

    private var imageCapture: ImageCapture? = null

    fun startCamera(
        previewView: PreviewView,
        lifecycleOwner: LifecycleOwner,
        onImageSaved: (File) -> Unit,
        onError: (Exception) -> Unit
    ) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            imageCapture = ImageCapture.Builder().build()

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()

                val camera = cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    imageCapture
                )

                val rotation = previewView.display.rotation
                preview.setSurfaceProvider(previewView.surfaceProvider)

                val imageCaptureOptions = ImageCapture.OutputFileOptions.Builder(
                    createNewFile(context, "image", ".jpg")
                ).build()

                imageCapture?.takePicture(
                    ImageCapture.OutputFileOptions.Builder(
                        createNewFile(context, "image", ".jpg")
                    ).build(),
                    ContextCompat.getMainExecutor(context),
                    object : ImageCapture.OnImageSavedCallback {
                        override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                            val savedUri = outputFileResults.savedUri ?: Uri.fromFile(
                                createNewFile(
                                    context,
                                    "image",
                                    ".jpg"
                                )
                            )
                            onImageSaved(File(savedUri.path!!))
                        }

                        override fun onError(exception: ImageCaptureException) {
                            onError(exception)
                        }
                    }
                )
            } catch (e: Exception) {
                onError(e)
            }

        }, ContextCompat.getMainExecutor(context))
    }

    private fun createNewFile(
        context: Context,
        prefix: String,
        suffix: String
    ): File {
        val imageName = "${prefix}_${System.currentTimeMillis()}$suffix"
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File(storageDir, imageName)
    }
}
