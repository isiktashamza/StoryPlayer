package com.isiktas.story.page.fragment.story_container

import com.isiktas.story.base.BasePresenter
import com.isiktas.story.base.BaseView
import com.isiktas.story.listener.StoryGroupChangeListener
import com.isiktas.story.model.StoriesResponse
import java.io.InputStream

interface StoryContainerContract {

    interface View: BaseView<Presenter>, StoryGroupChangeListener {
        fun storiesRead(storiesResponse: StoriesResponse)
    }

    interface Presenter : BasePresenter {
        fun readStories(inputStream: InputStream)
    }
}