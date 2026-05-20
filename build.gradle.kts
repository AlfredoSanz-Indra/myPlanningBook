buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.4.4")
    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "9.0.1" apply false
    id("org.jetbrains.kotlin.android") version "2.3.21" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.3.21"
}
val sourceCompatibility by extra(JavaVersion.VERSION_17)
