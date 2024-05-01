import org.gradle.api.Project
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.bundling.Jar
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.named
import org.gradle.language.jvm.tasks.ProcessResources

fun Project.setupPlatform() {
    val rootSourceSets = rootProject.extensions.getByType<SourceSetContainer>()
    val sourceSets = extensions.getByType<SourceSetContainer>()

    sourceSets.apply {
        val main = getByName("main")
        val testmod = create("testmod")

        main.apply {
            val root = rootSourceSets["main"]
            compileClasspath += root.output
        }

        testmod.apply {
            val root = rootSourceSets["testmod"]
            compileClasspath += main.compileClasspath + main.output + root.output
            runtimeClasspath += main.runtimeClasspath + main.output
        }
    }

    tasks.named<ProcessResources>("processResources") {
        from(rootSourceSets["main"].resources)
    }

    tasks.named<JavaCompile>("compileJava") {
        source(rootSourceSets["main"].allJava)
    }

    tasks.named<JavaCompile>("compileTestmodJava") {
        source(rootSourceSets["testmod"].allJava)
    }

    tasks.named<Jar>("sourcesJar") {
        from(rootSourceSets["main"].allSource)
    }
}
