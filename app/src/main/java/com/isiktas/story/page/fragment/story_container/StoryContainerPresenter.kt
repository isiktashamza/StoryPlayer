package com.isiktas.story.page.fragment.story_container

import android.util.Log
import com.google.gson.Gson
import com.isiktas.story.model.StoriesResponse
import org.json.JSONObject
import java.io.InputStream

class StoryContainerPresenter (
    view: StoryContainerContract.View
) : StoryContainerContract.Presenter {

    private var view: StoryContainerContract.View? = view

    override fun readStories(inputStream: InputStream) {
        val storyContainerRepository = StoryContainerRepository(inputStream)
        val storiesJSON = storyContainerRepository.readStories()
        val storiesResponse = createStoryResponse(storiesJSON)
        view?.storiesRead(storiesResponse)
    }

    private fun createStoryResponse(storyJSON: JSONObject) : StoriesResponse {
        return Gson().fromJson<StoriesResponse>(storyJSON.toString(), StoriesResponse::class.java)
    }

    override fun onStart() {
    }

    override fun onPause() {
    }

    override fun onDestroy() {
        view = null
    }

}