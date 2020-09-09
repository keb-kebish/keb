rootProject.name = "keb"
include("keb-core")
include("keb-junit5")
include("keb-bobril")


fun setupBuildFileName(project: ProjectDescriptor) {
    project.children.forEach {
        it.apply {
            buildFileName = "$name.gradle.kts"
            assert(projectDir.isDirectory)
            assert(buildFile.isFile)
            setupBuildFileName(this)
        }
    }
}
setupBuildFileName(rootProject)