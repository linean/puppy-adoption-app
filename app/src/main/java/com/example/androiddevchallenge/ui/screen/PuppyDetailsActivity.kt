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
@file:Suppress("FunctionName")
@file:OptIn(
    ExperimentalCoilApi::class,
    ExperimentalAnimationApi::class,
    ExperimentalFoundationApi::class,
    ExperimentalSerializationApi::class
)

package com.example.androiddevchallenge.ui.screen

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.androiddevchallenge.R
import com.example.androiddevchallenge.data.PreviewPuppy
import com.example.androiddevchallenge.data.Puppy
import com.example.androiddevchallenge.ui.theme.MainTheme
import com.example.androiddevchallenge.ui.theme.typography
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class PuppyDetailsActivity : AppCompatActivity() {

    private val puppy by lazy {
        intent.getStringExtra(EXTRA_PUPPY)
            ?.let { Json.decodeFromString<Puppy>(it) }
            ?: error("Where is my puppy?!")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        title = puppy.name

        setContent {
            PuppyDetailsScreen(
                puppy = puppy,
                onCallClick = ::onCallClick
            )
        }
    }

    private fun onCallClick() {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:${puppy.contactNumber}")
        }
        startActivity(intent)
    }

    companion object {
        private const val EXTRA_PUPPY = "extra_puppy"

        fun start(activity: Activity, puppy: Puppy) {
            val intent = Intent(activity, PuppyDetailsActivity::class.java).apply {
                putExtra(EXTRA_PUPPY, Json.encodeToString(puppy))
            }

            activity.startActivity(intent)
        }
    }
}

@Composable
private fun PuppyDetailsScreen(
    puppy: Puppy,
    onCallClick: () -> Unit
) {
    MainTheme {
        Surface(color = MaterialTheme.colors.background) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
            ) {

                PuppyImage(puppy)
                PuppyDetails(puppy)
            }

            CallButton(onCallClick = onCallClick)
        }
    }
}

@Composable
private fun PuppyImage(puppy: Puppy) {
    Image(
        painter = rememberImagePainter(puppy.image),
        contentDescription = "",
        modifier = Modifier.fillMaxWidth().height(200.dp),
        contentScale = ContentScale.FillWidth
    )
}

@Composable
private fun PuppyDetails(puppy: Puppy) {
    Column {
        Text(
            text = stringResource(R.string.puppy_details_screen_about_me_header),
            style = typography.h6,
            modifier = Modifier
                .padding(top = 16.dp)
                .padding(horizontal = 16.dp)
        )

        Text(
            text = puppy.longDescription,
            style = typography.body1,
            modifier = Modifier
                .padding(top = 8.dp)
                .padding(horizontal = 16.dp)
        )

        Text(
            text = stringResource(R.string.puppy_details_screen_owner_header),
            style = typography.h6,
            modifier = Modifier
                .padding(top = 16.dp)
                .padding(horizontal = 16.dp)
        )

        Text(
            text = puppy.preferredOwnerDescription,
            style = typography.body1,
            modifier = Modifier
                .padding(top = 8.dp, bottom = 96.dp)
                .padding(horizontal = 16.dp)
        )
    }
}

@Composable
private fun CallButton(
    onCallClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {
        FloatingActionButton(
            onClick = { onCallClick() },
            modifier = Modifier.padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_phone),
                contentDescription = null
            )
        }
    }
}

@Preview(name = "PuppyDetails", widthDp = 320, heightDp = 500)
@Composable
private fun PreviewPuppyDetails() {
    PuppyDetails(puppy = PreviewPuppy())
}

@Preview(name = "CallButton", widthDp = 320, heightDp = 320)
@Composable
private fun PreviewCallButton() {
    CallButton(onCallClick = {})
}
