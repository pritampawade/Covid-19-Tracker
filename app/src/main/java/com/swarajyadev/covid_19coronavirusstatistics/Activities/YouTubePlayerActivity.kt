package com.swarajyadev.covid_19coronavirusstatistics.Activities

import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView
import com.swarajyadev.covid_19coronavirusstatistics.API.API_Utils
import com.swarajyadev.covid_19coronavirusstatistics.R
import java.util.regex.Pattern

class YouTubePlayerActivity : YouTubeBaseActivity() {
    var player: YouTubePlayerView? = null
    var video: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_you_tube_player)
        video = getYouTubeId(intent.getStringExtra("video"))
        player = findViewById(R.id.player)
        player!!.initialize(API_Utils.youtube, object : YouTubePlayer.OnInitializedListener {
            override fun onInitializationSuccess(
                provider: YouTubePlayer.Provider,
                youTubePlayer: YouTubePlayer,
                b: Boolean
            ) {
                if (!b) {
                    youTubePlayer.loadVideo(video)
                    youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT)
                }
            }

            override fun onInitializationFailure(
                provider: YouTubePlayer.Provider,
                youTubeInitializationResult: YouTubeInitializationResult
            ) {
                Toast.makeText(
                    this@YouTubePlayerActivity,
                    youTubeInitializationResult.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun getYouTubeId(youTubeUrl: String): String {
        val pattern = "(?<=youtu.be/|watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*"
        val compiledPattern = Pattern.compile(pattern)
        val matcher = compiledPattern.matcher(youTubeUrl)
        return if (matcher.find()) {
            matcher.group()
        } else {
            "error"
        }
    }
}