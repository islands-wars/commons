plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "fr.islandswars"
version = "0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("org.mongodb:mongodb-driver-reactivestreams:5.0.0")
    implementation("io.lettuce:lettuce-core:6.3.2.RELEASE")
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