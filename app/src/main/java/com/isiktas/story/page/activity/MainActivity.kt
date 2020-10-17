package com.isiktas.story.page.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.isiktas.story.R
import com.isiktas.story.page.fragment.story_container.StoryContainerFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .replace(R.id.main_activity_container,
                StoryContainerFragment()
            ).commit()

    }
}