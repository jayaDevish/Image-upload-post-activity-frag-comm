package com.jaya.bootcamp.instagram.ui.profile

import android.os.Bundle
import android.view.View
import com.jaya.bootcamp.instagram.R
import com.jaya.bootcamp.instagram.di.component.FragmentComponent
import com.jaya.bootcamp.instagram.ui.base.BaseFragment

class ProfileFragment : BaseFragment<ProfileViewModel>() {

    companion object {

        const val TAG = "ProfileFragment"

        fun newInstance(): ProfileFragment {
            val args = Bundle()
            val fragment = ProfileFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun provideLayoutId(): Int = R.layout.fragment_profile

    override fun injectDependencies(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    override fun setupObservers() {
        super.setupObservers()

    }

    override fun setupView(view: View) {

    }

}