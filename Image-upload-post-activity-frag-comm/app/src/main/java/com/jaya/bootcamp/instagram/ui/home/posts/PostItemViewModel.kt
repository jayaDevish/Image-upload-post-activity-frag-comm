package com.jaya.bootcamp.instagram.ui.home.posts

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.jaya.bootcamp.instagram.R
import com.jaya.bootcamp.instagram.data.model.Image
import com.jaya.bootcamp.instagram.data.model.Post
import com.jaya.bootcamp.instagram.data.remote.Networking
import com.jaya.bootcamp.instagram.data.repository.PostRepository
import com.jaya.bootcamp.instagram.data.repository.UserRepository
import com.jaya.bootcamp.instagram.ui.base.BaseItemViewModel
import com.jaya.bootcamp.instagram.utils.common.Resource
import com.jaya.bootcamp.instagram.utils.common.TimeUtils
import com.jaya.bootcamp.instagram.utils.display.ScreenUtils
import com.jaya.bootcamp.instagram.utils.log.Logger
import com.jaya.bootcamp.instagram.utils.network.NetworkHelper
import com.jaya.bootcamp.instagram.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class PostItemViewModel @Inject constructor(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    userRepository: UserRepository,
    private val postRepository: PostRepository
) : BaseItemViewModel<Post>(schedulerProvider, compositeDisposable, networkHelper) {

    companion object {
        const val TAG = "PostItemViewModel"
    }

    private val user = userRepository.getCurrentUser()!!
    private val screenWidth = ScreenUtils.getScreenWidth()
    private val screenHeight = ScreenUtils.getScreenHeight()
    private val headers = mapOf(
        Pair(Networking.HEADER_API_KEY, Networking.API_KEY),
        Pair(Networking.HEADER_USER_ID, user.id),
        Pair(Networking.HEADER_ACCESS_TOKEN, user.accessToken)
    )

    val name: LiveData<String> = Transformations.map(data) { it.creator.name }
    val postTime: LiveData<String> = Transformations.map(data) { TimeUtils.getTimeAgo(it.createdAt) }
    val likesCount: LiveData<Int> = Transformations.map(data) { it.likedBy?.size ?: 0 }
    val isLiked: LiveData<Boolean> = Transformations.map(data) {
        it.likedBy?.find { postUser -> postUser.id == user.id } !== null
    }

    val profileImage: LiveData<Image> = Transformations.map(data) {
        it.creator.profilePicUrl?.run { Image(this, headers) }
    }

    val imageDetail: LiveData<Image> = Transformations.map(data) {
        Image(
            it.imageUrl,
            headers,
            screenWidth,
            it.imageHeight?.let { height ->
                return@let (calculateScaleFactor(it) * height).toInt()
            } ?: screenHeight / 3)
    }

    override fun onCreate() {
        Logger.d(TAG, "onCreate called")
    }

    private fun calculateScaleFactor(post: Post) =
        post.imageWidth?.let { return@let screenWidth.toFloat() / it } ?: 1f

    fun onLikeClick() = data.value?.let {
        if (networkHelper.isNetworkConnected()) {
            val api =
                if (isLiked.value == true)
                    postRepository.makeUnlikePost(it, user)
                else
                    postRepository.makeLikePost(it, user)

            compositeDisposable.add(api
                .subscribeOn(schedulerProvider.io())
                .subscribe(
                    { responsePost -> if (responsePost.id == it.id) updateData(responsePost) },
                    { error -> handleNetworkError(error) }
                )
            )
        } else {
            messageStringId.postValue(Resource.error(R.string.network_connection_error))
        }
    }

}