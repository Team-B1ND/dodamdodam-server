plugins {
    id("buildsrc.convention.spring-boot-library")
    kotlin("kapt")
}

dependencies {
    api(libs.springBootStarterData.jpa)
    api(libs.querydsl.jpa) { artifact { classifier = "jakarta" } }
    kapt(libs.querydsl.apt) { artifact { classifier = "jakarta" } }
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}