package com.isiktas.story.page.fragment.story_container

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.isiktas.story.R
import com.isiktas.story.listener.StoryGroupChangeListener
import com.isiktas.story.model.StoriesResponse
import com.isiktas.story.model.StoryList
import com.isiktas.story.page.fragment.StoryDetailFragment
import kotlin.math.abs

class StoryContainerFragment : Fragment(), StoryContainerContract.View {

    private var presenter: StoryContainerContract.Presenter? = null

    private lateinit var viewPager: ViewPager


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_story_container, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPager = view.findViewById(R.id.story_container_view_pager)

        setPresenter(StoryContainerPresenter(this))
        presenter?.readStories(requireContext().assets.open("story.json"))


    }


    class StoryContainerFragmentAdapter(fm: FragmentManager, private val storyList: List<StoryList>, private val callback: StoryGroupChangeListener) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getItem(position: Int): Fragment {
            return StoryDetailFragment(storyList.elementAt(position).stories, callback)
        }

        override fun getCount(): Int {
            return storyList.size
        }

    }

    class CubeOutAnimation : ViewPager.PageTransformer {
        override fun transformPage(page: View, position: Float) {
            when {
                position < -1 -> {
                    page.alpha = 0f
                }
                position <= 0 -> {
                    page.alpha = 1f
                    page.pivotX = page.width.toFloat()
                    page.pivotY = page.height.toFloat()/2
                    page.rotationY = -45* abs(position)
                }
                position <= 1 -> {
                    page.alpha = 1f
                    page.pivotX = 0f
                    page.pivotY = page.height.toFloat()/2
                    page.rotationY = 45*abs(position)
                }
                else -> {
                    page.alpha = 0f
                }
            }

        }

    }

    override fun storiesRead(storiesResponse: StoriesResponse) {
        viewPager.offscreenPageLimit = storiesResponse.data.size
        viewPager.adapter =
            StoryContainerFragmentAdapter(
                childFragmentManager,
                storiesResponse.data,
                this
            )
        viewPager.setPageTransformer(false,
            CubeOutAnimation()
        )
    }

    override fun setPresenter(presenter: StoryContainerContract.Presenter) {
        this.presenter = presenter
    }

    override fun onNextStoryGroup() {
        if (viewPager.currentItem != viewPager.childCount - 1) viewPager.currentItem = viewPager.currentItem+1
    }

    override fun onPreviousStoryGroup() {
        if (viewPager.currentItem != 0) viewPager.currentItem = viewPager.currentItem-1
    }

}