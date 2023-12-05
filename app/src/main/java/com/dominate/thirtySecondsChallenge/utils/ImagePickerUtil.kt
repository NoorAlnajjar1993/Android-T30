package com.dominate.thirtySecondsChallenge.utils

import androidx.fragment.app.Fragment
import com.github.dhaval2404.imagepicker.ImagePicker


fun Fragment.pickImages(requestCode: Int) {
    ImagePicker.with(this)
        .crop() //Crop image(Optional), Check Customization for more option
        .maxResultSize(
            1080,
            1080
        )    //Final image resolution will be less than 1080 x 1080(Optional)
        .start(requestCode)
}

fun Fragment.captureImage(requestCode: Int) {
    ImagePicker.with(this)
        .crop() //Crop image(Optional), Check Customization for more option
        .cameraOnly()
             //Final image size will be less than 1 MB(Optional)
        .maxResultSize(
            1080,
            1080
        )    //Final image resolution will be less than 1080 x 1080(Optional)
        .start(requestCode)
}