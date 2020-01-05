# TODOs
## Core functionality
- validator "at" - by default I would make it mandatory (default implementation would throw exception)
  - It could be disabled in config
  
- Prepare support for basic elements via out-of-the-box modules
   - for example, because this:
      
      ```assertThat(pageWithModulesPage.surname.textInput.getAttribute("value")).isEqualTo("Doe")```
      
     is really awful. .getAttribute('valeu') for input i really don't like
   - We realized, that  `WebElement.getAttribute("value")` is not nice. And that we can solve it by normal prepared modules: 
     `val input by content { module(InputModule(css("#selector"))) }`
  
- Lazily start the browser inside tests 
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

## Keb configuration
- KebConfig can be defined global, or locally overridden by test  "probably not so important for the beginning"

- Load KebConfig from external file.
  - Preferably Kotlin Script format

## Tests semantic
- In which style should be tests written?  I like style with closures
  - it enables you write it in the same way as now, but add possibliity to have
  - each page enclosed inside closure
  - it prevent using "stale" Pages  it helps a lot.
    
- Vodnr: TODO solve how to go from one page to other
   ```
     at(::Page1) {
       menu.goToPage2()  //this will return page 2
   }
    at(::Page2) {   //This is  ble
  }
  ```
  
## Other
- publish Maven artifact