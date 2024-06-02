import java.nio.charset.StandardCharsets

plugins {
    java
    id("org.spongepowered.gradle.vanilla") version "0.2.1-SNAPSHOT"
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
        toolchain.languageVersion.set(JavaLanguageVersion.of(21))

        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21

        withSourcesJar()
    }

    tasks.withType<JavaCompile> {
        options.encoding = StandardCharsets.UTF_8.name()
        options.release.set(21)
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
                name = "B2"
                url = rootProject.projectDir.resolve(".b2").toURI()
            }
        }
    }
}

repositories {
    maven("https://maven.fabricmc.net/")
    mavenCentral()
}

minecraft {
    version(rootProp["minecraft"])
}

dependencies {
    compileOnly("net.fabricmc:sponge-mixin:0.13.2+mixin.0.8.5")
    compileOnly("io.github.llamalad7:mixinextras-common:0.3.5")
    compileOnly("org.ow2.asm:asm:9.6")

    decompiler("org.vineflower:vineflower:1.10.0")
}

sourceSets {
    val main by getting
    val testmod by creating

    testmod.apply {
        compileClasspath += main.compileClasspath + main.output
    }
}
