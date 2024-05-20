plugins {
    id("java")
    id("maven-publish")
}

group = "fr.islandswars"
version = "0.2.5"

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

val sourceJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}

val javadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
    from(tasks.javadoc.get().destinationDir)
}

publishing {
    publications {
        create<MavenPublication>("gpr") {
            from(components["java"])
            groupId = rootProject.group.toString()
            artifactId = rootProject.name
            version = version

            artifact(sourceJar)
            artifact(javadocJar)

            pom {
                name.set(rootProject.name)
                description.set("Utility classes for connecting to islands-wars database")
                url.set("https://github.com/islands-wars/commons")

                licenses {
                    license {
                        name.set("The GNU General Public License, Version 3.0")
                        url.set("https://www.gnu.org/licenses/gpl-3.0.html#license-text")
                    }
                }
                developers {
                    developer {
                        id.set("Xharos")
                        name.set("Burgaud Valentin")
                        email.set("jangliu@islandswars.fr")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/islands-wars/commons.git")
                    developerConnection.set("scm:git:ssh://github.com:islands-wars/commons.git")
                    url.set("https://github.com/islands-wars/commons")
                }
            }
        }
    }

    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/islands-wars/commons")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
                password = project.findProperty("gpr.token") as String? ?: System.getenv("TOKEN")
            }
        }
    }
}