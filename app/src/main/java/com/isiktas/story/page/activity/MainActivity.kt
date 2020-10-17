package com.isiktas.story.page.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.isiktas.story.R
import com.isiktas.story.listener.FragmentChangeListener
import com.isiktas.story.page.fragment.home.HomeFragment
import com.isiktas.story.page.fragment.story_container.StoryContainerFragment

class MainActivity : AppCompatActivity(), FragmentChangeListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .replace(R.id.main_activity_container,
                HomeFragment()
            ).commit()

    }

    override fun addFragment(fragment: Fragment, bundle: Bundle?) {
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction().addToBackStack(fragment.javaClass.name)
            .add(R.id.main_activity_container, fragment).commit()
    }

    override fun destroyCurrentFragment() {
        if (supportFragmentManager.backStackEntryCount > 0){
            supportFragmentManager.popBackStackImmediate()
        }
    }
}