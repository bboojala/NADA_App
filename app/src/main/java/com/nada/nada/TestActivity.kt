package com.nada.nada

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.nada.nada.databinding.ActivityTestBinding
import okhttp3.MediaType.Companion.toMediaType
import org.json.JSONObject
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.authentication.storage.CredentialsManager
import com.auth0.android.authentication.storage.SharedPreferencesStorage
import com.auth0.android.callback.Callback
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.result.Credentials

class TestActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityTestBinding
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private val oktaApiUrl = "https://dev-st66scp6rrhrbker.okta.com/api/v1/authn"
    private lateinit var account: Auth0
    private var appJustLaunched = true
    private var userIsAuthenticated = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        account = Auth0(
            getString(R.string.com_auth0_client_id),
            getString(R.string.com_auth0_domain)
        )
        setSupportActionBar(binding.appBarTest.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_test)

        // Passing each menu ID as a set of Ids because each menu should be considered as top-level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nadaEducationFragment, R.id.newsFragment,
                R.id.showsEventsFragment, R.id.researchDataFragment,
                R.id.membershipFragment, R.id.aboutNadaFragment,R.id.profileFragment,R.id.settingsFragment
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


        navView.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            navController.popBackStack() // Clear the current fragment stack
            navController.navigate(menuItem.itemId) // Navigate to the selected fragment
            drawerLayout.closeDrawers()
            true
        }
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                handleBackPress()
            }
        })
    }

    private fun handleBackPress() {
        val drawerLayout: DrawerLayout = binding.drawerLayout
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            showExitConfirmation()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
            showExitConfirmation()
    }

    private fun showExitConfirmation() {
        AlertDialog.Builder(this)
            .setTitle("Exit App")
            .setMessage("Are you sure you want to exit?")
            .setPositiveButton("Yes") { _, _ ->
                finish() // Close the app
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss() // Close the dialog
            }
            .show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
       
        menuInflater.inflate(R.menu.test, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.loginItem -> {
                openBottomLoginSheetDialog()
                true
            }
            R.id.createAccountItem -> {
                openBottomSignUpSheetDialog()
                true
            }
            R.id.logoutItem -> {
                val auth0 = Auth0(
                    getString(R.string.com_auth0_client_id),
                    getString(R.string.com_auth0_domain))
                logoutFromApp(auth0, this, findViewById(android.R.id.content))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_test)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun openBottomLoginSheetDialog() {
        val bottomSheetView =
            LayoutInflater.from(this).inflate(R.layout.login_bottom_sheet, null)

        val bottomSheetDialog = BottomSheetDialog(this)
        val email: EditText = bottomSheetView.findViewById(R.id.usernameInput)
        val password: EditText = bottomSheetView.findViewById(R.id.passwordInput)
        val submit: Button = bottomSheetView.findViewById(R.id.loginButton)

        emailEditText = email
        passwordEditText = password

        submit.setOnClickListener {
            Log.d("LoginButton", "Button clicked")
            val emailText = emailEditText.text.toString().trim()
            val passwordText = passwordEditText.text.toString().trim()

            if (emailText.isNotEmpty() && passwordText.isNotEmpty()) {
                Log.d("LoginButton", "Attempting to authenticate")

                val auth0 = Auth0(
                    getString(R.string.com_auth0_client_id),
                    getString(R.string.com_auth0_domain))

                AuthenticationAPIClient(auth0)
                    .login(emailText, passwordText, "Username-Password-Authentication")
                    .setScope("openid profile email")
                    .start(object : Callback<Credentials, AuthenticationException> {
                        override fun onSuccess(credentials: Credentials) {
                            // Authentication successful
                            val accessToken = credentials.accessToken
                            val idToken = credentials.idToken
                            val expiresAt = credentials.expiresAt
                            userIsAuthenticated = true
                            Log.d("Auth0", "Login successful! ID accessToken: $accessToken")
                            Log.d("Auth0", "Login successful! ID expiresAt: $expiresAt")
                            Log.d("Auth0", "Login successful! ID Token: $idToken")  // Log successful login with ID token
                            bottomSheetDialog.dismiss()
                            showSnackBar("$idToken,$accessToken")
                            //showSnackBar(getString(R.string.login_success_message))
                        }

                        override fun onFailure(exception: AuthenticationException) {
                            Log.e("AuthError", "Error: ${exception.message}")
                            Log.e("AuthError", "Details: ${exception.getDescription()}")
                        }

                    })



                //authenticateWithOkta(email, password,bottomSheetDialog)
            } else {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
        }


        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.setOnShowListener { dialog ->
            val bottomSheet = (dialog as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout?
            bottomSheet?.let {
                it.setBackgroundColor(Color.TRANSPARENT) // Ensure transparency if needed
                val behavior = BottomSheetBehavior.from(it)

                // Remove fixed height, make it wrap_content
                val layoutParams = it.layoutParams as CoordinatorLayout.LayoutParams
                layoutParams.height = CoordinatorLayout.LayoutParams.WRAP_CONTENT
                it.layoutParams = layoutParams

                behavior.state = BottomSheetBehavior.STATE_EXPANDED

                // Handle behavior to adjust bottom sheet when keyboard is shown
                bottomSheetView.viewTreeObserver.addOnGlobalLayoutListener {
                    val rect = Rect()
                    bottomSheetView.getWindowVisibleDisplayFrame(rect)
                    val screenHeight = bottomSheetView.rootView.height
                    val keypadHeight = screenHeight - rect.bottom

                    if (keypadHeight > screenHeight * 0.15) {
                        // If the keyboard is opened, adjust the bottom sheet height
                        layoutParams.height = screenHeight - keypadHeight
                        bottomSheet.layoutParams = layoutParams
                    } else {
                        // Reset to wrap content when the keyboard is hidden
                        layoutParams.height = CoordinatorLayout.LayoutParams.WRAP_CONTENT
                        bottomSheet.layoutParams = layoutParams
                    }
                }
            }
        }
        bottomSheetDialog.show()
    }

    private fun openBottomSignUpSheetDialog() {
        val bottomSheetView =
            LayoutInflater.from(this).inflate(R.layout.signup_bottom_sheet, null)
        val bottomSheetDialog = BottomSheetDialog(this)



        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.setOnShowListener { dialog ->
            val bottomSheet = (dialog as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout?
            bottomSheet?.let {
                it.setBackgroundColor(Color.TRANSPARENT) // Ensure transparency if needed
                val behavior = BottomSheetBehavior.from(it)

                // Remove fixed height, make it wrap_content
                val layoutParams = it.layoutParams as CoordinatorLayout.LayoutParams
                layoutParams.height = CoordinatorLayout.LayoutParams.WRAP_CONTENT
                it.layoutParams = layoutParams

                behavior.state = BottomSheetBehavior.STATE_EXPANDED

                // Handle behavior to adjust bottom sheet when keyboard is shown
                bottomSheetView.viewTreeObserver.addOnGlobalLayoutListener {
                    val rect = Rect()
                    bottomSheetView.getWindowVisibleDisplayFrame(rect)
                    val screenHeight = bottomSheetView.rootView.height
                    val keypadHeight = screenHeight - rect.bottom

                    if (keypadHeight > screenHeight * 0.15) {
                        // If the keyboard is opened, adjust the bottom sheet height
                        layoutParams.height = screenHeight - keypadHeight
                        bottomSheet.layoutParams = layoutParams
                    } else {
                        // Reset to wrap content when the keyboard is hidden
                        layoutParams.height = CoordinatorLayout.LayoutParams.WRAP_CONTENT
                        bottomSheet.layoutParams = layoutParams
                    }
                }
            }
        }
        bottomSheetDialog.show()
    }

    private fun authenticateWithOkta(email: String, password: String,bottomSheetDialog: BottomSheetDialog) {
        Log.d("OktaAuth", "Starting authentication for $email")

        val jsonBody = JSONObject().apply {
            put("username", email)
            put("password", password)
        }

        val requestBody = jsonBody.toString().toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .url(oktaApiUrl) // Replace with your actual API URL
            .post(requestBody)
            .addHeader("Content-Type", "application/json")
            .build()

        OkHttpClient().newCall(request).enqueue(object : okhttp3.Callback {
            override fun onResponse(call: okhttp3.Call, response: Response) {
                Log.d("OktaAuth", "Response received: ${response.code}")
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    Log.d("OktaAuth", "Response body: $responseBody")

                    runOnUiThread {
                        bottomSheetDialog.dismiss()
                        Toast.makeText(this@TestActivity, "Login successful", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.d("OktaAuth", "Error response: ${response.message}")
                    runOnUiThread {
                        Toast.makeText(this@TestActivity, "Authentication failed: ${response.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: okhttp3.Call, e: IOException) {
                Log.e("OktaAuth", "Request failed", e)
                runOnUiThread {
                    Toast.makeText(this@TestActivity, "Request failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }


    private fun login() {
        Log.d("Auth0", "Login process started")  // Log when the login process starts

        WebAuthProvider
            .login(account)
            .withScheme(getString(R.string.com_auth0_scheme))
            .start(this, object : Callback<Credentials, AuthenticationException> {

                override fun onFailure(exception: AuthenticationException) {
                    Log.e("Auth0", "Login failed: ${exception.message}")  // Log failure message
                    showSnackBar(getString(R.string.login_failure_message))
                }

                override fun onSuccess(credentials: Credentials) {
                    // The user successfully logged in.
                    val idToken = credentials.idToken
                    userIsAuthenticated = true

                    Log.d("Auth0", "Login successful! ID Token: $idToken")  // Log successful login with ID token
                    showSnackBar(getString(R.string.login_success_message))
                }
            })
    }


    private fun logout() {
        WebAuthProvider
            .logout(account)
            .withScheme(getString(R.string.com_auth0_scheme))
            .start(this, object : Callback<Void?, AuthenticationException> {

                override fun onFailure(exception: AuthenticationException) {
                    showSnackBar(getString(R.string.general_failure_with_exception_code,
                        exception.getCode()))
                }

                override fun onSuccess(payload: Void?) {
                    showSnackBar("Logout Successful")
                }

            })
    }



    fun logoutFromApp(auth0: Auth0, context: Context, rootView: View) {
        try {
            // Initialize the CredentialsManager
            val credentialsManager = CredentialsManager(
                AuthenticationAPIClient(auth0),
                SharedPreferencesStorage(context)
            )

            // Clear stored credentials
            credentialsManager.clearCredentials()

            // Show success Snackbar
            Snackbar.make(rootView, "Logout successful", Snackbar.LENGTH_SHORT).show()


        }
        catch (e: Exception) {
            // Show error Snackbar
            Snackbar.make(rootView, "Logout failed: ${e.message}", Snackbar.LENGTH_LONG).show()
        }
    }



    private fun showSnackBar(text: String) {
        Snackbar
            .make(
                binding.root,
                text,
                Snackbar.LENGTH_LONG
            ).show()
    }

}
