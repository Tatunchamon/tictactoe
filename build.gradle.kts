plugins {
    java
    idea
    id("org.springframework.boot") version "3.3.1" apply false
    id("org.jetbrains.kotlin.jvm") version "2.0.20-Beta2"
}

group = "com.scentbird"
version = "0.0.1-SNAPSHOT"

allprojects {
    repositories {
        mavenCentral()
    }
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(JavaVersion.VERSION_21.toString())
    }
}

subprojects{
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.springframework.boot")

    tasks.withType<Test> {
        useJUnitPlatform()
    }
    dependencies {
        implementation(platform(rootProject.libs.spring.boot.bom))
    }
    tasks.jar {
        enabled = !sourceSets.main.get().allSource.isEmpty
    }
}
