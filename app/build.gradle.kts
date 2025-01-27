plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.nada.nada"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.nada.nada"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        manifestPlaceholders["auth0Domain"] =  "@string/com_auth0_domain"
        manifestPlaceholders["auth0Scheme"] =  "@string/com_auth0_scheme"
        manifestPlaceholders["appAuthRedirectScheme"] =  "com.nada.nada"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"



    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
    }
    packagingOptions {
        resources {
            excludes += "META-INF/okta/version.properties"
        }
    }
}




dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation("androidx.browser:browser:1.8.0")
    implementation(libs.okta.authn.sdk.api)
    implementation("com.okta.android:okta-oidc-android:1.3.4")
    implementation(libs.okta.authn.sdk.impl)
    implementation("com.squareup.okhttp3:okhttp:4.10.0")

    implementation ("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.12.0")

    implementation ("com.auth0.android:auth0:2.7.0")
    implementation ("com.auth0.android:jwtdecode:2.0.1")
}
