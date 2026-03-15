plugins {
    id("buildsrc.convention.spring-boot-application")
}

dependencies {
    implementation(project(":core:core-common"))
    implementation(project(":core:core-security"))

    // swagger
    implementation(libs.springdoc.openapi.webmvc.ui)

    // aws s3
    implementation(platform("software.amazon.awssdk:bom:2.42.9"))
    implementation("software.amazon.awssdk:s3")
}
