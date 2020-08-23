### Run Tests
`gradlew test`





### Release version 
Release plugin would help :)

###### Release documentation
copy ./README.md to ./docs/README.md

###### Remove snapshot from version
- In main build.gradle.kts set version e.g. 
`version = "1.1"`

- commit push
- create tag e.g. "1.1"


###### Upload to Bintray
Run command:

`gradlew bintrayUpload -PbintrayApiKey=<API_KEY>`  
<API_KEY> - search for email _"Keb - Kebish - Bintray API Key"_

###### Prepare next snapshot version
- In main build.gradle.kts set version e.g. 
`version = "1.2-SNAPSHOT"`

- Update version in README.md

- commit push



