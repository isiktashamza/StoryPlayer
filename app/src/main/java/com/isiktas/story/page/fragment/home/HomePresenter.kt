package com.isiktas.story.page.fragment.home

import com.google.gson.Gson
import com.isiktas.story.model.StoriesResponse
import org.json.JSONObject
import java.io.InputStream

class HomePresenter (
    view: HomeContract.View
) : HomeContract.Presenter {

    private var view: HomeContract.View? = view

    override fun readStories(inputStream: InputStream) {
        val homeRepository = HomeRepository(inputStream)
        val storiesJSON = homeRepository.readStories()
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