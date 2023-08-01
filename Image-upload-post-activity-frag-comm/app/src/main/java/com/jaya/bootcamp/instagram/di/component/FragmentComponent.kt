package com.jaya.bootcamp.instagram.di.component

import com.jaya.bootcamp.instagram.di.FragmentScope
import com.jaya.bootcamp.instagram.di.module.FragmentModule
import com.jaya.bootcamp.instagram.ui.dummies.DummiesFragment
import com.jaya.bootcamp.instagram.ui.home.HomeFragment
import com.jaya.bootcamp.instagram.ui.photo.PhotoFragment
import com.jaya.bootcamp.instagram.ui.profile.ProfileFragment
import dagger.Component

@FragmentScope
@Component(
    dependencies = [ApplicationComponent::class],
    modules = [FragmentModule::class]
)
interface FragmentComponent {

    fun inject(fragment: DummiesFragment)

    fun inject(fragment: ProfileFragment)

    fun inject(fragment: PhotoFragment)

    fun inject(fragment: HomeFragment)
}