package com.francotte.musicplayer4.di

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.francotte.musicplayer4.R
import com.francotte.musicplayer4.exoplayer.MusicServiceConnection
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideProvideServiceConnection(@ApplicationContext context : Context) = MusicServiceConnection(context)

    @Provides
    @Singleton
    fun provideGlideInstance(
       @ApplicationContext context : Context
    ) = Glide.with(context).setDefaultRequestOptions(
        RequestOptions()
            .placeholder(R.drawable.image_cd)
            .error(R.drawable.image_cd)
            .diskCacheStrategy(DiskCacheStrategy.DATA)
    )
}