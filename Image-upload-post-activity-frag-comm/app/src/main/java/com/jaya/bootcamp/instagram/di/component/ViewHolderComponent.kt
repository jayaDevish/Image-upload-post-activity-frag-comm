package com.jaya.bootcamp.instagram.di.component

import com.jaya.bootcamp.instagram.di.ViewModelScope
import com.jaya.bootcamp.instagram.di.module.ViewHolderModule
import com.jaya.bootcamp.instagram.ui.dummies.DummyItemViewHolder
import com.jaya.bootcamp.instagram.ui.home.posts.PostItemViewHolder
import dagger.Component

@ViewModelScope
@Component(
    dependencies = [ApplicationComponent::class],
    modules = [ViewHolderModule::class]
)
interface ViewHolderComponent {

    fun inject(viewHolder: DummyItemViewHolder)

    fun inject(viewHolder: PostItemViewHolder)
}