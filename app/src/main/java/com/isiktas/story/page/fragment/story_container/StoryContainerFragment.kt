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
import com.isiktas.story.animation.CubicTransition
import com.isiktas.story.listener.FragmentChangeListener
import com.isiktas.story.listener.StoryGroupChangeListener
import com.isiktas.story.model.StoriesResponse
import com.isiktas.story.model.StoryList
import com.isiktas.story.page.fragment.story_detail.StoryDetailFragment

class StoryContainerFragment : Fragment(), StoryGroupChangeListener {


    private lateinit var viewPager: ViewPager

    private lateinit var fragmentChangeListener: FragmentChangeListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_story_container, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
        setFragmentChangeListener(activity as FragmentChangeListener)

        populateStories(readStoriesFromArguments())

    }

    private fun initViews(root:View) {
        viewPager = root.findViewById(R.id.story_container_view_pager)

    }

    private fun readStoriesFromArguments() : StoriesResponse {
        return requireArguments().getSerializable("stories") as StoriesResponse
    }
    private fun readCurrentStoryFromArguments() : Int{
         return requireArguments().getInt("current_story", 0)
    }

    private fun setFragmentChangeListener(fragmentChangeListener: FragmentChangeListener) {
        this.fragmentChangeListener = fragmentChangeListener
    }

    class StoryContainerFragmentAdapter(fm: FragmentManager, private val storyList: List<StoryList>, private val callback: StoryGroupChangeListener) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getItem(position: Int): Fragment {
            return StoryDetailFragment(
                storyList.elementAt(position).stories,
                callback
            )
        }

        override fun getCount(): Int {
            return storyList.size
        }

    }

    private fun populateStories(storiesResponse: StoriesResponse) {
        viewPager.offscreenPageLimit = storiesResponse.data.size
        viewPager.adapter =
            StoryContainerFragmentAdapter(
                childFragmentManager,
                storiesResponse.data,
                this
            )
        viewPager.setPageTransformer(false,
            CubicTransition()
        )
        viewPager.currentItem = readCurrentStoryFromArguments()
    }


    override fun onNextStoryGroup() {
        if (viewPager.currentItem != viewPager.childCount - 1){
            viewPager.currentItem = viewPager.currentItem+1
        }
        else {
            fragmentChangeListener.destroyCurrentFragment()
        }
    }

    override fun onPreviousStoryGroup() {
        if (viewPager.currentItem != 0){
            viewPager.currentItem = viewPager.currentItem-1
        }
    }

}