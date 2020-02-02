### Publish version to bintray

In main build.gradle.kts set version e.g. 
`version = "1.1"`

Run command:

`gradlew bintrayUpload -PbintrayApiKey=<API_KEY>`