package com.isiktas.story.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class StoriesResponse(

    @SerializedName("data")
    val data: List<StoryList>
) : Serializable


data class StoryList(
    @SerializedName("stories")
    val stories: List<Story>
)

data class Story(

    @SerializedName("story_id")
    val storyId: String,

    @SerializedName("username")
    val username: String,

    @SerializedName("story_time")
    val story_time: String,

    @SerializedName("url")
    val url: String,

    @SerializedName("content_type")
    val contentType: Int,

    @SerializedName("user_pp")
    val userPP: String


)