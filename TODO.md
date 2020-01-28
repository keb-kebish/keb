# TODOs
## Core functionality
- Prepare support for basic elements via out-of-the-box modules
   - for example, because this:
      
      ```assertThat(pageWithModulesPage.surname.textInput.getAttribute("value")).isEqualTo("Doe")```
      
     is really awful. .getAttribute('valeu') for input i really don't like
   - We realized, that  `WebElement.getAttribute("value")` is not nice. And that we can solve it by normal prepared modules: 
     `val input by content { module(InputModule(css("#selector"))) }`
  
- IMPORTATNT TO MULTIPLE TESTS Lazily start the browser inside tests 
  - not when the test starts, but on first browser access

- Browser should have possibility to directly get and set url. 
  This should not been required:
   ```browser.driver.get("localhost:8080")```
  something like `browser.url` and `browser.setUrl()` or someting...
   
- Keb should provide support for obtaining WebDriver (Firefox, chrome)
  - There is library (something like "web driver manager") which provide this
  - So that user doesn't have to investigate how to obtain driver
  
- By default we don't want to close browser after each test
    - Share browser between tests. (plus do it configurable)
        - Probably Implementation of "KebTest" will be needed.
        - Let there some possibility to write tests, which use multiple Browsers...

- We want to capture images on fail test (maybe even on every test)

- In at() verifier use for example assertJ - so that we can have nice error message, when it fails

- at() - waiting
   - in at (and to) should be possible to override waiting timeout
   - at(::LongLoadingPage, wait: 30, retryInterval: 200)
   
- lateinit browser not initialized
  - Kdyz delas stranku a primo se do ni selecti obsah,
  - Tak vyleti     lateinit browser   has not been initialized
    - Šlo by se tam hooknout na getter a vratit nějakou víc vysvetlujici hlasku  
   
## Keb configuration
- in config   driver have to be closure 
     - to start browser in lazy way
     - to be able start browser before each test

- KebConfig can be defined global, or locally overridden by test  "probably not so important for the beginning"

- Load KebConfig from external file.
  - Preferably Kotlin Script format

## Tests semantic

  
## Other
- publish Maven artifact

- verify, that setup project and write first working tests is as simple and prepared as possible
     - samples and on boarding have to be super easy
     
- closing browser register onJVMExit() - less browsers will hang in processes
   - probably will be needed need reference to browser as "weak reference" - to release memory  
     
     
## Ideas

- Consider usage of "concept of CurrentPage"
   - e.g. browser will remember on which page is this can be set by "at" method
   
   
- Consider possibility, where everything is waiting by default
    - even  WebElement.click() - is tried multiple times until it succeeed (with maxTimeout ofcourse)   
       - e.g.  element is covered by loader, thats why click fails, when loader disapper it will succeed
       - to achive this, some kind of WebElement proxy will be needed (or use custom KebElement)
