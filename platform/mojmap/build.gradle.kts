tasks.jar {
    from(rootProject.sourceSets["main"].output)
}

tasks.sourcesJar {
    from(rootProject.sourceSets["main"].allSource)
    archiveClassifier.set("sources")
}

afterEvaluate {
    upload {
        maven(tasks.jar.get(), tasks.sourcesJar.get())
    }
}
