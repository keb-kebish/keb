### Run Tests
`gradlew test`





### Release version 
Release plugin would help :)

###### Release documentation
- In `./README.md` set version e.g. `val kebVersion = 1.1`

- copy ./README.md to `./docs/README.md`

###### Remove snapshot from version
- In main `buildSrc/src/main/kotlin/kebbuild.project-info.gradle.kts` set version e.g.
  `version = "1.1"`

- commit push
- create tag e.g. "1.1"


###### Upload Maven Central

Run command:
`gradlew publish`

Note credentials and secret signing key have to be setup in `gradle.properties`
Instructions can be found here https://drive.google.com/drive/folders/1mjguZXZkCqyLBUPAhzNBohnfGSIp8XZe

###### Prepare next snapshot version

- In main `buildSrc/src/main/kotlin/kebbuild.project-info.gradle.kts` set version e.g.
  `version = "1.2-SNAPSHOT"`

- commit push

###### Then Release in sonartype to sync into maven central

- follow these instructions https://central.sonatype.org/publish/release/



