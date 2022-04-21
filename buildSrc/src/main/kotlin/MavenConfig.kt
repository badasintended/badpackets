import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create

fun UploadConfig.maven(jar: Jar, sourceJar: Jar) {
    project.extensions.configure<PublishingExtension> {
        publications {
            create<MavenPublication>("maven") {
                artifactId = project.rootProp["archiveBaseName"]
                version = "${project.name}-${project.version}"
                artifact(jar) {
                    classifier = null
                }
                artifact(sourceJar) {
                    classifier = "sources"
                }
            }
        }
    }
}
