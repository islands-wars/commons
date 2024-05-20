plugins {
    id("java")
    id("maven-publish")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "fr.islandswars"
version = "0.2"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("org.mongodb:mongodb-driver-reactivestreams:5.0.0")
    implementation("io.lettuce:lettuce-core:6.3.2.RELEASE")
    implementation("com.rabbitmq:amqp-client:5.21.0")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "fr.islandswars.commons.Commons"
    }
}

tasks.shadowJar {
    manifest {
        attributes["Main-Class"] = "fr.islandswars.commons.Commons"
    }
}