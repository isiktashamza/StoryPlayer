package com.isiktas.story.page.fragment

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.LayerDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.isiktas.story.R
import com.isiktas.story.listener.StoryGroupChangeListener
import com.isiktas.story.model.Story
import de.hdodenhof.circleimageview.CircleImageView
import java.lang.Exception

class StoryDetailFragment(private val stories: List<Story>, private val storyGroupChangeListener: StoryGroupChangeListener) : Fragment() {

    private lateinit var touchHandler: ConstraintLayout
    private lateinit var image: ImageView
    private lateinit var video: VideoView
    private lateinit var progressContainer: LinearLayout
    private lateinit var userProfilePicture: CircleImageView
    private lateinit var username: TextView
    private lateinit var time: TextView

    private val displayMetrics = DisplayMetrics()

    private val handler = Handler()
    private var thread: Thread? = null
    private var progressStatus = 0

    private var progressBarList = ArrayList<ProgressBar>(stories.size)
    private var currentStory = 0

    private var isStoryHeld = false
    private var stopPosition = 0
    private var oneSecRunnable = Runnable {
        isStoryHeld = true
        holdStory()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_story_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
        getDisplayMetrics()
        setListeners()
        initProgressBars()
        addProgressBarsToContainer()
    }

    override fun onResume() {
        super.onResume()
        populateViews()
    }

    override fun onPause() {
        super.onPause()
        resetFields()
    }

    private fun initViews(root: View) {
        touchHandler = root.findViewById(R.id.story_detail_touch_handler)
        image = root.findViewById(R.id.story_detail_image)
        video = root.findViewById(R.id.story_detail_video)
        progressContainer = root.findViewById(R.id.progress_bar_container)
        userProfilePicture = root.findViewById(R.id.story_detail_profile_picture)
        username = root.findViewById(R.id.story_detail_username)
        time = root.findViewById(R.id.story_detail_time)
    }

    private fun getDisplayMetrics() {
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setListeners() {
        touchHandler.setOnTouchListener { _, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    handler.postDelayed(oneSecRunnable, 200)
                }
                MotionEvent.ACTION_UP -> {
                    if (isStoryHeld){
                        resumeStory()
                    }
                    else {
                        handler.removeCallbacks(oneSecRunnable)
                        changeStory(motionEvent.x)

                    }
                    isStoryHeld = false

                }
            }
            true
        }
    }

    private fun populateViews() {
        thread = null
        Glide.with(requireContext()).load(stories.elementAt(currentStory).userPP).centerCrop()
            .into(userProfilePicture)
        username.text = stories.elementAt(currentStory).username
        time.text = stories.elementAt(currentStory).story_time

        if (stories.elementAt(currentStory).contentType == 0) {
            handleImage()
        } else {
            handleVideo()
        }
    }

    private fun initProgressBars() {
        val width =
            displayMetrics.widthPixels - displayMetrics.density * 40 - 10 * (stories.size - 1)
        val layoutParams = FrameLayout.LayoutParams(
            width.toInt() / stories.size,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        layoutParams.marginEnd = 5
        layoutParams.marginStart = 5
        for (x in 1..stories.size) {
            val progressBar = ProgressBar(requireContext(), null, R.attr.progressBarStyle)
            progressBar.progressDrawable =
                ContextCompat.getDrawable(requireContext(), R.drawable.progress_drawable)
            progressBar.layoutParams = layoutParams
            progressBar.isIndeterminate = false
            progressBar.progress = 0
            progressBar.secondaryProgress = 0
            progressBar.max = 100
            progressBarList.add(progressBar)
        }
    }

    private fun addProgressBarsToContainer() {
        for (x in stories.indices) {
            progressContainer.addView(progressBarList.elementAt(x))
        }
    }

    private fun handleImage() {
        video.visibility = View.GONE
        image.visibility = View.VISIBLE
        Glide.with(requireContext()).load(stories.elementAt(currentStory).url).into(image)

        startThread()
    }

    private fun handleVideo() {
        image.visibility = View.GONE
        video.visibility = View.VISIBLE
        video.setVideoURI(Uri.parse(stories.elementAt(currentStory).url))
        video.requestFocus()
        video.start()

        startThread()
    }

    private fun nextStory() {
        if (currentStory != stories.size - 1) {
            (progressContainer.getChildAt(currentStory) as ProgressBar).secondaryProgress = 100
            currentStory++
            resetFields()
            populateViews()
        } else {
            storyGroupChangeListener.onNextStoryGroup()
        }
    }

    private fun previousStory() {
        if (currentStory != 0) {
            (progressContainer.getChildAt(currentStory) as ProgressBar).secondaryProgress = 0
            currentStory--
            resetFields()
            populateViews()
        } else {
            storyGroupChangeListener.onPreviousStoryGroup()

        }
    }

    private fun holdStory() {
        thread = null
        if (stories.elementAt(currentStory).contentType == 1){
            video.pause()
        }
    }

    private fun resumeStory() {
        startThread()
        if (stories.elementAt(currentStory).contentType == 1){
            video.start()
        }
    }

    private fun changeStory(x: Float) {
        val width = displayMetrics.widthPixels
        if (x > width / 2) {
            nextStory()
        } else {
            previousStory()
        }
    }

    private fun resetFields() {
        thread = null
        video.visibility = View.GONE
        image.visibility = View.GONE
        isStoryHeld = false
        progressStatus = 0
    }

    private fun startThread() {
        thread = Thread(Runnable {
            while (progressStatus <= 100) {
                if (thread != null && thread!!.isInterrupted.not()) {
                    if (stories.elementAt(currentStory).contentType == 0) {
                        progressStatus += 1

                    } else {
                        progressStatus = 100 * video.currentPosition / video.duration
                    }
                    handler.post {
                        (progressContainer.getChildAt(currentStory) as ProgressBar).secondaryProgress =
                            progressStatus
                    }
                    try {
                        Thread.sleep(50)
                    } catch (e: Exception) {
                        e.printStackTrace() //TODO: handle exception
                    }
                } else {
                    break
                }
            }
            handler.post {
                if (progressStatus >= 100) nextStory()
            }
        })
        thread!!.start()
    }
}