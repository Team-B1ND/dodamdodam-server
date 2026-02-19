plugins {
    id("buildsrc.convention.spring-boot-library")
}

dependencies {
    api(libs.springBootStarterData.jpa)
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}