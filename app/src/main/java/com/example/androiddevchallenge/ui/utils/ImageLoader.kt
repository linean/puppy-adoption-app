/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge.ui.utils

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target

interface ImageLoader {

    @Composable
    fun loadImage(
        url: String,
        @DrawableRes
        placeholderRes: Int
    ): Painter
}

object PreviewImageLoader : ImageLoader {

    @Composable
    override fun loadImage(
        url: String,
        placeholderRes: Int
    ): Painter {
        return painterResource(id = placeholderRes)
    }
}

object RemoteImageLoader : ImageLoader {

    @Composable
    override fun loadImage(
        url: String,
        placeholderRes: Int
    ): Painter {
        val placeholder = painterResource(id = placeholderRes)
        var painter by remember { mutableStateOf(placeholder) }

        LaunchedEffect(key1 = url) {
            val target = object : Target {
                override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom?) {
                    painter = BitmapPainter(bitmap.asImageBitmap())
                }

                override fun onBitmapFailed(
                    error: java.lang.Exception?,
                    errorDrawable: Drawable?
                ) = Unit

                override fun onPrepareLoad(
                    placeHolderDrawable: Drawable?
                ) = Unit
            }

            Picasso
                .get()
                .load(url)
                .into(target)
        }

        return painter
    }
}
