package com.isiktas.story.page.fragment.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.isiktas.story.R
import com.isiktas.story.adapter.StoryRecyclerAdapter
import com.isiktas.story.listener.FragmentChangeListener
import com.isiktas.story.model.StoriesResponse
import com.isiktas.story.page.fragment.story_container.StoryContainerFragment

class HomeFragment : Fragment(), HomeContract.View {

    private var presenter : HomeContract.Presenter? = null
    private lateinit var storyRecyclerView: RecyclerView

    private lateinit var fragmentChangeListener: FragmentChangeListener

    private lateinit var response: StoriesResponse

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
        setFragmentChangeListener(activity as FragmentChangeListener)

        setPresenter(HomePresenter(this))
        presenter?.readStories(requireContext().assets.open("story.json"))
    }

    private fun initViews(root: View) {
        storyRecyclerView = root.findViewById(R.id.home_story_recycler_view)
        storyRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        storyRecyclerView.adapter = StoryRecyclerAdapter(arrayListOf(), requireContext(), this)
    }

    override fun storiesRead(storiesResponse: StoriesResponse) {
        response = storiesResponse
        if (activity != null && requireActivity().isFinishing.not()){
            (storyRecyclerView.adapter as StoryRecyclerAdapter).stories.addAll(storiesResponse.data)
            (storyRecyclerView.adapter as StoryRecyclerAdapter).notifyDataSetChanged()
        }
    }

    override fun setPresenter(presenter: HomeContract.Presenter) {
        this.presenter = presenter
    }

    override fun storyClicked(index: Int) {
        val bundle = Bundle()
        bundle.putInt("current_story", index)
        bundle.putSerializable("stories", response)
        fragmentChangeListener.addFragment(StoryContainerFragment(), bundle)
    }

    private fun setFragmentChangeListener(fragmentChangeListener: FragmentChangeListener) {
        this.fragmentChangeListener = fragmentChangeListener
    }
}