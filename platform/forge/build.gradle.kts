import net.minecraftforge.gradle.SlimeLauncherOptions

plugins {
    id("net.minecraftforge.gradle") version "7.0.19"
    id("net.minecraftforge.jarjar") version "0.2.3"
}

setupPlatform("test")

repositories {
    minecraft.mavenizer(this)
    maven(fg.forgeMaven)
    maven(fg.minecraftLibsMaven)
    maven("https://maven.fabricmc.net/")
    mavenCentral()
}

jarJar.register {
    archiveClassifier = null
}

dependencies {
    implementation(minecraft.dependency("net.minecraftforge:forge:${rootProp["minecraft"]}-${rootProp["forge"]}"))
    annotationProcessor("net.minecraftforge:eventbus-validator:7.0-beta.7")

    implementation("org.jetbrains:annotations:19.0.0")

    compileOnly("io.github.llamalad7:mixinextras-common:0.3.5")
    implementation("jarJar"("io.github.llamalad7:mixinextras-forge:0.3.5")) {
        jarJar.configure(this) {
            setRange("[0.3.5,)")
        }
    }
}

minecraft {
    mappings("official", rootProp["minecraft"])

    runs {
        val runConfig = Action<SlimeLauncherOptions> {
            workingDir = rootProject.file("run")
            args("--mixin.config=badpackets.mixins.json")
        }
        create("client", runConfig)
        create("server", runConfig)
    }
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

    manifest.attributes(
        mapOf(
            "MixinConfigs" to "badpackets.mixins.json"
        )
    )
}

tasks.test {
    failOnNoDiscoveredTests = false
}

afterEvaluate {
    val jarJar by tasks.getting(Jar::class)
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
