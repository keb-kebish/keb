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


###### Upload to Bintray
Run command:

`gradlew bintrayUpload -PbintrayApiKey=<API_KEY>` //TODO replace with mavenCentral  
<API_KEY> - is in email _"Keb - Kebish - Bintray API Key"_

###### Prepare next snapshot version

- In main `buildSrc/src/main/kotlin/kebbuild.project-info.gradle.kts` set version e.g.
  `version = "1.2-SNAPSHOT"`

- commit push



