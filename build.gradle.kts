import java.nio.charset.StandardCharsets

plugins {
    java
    id("net.fabricmc.fabric-loom") version "1.15.5"
    id("maven-publish")
}

version = env["MOD_VERSION"] ?: "${prop["majorVersion"]}.999-${env["GIT_HASH"] ?: "local"}"

allprojects {
    apply(plugin = "base")
    apply(plugin = "java")

    version = rootProject.version

    repositories {
        mavenCentral {
            content {
                excludeGroupByRegex("org.lwjgl")
            }
        }

        maven("https://libraries.minecraft.net")
    }

    java {
        toolchain.languageVersion.set(JavaLanguageVersion.of(25))

        sourceCompatibility = JavaVersion.VERSION_25
        targetCompatibility = JavaVersion.VERSION_25

        withSourcesJar()
    }

    tasks.withType<JavaCompile> {
        options.encoding = StandardCharsets.UTF_8.name()
        options.release.set(25)
    }

    task("listPluginVersions") {
        doLast {
            project.plugins.forEach {
                println("$it -> ${it.javaClass.protectionDomain.codeSource.location.toURI().toString().lowercase()}")
            }
        }
    }
}

subprojects {
    apply(plugin = "maven-publish")

    base {
        archivesName.set("${rootProject.base.archivesName.get()}-${project.name}")
    }

    publishing {
        repositories {
            maven {
                url = uri("https://maven.pkg.github.com/badasintended/badpackets")
                name = "GitHub"
                credentials {
                    username = env["GITHUB_ACTOR"]
                    password = env["GITHUB_TOKEN"]
                }
            }

            maven {
                url = uri("https://maven4.bai.lol")
                name = "Badasintended"
                credentials {
                    username = env["MAVEN_USERNAME"]
                    password = env["MAVEN_PASSWORD"]
                }
            }
        }
    }
}

repositories {
    mavenCentral()
    maven("https://maven2.bai.lol")
}

dependencies {
    minecraft("com.mojang:minecraft:${rootProp["minecraft"]}")
    compileOnly("lol.bai:fabric-loader-environment:0.0.1")

    compileOnly("net.fabricmc:sponge-mixin:0.13.2+mixin.0.8.5")
    compileOnly("io.github.llamalad7:mixinextras-common:0.3.5")
}

loom {
    runs {
        configureEach {
            isIdeConfigGenerated = false
        }
    }
}

sourceSets {
    val main by getting
    val testmod by creating

    testmod.apply {
        compileClasspath += main.compileClasspath + main.output
    }
}
