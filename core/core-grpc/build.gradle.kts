import com.google.protobuf.gradle.id

plugins {
    id("buildsrc.convention.spring-boot-library")
    id("com.google.protobuf") version "0.9.4"
}

dependencies {
    implementation(libs.kotlinxCoroutines)
    implementation(project(":core:core-common"))
    implementation(libs.springBootStarterWeb)

    // grpc
    api("io.grpc:grpc-protobuf:1.62.2")
    api("io.grpc:grpc-stub:1.62.2")
    api("io.grpc:grpc-kotlin-stub:1.4.1")

    implementation("com.google.protobuf:protobuf-kotlin:3.25.3")

}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.25.3"
    }
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.62.2"
        }
        id("grpckt") {
            artifact = "io.grpc:protoc-gen-grpc-kotlin:1.4.1:jdk8@jar"
        }
    }
    generateProtoTasks {
        all().forEach {
            it.plugins {
                id("grpc")
                id("grpckt")
            }
        }
    }
}