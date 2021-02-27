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
package com.example.androiddevchallenge.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Puppy(
    @SerialName("image") val image: String,
    @SerialName("name") val name: String,
    @SerialName("short_description") val shortDescription: String,
    @SerialName("long_description") val longDescription: String,
    @SerialName("preferred_owner_description") val preferredOwnerDescription: String,
    @SerialName("contact_number") val contactNumber: String
)

@Suppress("FunctionName")
fun PreviewPuppy() = Puppy(
    image = "https://images.dog.ceo/breeds/terrier-russell/jack1.jpg",
    name = "Preview",
    shortDescription = "• lorem ipsum dolor\n• sit amet\n•consectetur adipiscing",
    longDescription = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec molestie malesuada ante id commodo.",
    preferredOwnerDescription = "Donec efficitur ac ex sed tempus. Donec id iaculis nulla, eget mattis sapien. Curabitur congue egestas turpis, sit amet pharetra odio porta eu. Nullam ut sem a ex rhoncus ornare.",
    contactNumber = "123456789"
)
