plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.android.library) apply false
}

buildscript {
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://repo.okta.com/maven2/")  // Okta repository
        }
    }
    dependencies {
        classpath(libs.gradle)
        classpath(libs.kotlin.gradle.plugin)
        classpath("com.android.tools:r8:8.7.18")
    }
}
