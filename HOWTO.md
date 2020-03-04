### Publish version to bintray
Release plugin would help

- In main build.gradle.kts set version e.g. 
`version = "1.1"`

- commit push
- create tag e.g. "1.1"

Run command:

`gradlew bintrayUpload -PbintrayApiKey=<API_KEY>`


- In main build.gradle.kts set version e.g. 
`version = "1.2-SNAPSHOT"`

- Update version in README.md

- commit push