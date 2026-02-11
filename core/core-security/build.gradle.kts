plugins {
    id("buildsrc.convention.spring-boot-library")
}

dependencies {
    api(project(":core:core-common"))
    api(libs.springBootStarterSecurity)
    implementation("io.jsonwebtoken:jjwt-api:0.12.3")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.3")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.3")
}
