plugins {
    id("buildsrc.convention.spring-boot-library")
    id("org.jetbrains.kotlin.kapt")
}

dependencies {
    api(libs.springBootStarterData.jpa)
    api("com.querydsl:querydsl-jpa:${libs.versions.querydsl.get()}:jakarta")
    kapt("com.querydsl:querydsl-apt:${libs.versions.querydsl.get()}:jakarta")
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}