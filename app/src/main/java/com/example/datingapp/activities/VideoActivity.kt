package com.example.datingapp.activities

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.example.datingapp.R
import com.example.datingapp.ui.theme.DatingAppTheme
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


@AndroidEntryPoint
class VideoActivity : ComponentActivity() {

    @ApplicationContext
    @Inject
    lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DatingAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    val context = LocalContext.current
                    val videoUri = "android.resource://${context.packageName}/${R.raw.dating_video}"
                    VideoPlayer(Uri.parse(videoUri))
                }
            }
        }
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent =
                    Intent(applicationContext, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                applicationContext.startActivity(intent)
            }
        })
    }

    @Composable
    fun VideoPlayer(uri: Uri) {
        val context = LocalContext.current
        val playerListener = remember {
            object : Player.Listener {
                override fun onPlaybackStateChanged(state: Int) {
                    if (state == Player.STATE_ENDED) {
                        val intent =
                            Intent(applicationContext, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        applicationContext.startActivity(intent)
                    }
                }
            }
        }

        val exoPlayer = remember {
            ExoPlayer.Builder(context)
                .build()
                .apply {
                    val defaultDataSourceFactory = DefaultDataSource.Factory(context)
                    val dataSourceFactory: DataSource.Factory = DefaultDataSource.Factory(
                        context,
                        defaultDataSourceFactory
                    )
                    val source = ProgressiveMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(MediaItem.fromUri(uri))
                    setMediaSource(source)
                    prepare()
                }
        }

        exoPlayer.addListener(playerListener)
        exoPlayer.playWhenReady = true
        exoPlayer.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING

        DisposableEffect(
            AndroidView(factory = {
                StyledPlayerView(context).apply {
                    hideController()
                    useController = false
                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM

                    player = exoPlayer
                    layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                }
            })
        ) {
            onDispose { exoPlayer.release() }
        }
    }
}
