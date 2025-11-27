// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
}

// Add JavaPoet to the buildscript classpath and force the same version as project
// dependencies so Gradle plugins that run in their own classloader (like Hilt's
// Gradle plugin) have a consistent JavaPoet available at runtime.
buildscript {
    configurations.all {
        resolutionStrategy.force("com.squareup:javapoet:1.13.0")
    }
    dependencies {
        classpath("com.squareup:javapoet:1.13.0")
    }
}

// Force dependency resolution for JavaPoet to avoid `NoSuchMethodError` due to
// mismatched versions resolved transitively by other libraries. This ensures
// Hilt/Dagger annotation processors use a compatible JavaPoet API.
subprojects {
    configurations.all {
        resolutionStrategy {
            // Force a JavaPoet version that has `ClassName.canonicalName()` API
            // which some annotation processors (Hilt/Dagger) rely on.
            // Use a version that exists in Maven Central and matches KotlinPoet usage.
            // 1.13.0 is the latest published `com.squareup:javapoet` on Maven Central as of
            // this change and is compatible with kotlinpoet-javapoet 1.14.2.
            force("com.squareup:javapoet:1.13.0")
        }
    }
}