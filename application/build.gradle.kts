plugins {
    java
    id("com.google.protobuf") version "0.9.4"
}

dependencies {
    runtimeOnly(libs.grpc.netty.shaded)
    implementation(libs.grpc.protobuf)
    implementation(libs.grpc.stub)
    implementation(libs.grpc.core)

    implementation(libs.spring.boot.starter.actuator)
    implementation(libs.grpc.spring.boot.starter)
    implementation(libs.spring.boot.starter.web)

    testImplementation("org.springframework.boot:spring-boot-starter-test")

    testRuntimeOnly(libs.junit.platform.launcher)

    implementation("javax.annotation:javax.annotation-api:1.3.2")
    compileOnly("org.projectlombok:lombok:1.18.34")
    annotationProcessor("org.projectlombok:lombok:1.18.34")
    implementation("com.google.protobuf:protobuf-java-util:3.25.1")

    implementation("org.slf4j:slf4j-api:2.0.13")
    implementation("ch.qos.logback:logback-classic:1.5.6")

}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.25.1"//libs.protobuf.protoc.get().toString()
    }
    plugins {
        register("grpc") {
            artifact = libs.protoc.gen.grpc.java.get().toString()
        }
    }
    generateProtoTasks {
        all().forEach {
            it.plugins {
                register("grpc")
            }
        }
    }
}
