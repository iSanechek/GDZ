package com.isanechek.gdz.ui.fragments.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.isanechek.gdz.GdzApplication

/**
 * Created by isanechek on 11/29/17.
 */
abstract class BaseFragment : Fragment() {

    val router = GdzApplication.instance.router

    abstract fun fragmentView() : View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            fragmentView()
}