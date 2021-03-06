import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.11"
    maven
}

group = "dev.vishna"
version = "0.1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
    jcenter()
}

dependencies {
    compile(kotlin("stdlib-jdk8"))
    compile("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.0.1")
    compile("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.0.1")
    compile("com.github.vishna:kmnd:0.0.2")
    compile("com.github.vishna:emojilog:master-SNAPSHOT")

    testCompile("junit", "junit", "4.12")
    testCompile("org.amshove.kluent:kluent:1.34")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}