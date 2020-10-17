package com.isiktas.story.base

interface BaseView<T : BasePresenter> {
    fun setPresenter(presenter: T)
}