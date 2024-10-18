import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "it.dhd.oxygencustomizer.aiplugin"
    compileSdk = 34

    defaultConfig {
        applicationId = "it.dhd.oxygencustomizer.aiplugin"
        minSdk = 31
        targetSdk = 34
        versionCode = 7
        versionName = "2.1.0"
        setProperty("archivesBaseName", "OxygenCustomizerAIPlugin.apk")
    }

    val keystorePropertiesFile = rootProject.file("keystore.properties")
    var releaseSigning = signingConfigs.getByName("debug")

    try {
        val keystoreProperties = Properties()
        FileInputStream(keystorePropertiesFile).use { inputStream ->
            keystoreProperties.load(inputStream)
        }

        releaseSigning = signingConfigs.create("release") {
            keyAlias = keystoreProperties.getProperty("keyAlias")
            keyPassword = keystoreProperties.getProperty("keyPassword")
            storeFile = rootProject.file(keystoreProperties.getProperty("storeFile"))
            storePassword = keystoreProperties.getProperty("storePassword")
            enableV1Signing = true
            enableV2Signing = true
        }
    } catch (ignored: Exception) {
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                "proguard-android-optimize.txt",
                "proguard.pro",
                "proguard-rules.pro")
            signingConfig = releaseSigning
        }
        debug {
            isDebuggable = true
            isMinifyEnabled = false
            isShrinkResources = false
            signingConfig = releaseSigning
        }
        getByName("debug") {
            versionNameSuffix = ".debug"
        }
    }
    applicationVariants.all {
        val variant = this
        variant.outputs
            .map { it as com.android.build.gradle.internal.api.BaseVariantOutputImpl }
            .forEach { output ->
                val outputFileName = "OxygenCustomizerAIPlugin.apk"
                println("OutputFileName: $outputFileName")
                output.outputFileName = outputFileName
            }
    }
    buildFeatures{
        buildConfig = true
        aidl = true
        viewBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    packaging {
        jniLibs.useLegacyPackaging = true
    }
}

dependencies {

    implementation(libs.su.core)
    implementation(libs.su.nio)
    implementation(libs.su.service)
    implementation(libs.core.ktx)
    implementation(libs.pytorch.android.lite)
    implementation(libs.pytorch.android.torchvision.lite)
    implementation(libs.material)
    implementation(libs.androidx.preference)
    implementation(libs.prdownloader)
    implementation(libs.onnxruntime)
    implementation(libs.androidx.exifinterface)

}