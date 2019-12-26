package keb.test.util

import org.apache.commons.io.FileUtils
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption


class ClasspathCopier {

    companion object {

        /** Copy all files from package into specified targetDirectory */
        fun copyPackageIntoDirectory(packageName: String, targetDirectory: File) {
            val pkgName = cleanPackageName(packageName)
            targetDirectory.mkdirs()

            val resources = PathMatchingResourcePatternResolver()
                .getResources("classpath*:/${pkgName}/**")

            resources.forEach { resource ->
                Files.copy(
                    resource.getInputStream(),
                    Paths.get(targetDirectory.absolutePath, resource.filename),
                    StandardCopyOption.REPLACE_EXISTING
                )
            }
        }

        private fun cleanPackageName(packageName: String): String {
            var pkgName = packageName.replace('.', '/')
            while (pkgName.startsWith("/")) {
                pkgName = pkgName.substring(1)
            }
            while (pkgName.endsWith("/")) {
                pkgName = pkgName.substring(0, pkgName.length - 1)
            }
            return pkgName
        }

        fun copyResourceToFile(resource: String, destinationFile: File): File {
            ClasspathCopier::class.java.classLoader.getResourceAsStream(resource).use { resourceStream ->
                FileUtils.copyInputStreamToFile(resourceStream, destinationFile)
            }
            return destinationFile
        }

    }

}