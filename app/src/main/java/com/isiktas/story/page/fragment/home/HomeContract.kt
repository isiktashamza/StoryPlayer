package com.isiktas.story.page.fragment.home

import com.isiktas.story.adapter.StoryRecyclerAdapter
import com.isiktas.story.base.BasePresenter
import com.isiktas.story.base.BaseView
import com.isiktas.story.model.StoriesResponse
import java.io.InputStream

interface HomeContract {
    interface View: BaseView<Presenter>, StoryRecyclerAdapter.StoryClickListener{
        fun storiesRead(storiesResponse: StoriesResponse)
    }

    interface Presenter : BasePresenter {
        fun readStories(inputStream: InputStream)
    }
}