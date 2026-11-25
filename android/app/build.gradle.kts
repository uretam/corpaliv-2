plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("jacoco")
}

android {
    namespace = "cl.corpaliv.app"
    compileSdk = 36

    defaultConfig {
        applicationId = "cl.corpaliv.app"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "0.1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes { release { isMinifyEnabled = false } }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }
    buildFeatures { compose = true }
}

dependencies {
    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation(platform("androidx.compose:compose-bom:2024.09.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.9.0")
    testImplementation("com.squareup.okhttp3:mockwebserver:4.12.0")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test:runner:1.6.2")
}

jacoco { toolVersion = "0.8.13" }

tasks.register<JacocoReport>("jacocoLogicReport") {
    dependsOn("testDebugUnitTest")
    executionData(layout.buildDirectory.file("jacoco/testDebugUnitTest.exec"))
    sourceDirectories.setFrom(files("src/main/java"))
    classDirectories.setFrom(fileTree(layout.buildDirectory.dir("tmp/kotlin-classes/debug")) {
        include("cl/corpaliv/app/AppViewModel*", "cl/corpaliv/app/AppState*", "cl/corpaliv/app/Pantalla*",
            "cl/corpaliv/app/ApiRepository*", "cl/corpaliv/app/FormularioValidator*")
    })
    reports { xml.required.set(true); html.required.set(true) }
}

tasks.register<JacocoReport>("jacocoAppReport") {
    dependsOn("testDebugUnitTest")
    executionData(layout.buildDirectory.file("jacoco/testDebugUnitTest.exec"))
    sourceDirectories.setFrom(files("src/main/java"))
    classDirectories.setFrom(fileTree(layout.buildDirectory.dir("tmp/kotlin-classes/debug")) {
        include("cl/corpaliv/app/**")
    })
    reports { xml.required.set(true); html.required.set(true) }
}
