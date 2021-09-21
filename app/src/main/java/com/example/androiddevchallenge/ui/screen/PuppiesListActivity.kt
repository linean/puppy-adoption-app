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

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Card
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.androiddevchallenge.R
import com.example.androiddevchallenge.data.PreviewPuppy
import com.example.androiddevchallenge.data.Puppy
import com.example.androiddevchallenge.ui.theme.MainTheme
import com.example.androiddevchallenge.ui.theme.typography
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class PuppiesListActivity : AppCompatActivity() {

    private val puppies = MutableLiveData<List<Puppy>>(emptyList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadPuppies()

        setContent {
            PuppiesListScreen(puppies, ::onPuppyClick)
        }
    }

    private fun loadPuppies() {
        lifecycleScope.launch(Dispatchers.IO) {
            val puppiesJson = resources
                .openRawResource(R.raw.puppies)
                .use { it.reader().readText() }

            puppies.postValue(Json.decodeFromString(puppiesJson))
        }
    }

    private fun onPuppyClick(puppy: Puppy) {
        PuppyDetailsActivity.start(this, puppy)
    }
}

@Composable
private fun PuppiesListScreen(
    puppiesLiveData: LiveData<List<Puppy>>,
    onPuppyClick: (Puppy) -> Unit
) {
    val puppies by puppiesLiveData.observeAsState()

    MainTheme {
        Surface(color = MaterialTheme.colors.background) {
            PuppiesList(puppies.orEmpty(), onPuppyClick)
        }
    }
}

@Composable
private fun PuppiesList(
    puppies: List<Puppy>,
    onPuppyClick: (Puppy) -> Unit
) {
    val listState = rememberLazyListState()

    LazyVerticalGrid(
        state = listState,
        cells = GridCells.Fixed(2),
        contentPadding = PaddingValues(bottom = 72.dp)
    ) {
        items(puppies) { puppy ->
            PuppyListItem(
                puppy = puppy,
                onPuppyClick = onPuppyClick
            )
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {
        val showButton = listState.firstVisibleItemIndex > 0
        val coroutineScope = rememberCoroutineScope()

        AnimatedVisibility(visible = showButton) {
            ScrollToTopButton(
                onClick = {
                    coroutineScope.launch {
                        listState.animateScrollToItem(0)
                    }
                }
            )
        }
    }
}

@Composable
private fun PuppyListItem(
    puppy: Puppy,
    onPuppyClick: (Puppy) -> Unit = {}
) {
    Card(
        elevation = 2.dp,
        modifier = Modifier
            .padding(16.dp)
            .clickable { onPuppyClick(puppy) }
    ) {
        Column {
            PuppyImage(puppy)
            PuppyDetails(puppy)
        }
    }
}

@Composable
private fun PuppyImage(puppy: Puppy) {
    Image(
        painter = rememberImagePainter(puppy.image),
        contentDescription = null,
        modifier = Modifier.height(160.dp),
        contentScale = ContentScale.Crop
    )
}

@Composable
private fun PuppyDetails(puppy: Puppy) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded }
    ) {
        Text(
            text = puppy.name,
            style = typography.h6,
            modifier = Modifier
                .padding(8.dp)
                .weight(1F)
                .wrapContentWidth(Alignment.Start),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        val rotation by animateFloatAsState(if (expanded) 180F else 0F)

        Image(
            painter = painterResource(id = R.drawable.ic_arrow_drop_down),
            contentDescription = null,
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .wrapContentWidth(Alignment.End)
                .rotate(rotation)
        )
    }

    AnimatedVisibility(visible = expanded) {
        Column(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .padding(bottom = 8.dp)
        ) {
            Text(text = puppy.shortDescription)
        }
    }
}

@Composable
private fun ScrollToTopButton(
    onClick: () -> Unit
) {
    FloatingActionButton(
        onClick = { onClick() },
        modifier = Modifier.padding(16.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_arrow_upward),
            contentDescription = ""
        )
    }
}


@Preview("Puppy list item", widthDp = 200, heightDp = 250)
@Composable
private fun PreviewPuppyListItem() {
    PuppyListItem(puppy = PreviewPuppy())
}
