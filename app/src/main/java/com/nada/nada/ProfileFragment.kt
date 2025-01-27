package com.nada.nada

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.nada.nada.databinding.FragmentProfileBinding
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.Auth0
import com.auth0.android.result.UserProfile
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.callback.Callback



class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    // Assuming you have an Auth0 instance already initialized
    private lateinit var auth0: Auth0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment using ViewBinding
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        auth0 = Auth0(
            getString(R.string.com_auth0_client_id),
            getString(R.string.com_auth0_domain)
        )
        displayUsername()

        return binding.root
    }

    private fun displayUsername() {
        val client = AuthenticationAPIClient(auth0)

        // Assuming you have an access token
        val accessToken = ""
        // Call to get user info
        client.userInfo(accessToken).start(object : Callback<UserProfile, AuthenticationException> {
            override fun onSuccess(result: UserProfile) {
                // Retrieve username or set a fallback if not found
                val username = result.name ?: "Guest"
                // Set username to the TextView
                binding.usernameTextView.text = username
            }

            override fun onFailure(error: AuthenticationException) {
                // Handle the error if user info retrieval fails
                Toast.makeText(requireContext(), "Error retrieving user info: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Clean up the binding when the view is destroyed to prevent memory leaks
        _binding = null
    }
}
