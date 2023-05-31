package com.appdem.cameraFeature

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.appdem.R
import com.appdem.databinding.FragmentCameraBinding
import java.io.File
import java.io.FileOutputStream

class FragmentCamera : Fragment() {

    private lateinit var binding: FragmentCameraBinding
    private lateinit var cameraHelper: CameraHelper
    private val viewModel: FragmentCameraSharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_camera, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.checkPhotos.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentTwo_to_fragmentPictures)
        }

        cameraHelper = CameraHelper(requireContext())

        if (hasCameraPermission()) {
            binding.cameraButton.visibility = View.VISIBLE
        } else {
            binding.cameraButton.visibility = View.GONE
            requestCameraPermission()
        }
        binding.cameraButton.setOnClickListener {
            cameraHelper.startCamera(
                binding.previewView,
                viewLifecycleOwner,
                { file ->
                    viewModel.addImageToList(
                        rotateImageIfNeeded(
                            file
                        )
                    )
                },
                { error -> Log.e("Camera", "Error: $error") }
            )
        }
    }

    private fun requestCameraPermission() {
        requestPermissions(
            arrayOf(Manifest.permission.CAMERA),
            CAMERA_PERMISSION_REQUEST_CODE
        )
    }

    override fun onResume() {
        super.onResume()
        if (hasCameraPermission()) {
            binding.cameraButton.visibility = View.VISIBLE
        } else {
            binding.cameraButton.visibility = View.GONE
            requestCameraPermission()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {//granted
                binding.cameraButton.visibility = View.VISIBLE
            } else {//denied
                binding.cameraButton.visibility = View.GONE
                Toast.makeText(
                    requireContext(),
                    "Camera permission denied",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun rotateImageIfNeeded(file: File): File {
        val exif = ExifInterface(file.absolutePath)
        val orientation = exif.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_UNDEFINED
        )

        val rotation = when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90
            ExifInterface.ORIENTATION_ROTATE_180 -> 180
            ExifInterface.ORIENTATION_ROTATE_270 -> 270
            else -> 0
        }
        return if (rotation != 0) {
            val sourceBitmap = BitmapFactory.decodeFile(file.absolutePath)
            val matrix = Matrix().apply { postRotate(rotation.toFloat()) }
            val rotatedBitmap = Bitmap.createBitmap(
                sourceBitmap,
                0,
                0,
                sourceBitmap.width,
                sourceBitmap.height,
                matrix,
                true
            )

            val newFile = createNewFile(file.parentFile!!, "rotated_", ".jpg")
            FileOutputStream(newFile).use {
                rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            }
            newFile
        } else {
            file
        }
    }

    private fun createNewFile(
        parent: File,
        prefix: String,
        suffix: String
    ): File {
        var index = 0
        var file: File
        do {
            index++
            val fileName = "$prefix$index$suffix"
            file = File(parent, fileName)
        } while (file.exists())
        return file
    }

    private fun hasCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private const val CAMERA_PERMISSION_REQUEST_CODE = 100
    }
}