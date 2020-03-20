# TODOs
## Core functionality

- support for method  browser.navigate("www.kebish.org") or some similar shortcut 
- Page and test should have access to "title" - page title by default
- Content Parameters (wait=true  and  required=false) makes probably no sence together 
        add there validation with explaining error message 

- Prepare support for basic elements via out-of-the-box modules
   - for example, because this:
      
      ```assertThat(pageWithModulesPage.surname.textInput.getAttribute("value")).isEqualTo("Doe")```
      
     is really awful. .getAttribute('valeu') for input i really don't like
   - We realized, that  `WebElement.getAttribute("value")` is not nice. And that we can solve it by normal prepared modules: 
     `val input by content { module(InputModule(css("#selector"))) }`

- Browser should have possibility to directly get and set url. 
  This should not been required:
   ```browser.driver.get("localhost:8080")```
  something like `browser.url` and `browser.setUrl()` or someting...
   
- Keb should provide support for obtaining WebDriver (Firefox, chrome)
  - There is library (something like "web driver manager") which provide this
  - So that user doesn't have to investigate how to obtain driver
  
- By default we don't want to close browser after each test
    - Share browser between tests.  DONE
      (plus do it configurable) TODO
        - Probably Implementation of "KebTest" will be needed.
        - Let there some possibility to write tests, which use multiple Browsers...
        - Clear browser between tests.
           - In the same way as Geb
           - plus close browser dialogs - the same way as Scaler do
           - if test open multiple tabs/windows - it should reduce it to one
           - Do it configurable

- We want to capture images on fail test (maybe even on every test)

- In at() verifier use for example assertJ - so that we can have nice error message, when it fails

- at() - waiting
   - in at (and to) should be possible to override waiting timeout
   - at(::LongLoadingPage, wait: 30, retryInterval: 200)
   
- lateinit browser not initialized
  - Kdyz delas stranku a primo se do ni selecti obsah,
  - Tak vyleti     lateinit browser   has not been initialized
    - Šlo by se tam hooknout na getter a vratit nějakou víc vysvetlujici hlasku 
    
- in wait is used "Any" - to configure waiting presets, use static types
 
- keb config have to be "inheritable"
   c = kebConfig{someOptions};  c.extend {some local custom options}   
   
- browser.baseUrl - why it is directly on browser? should not it be just in configuration?

- at in page do not accept lambda - maybe it could accept it and execute it
   
## Keb configuration
- in config   driver have to be closure 
     - to start browser in lazy way
     - to be able start browser before each test

- KebConfig can be defined global, or locally overridden by test  "probably not so important for the beginning"

- Load KebConfig from external file.
  - Preferably Kotlin Script format

## Tests semantic

  
## Other

- Document way, how to use WebDriverManager - So everybody is able to use it

- verify, that setup project and write first working tests is as simple and prepared as possible
     - samples and on boarding have to be super easy

- to deprecated repositories gitlab etc. commit message about deprecation and link to kebish.org

- To web site add links to all kinds of logos

- publishing to maven central

- In our pages we need current latest version of our artifact - so everybody can easily search for it

- if i write this
   val aa by content { ClearableInput(css("#name")) }
   instead of this
   val aa by content { module(ClearableInput(css("#name"))) }
   I get ugly error - browser is not initialized - with no hint what is wrong
  -- Consider if method module is really needed
    When content get object instanceOf module - it can be initialize it 
      - and then the first example would work and it is even more readable
     
     
## Documentation       

- Initial documentation section
    - what is Keb  (primarne pro psani testu, lze pouzit i pro scraping, ale na to neni primarně laděný) 
    - what it resolve for you  = advantages = why use keb
        - browser management
        - page objects
        - modules
        - screenshots
        - powerful content selectors
        - extendable
        - advice you, how to write and manage your tests
        - screenshots, videos
        - jquery selectors
        - and more...

- write to documentation
   - prepared to be easily integrated to testing frameworks (junit5 is prepared other can be easily added)
   - other selectors can be easily added (html, css, bobril selectors are prepared, other selectors can be easily added)

- Create sample app - covered with KebTest - With all 
    - use it as sample and documentation - How To Write GUI Keb Tests
    - use all good techniques - Autostart app - Good Validators - Inherit pages - Page for Dialog and for context menu     
     
- Documentation - "Selectors" 
    - explain, that its just common selectors
     
## Ideas
- KebTest - Could be only Interface and not Class
   => no restrictions for tests

- Consider usage of "concept of CurrentPage"
   - e.g. browser will remember on which page is this can be set by "at" method
   
   
- Consider possibility, where everything is waiting by default
    - even  WebElement.click() - is tried multiple times until it succeeed (with maxTimeout ofcourse)   
       - e.g.  element is covered by loader, thats why click fails, when loader disapper it will succeed
       - to achive this, some kind of WebElement proxy will be needed (or use custom KebElement)


- JQuery
    - module with support for JQuery selectors
      https://subscription.packtpub.com/book/web_development/9781849515740/1/ch01lvl1sec18/using-jquery-selectors
    - it will be needed to avoid conflicts with different versions of jQuery in case, that jQuery is already loaded:
       https://stackoverflow.com/questions/1566595/can-i-use-multiple-versions-of-jquery-on-the-same-page
       And luckyli it seems to be possible
      
      