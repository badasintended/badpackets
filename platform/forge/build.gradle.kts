import net.minecraftforge.gradle.common.util.RunConfig

plugins {
    id("net.minecraftforge.gradle") version "6.0.24"
    id("org.spongepowered.mixin") version "0.7.38"
}

setupPlatform()

repositories {
    maven("https://maven.minecraftforge.net")
    maven("https://maven.fabricmc.net/")
    mavenCentral()
}

jarJar.enable()

dependencies {
    minecraft("net.minecraftforge:forge:${rootProp["minecraft"]}-${rootProp["forge"]}")

    implementation("org.jetbrains:annotations:19.0.0")

    compileOnly("io.github.llamalad7:mixinextras-common:0.3.5")
    implementation(jarJar("io.github.llamalad7:mixinextras-forge:0.3.5")) {
        jarJar.ranged(this, "[0.3.5,)")
    }

    // https://github.com/MinecraftForge/MinecraftForge/blob/7b782e5b05d0059836b39fa072d49f63679d1782/mdk/build.gradle#L143C5-L143C92
    implementation("net.sf.jopt-simple:jopt-simple:5.0.4") { version { strictly("5.0.4") } }
}

minecraft {
    mappings("official", rootProp["minecraft"])
    reobf = false

    runs {
        val runConfig = Action<RunConfig> {
            workingDirectory(rootProject.file("run"))
            ideaModule("${rootProject.name}.${project.name}.testmod")

            mods {
                create("badpackets") {
                    source(sourceSets["main"])
                }

                create("badpackets_testmod") {
                    source(sourceSets["testmod"])
                }
            }
        }
        create("client", runConfig)
        create("server", runConfig)
    }
}

mixin {
    add(sourceSets["main"], "badpackets.refmap.json")
    config("badpackets.mixins.json")
}

tasks.processResources {
    inputs.property("version", project.version)

    filesMatching("META-INF/mods.toml") {
        expand("version" to project.version)
    }
}

tasks.jar {
    archiveClassifier = "dev"
    finalizedBy("jarJar")

    manifest.attributes(mapOf(
        "MixinConfigs" to "badpackets.mixins.json"
    ))
}

tasks.jarJar {
    archiveClassifier = ""
}

afterEvaluate {
    val jarJar = tasks.jarJar.get()
    val sourcesJar = tasks.sourcesJar.get()

    upload {
        curseforge(jarJar)
        modrinth(jarJar)
        maven(jarJar, sourcesJar)
    }
}

sourceSets.forEach {
    val dir = layout.buildDirectory.dir("sourcesSets/${it.name}")
    it.output.setResourcesDir(dir)
    it.java.destinationDirectory.set(dir)
}
