package com.nada.nada.ui

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.nada.nada.R
import com.nada.nada.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val videoView= binding.videoView
        val videoUri = Uri.parse("https://www.nada.org/sites/default/files/media/video/HomePageLoop22.mp4")
        videoView.setVideoURI(videoUri)
        videoView.start()
        videoView.setOnPreparedListener { mediaPlayer ->
            mediaPlayer.isLooping = true
        }

        val imageView =  binding.nadaShowImageview
        val imageUrl = "https://www.nada.org/sites/default/files/media/images/NADA-Show-2025_home-page-static-video-bg_2500x1000.jpg"

        Glide.with(this)
            .load(imageUrl)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.placeholder_image) // Optional placeholder
                    .error(R.drawable.error_image) // Optional error image
            )
            .into(imageView)

        val newImageView = binding.newImageView
        val newImageUrl = "https://www.nada.org/sites/default/files/styles/facebook_small/public/media/images/1%5B40%5D.jpg?h=36848124&itok=tcPALqCU"
        Glide.with(this)
            .load(newImageUrl)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.placeholder_image) // Optional placeholder
                    .error(R.drawable.error_image) // Optional error image
            )
            .into(newImageView)



        val issuesImageView = binding.issuesImageView
        val issuesImageViewImageUrl =
            "https://www.nada.org/sites/default/files/styles/widescreen/public/media/images/capitol-hill-1200x800.jpg?h=10d202d3&itok=_odP0aAC"
        Glide.with(this)
            .load(issuesImageViewImageUrl)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.placeholder_image) // Placeholder image while loading
                    .error(R.drawable.error_image) // Fallback image if loading fails
            )
            .into(issuesImageView)



        val stepUpCareerImageView = binding.stepUpCareerImageView
        val stepUpCareerImageViewImageUrl =
            "https://www.nada.org/sites/default/files/media/images/get-started-bg.jpeg"
        Glide.with(this)
            .load(stepUpCareerImageViewImageUrl)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.placeholder_image) // Placeholder image while loading
                    .error(R.drawable.error_image) // Fallback image if loading fails
            )
            .into(stepUpCareerImageView)

        val upcommingWebinarImageView = binding.upcommingWebinarImageView
        val upcommingWebinarImageUrl =
            "https://www.nada.org/sites/default/files/media/images/IMG_2669.JPG"
        Glide.with(this)
            .load(upcommingWebinarImageUrl)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.placeholder_image) // Placeholder image while loading
                    .error(R.drawable.error_image) // Fallback image if loading fails
            )
            .into(upcommingWebinarImageView)


        val newWebinarImageView = binding.newWebinarImageView
        val newWebinarImageViewImageUrl =
            "https://www.nada.org/sites/default/files/styles/facebook_small/public/media/images/Webinar-APR-3-2024_0.jpg?h=ec041e41&itok=b1Pc2KsR"
        Glide.with(this)
            .load(newWebinarImageViewImageUrl)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.placeholder_image) // Placeholder image while loading
                    .error(R.drawable.error_image) // Fallback image if loading fails
            )
            .into(newWebinarImageView)

        val nadavideoView = binding.nadaShowvideoView
        val ndadvideoUri = Uri.parse("https://www.nada.org/sites/default/files/media/video/NADA%20Show%202025%20Register%20Now.mp4")

        nadavideoView.setVideoURI(ndadvideoUri)
        nadavideoView.setOnPreparedListener { mediaPlayer ->
            mediaPlayer.isLooping = true
        }
        nadavideoView.isFocusable = false
        nadavideoView.isFocusableInTouchMode = false

        nadavideoView.start()

        binding.root.requestFocus()


// Set focus to the root view or another view
        binding.root.requestFocus()




        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}