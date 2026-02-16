plugins {
    id("buildsrc.convention.spring-boot-library")
}

dependencies {
    api(project(":core:core-common"))
    api(libs.springBootStarterSecurity)
    api(libs.bundles.kotlinxEcosystem)
    
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
}
