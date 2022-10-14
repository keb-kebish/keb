# TODOs

## Core functionality

- ScreenshotMaker - Make default screenshot / html when browser is not initialized - with message "Browser is not
  initialized"
    - why test will fail, someone will search for screenshots
        - if he will find nothing he will be confused
        - if he find this default screenshot he will know, that browser was not initialized and that screenshot wasn't
          lost

-- browser.url

- relative = based on baseUrl  (if set)
- absolute

- videos (https://www.ignatiuz.com/blog/selenium/recording-of-test-execution-using-atutestrecorder/, ATUTestRecorder)


- Page and test should have access to "title" - page title by default
- Content Parameters (wait=true and required=false) makes probably no sence together add there validation with explaining error message 

- Prepare support for basic elements via out-of-the-box modules
   - for example, because this:
      
      ```assertThat(pageWithModulesPage.surname.textInput.getAttribute("value")).isEqualTo("Doe")```
      
     is really awful. .getAttribute('valeu') for input i really don't like
   - We realized, that  `WebElement.getAttribute("value")` is not nice. And that we can solve it by normal prepared modules: 
     `val input by content { module(InputModule(css("#selector"))) }`
   
- Keb should provide support for obtaining WebDriver (Firefox, chrome)
  - There is library (something like "web driver manager") which provide this
  - So that user doesn't have to investigate how to obtain driver
  
- Possibility to write tests, which use multiple Browsers...

- We want to capture images on fail test (maybe even on every test)

- Maybe Consider - In at() verifier use for example assertJ - so that we can have nice error message, when it fails

- at() - waiting
   - in at (and to) should be possible to override waiting timeout
   - at(::LongLoadingPage, wait: 30, retryInterval: 200)
   
- lateinit browser not initialized   -  Improve error message
  - Kdyz delas stranku a primo se do ni selecti obsah,
  - Tak vyleti     lateinit browser   has not been initialized
    - Šlo by se tam hooknout na getter a vratit nějakou víc vysvetlujici hlasku 
    
- in wait is used "Any" - to configure waiting presets, use static types

- keb config have to be "inheritable"
  c = kebConfig{someOptions}; c.extend {some local custom options}

- browser.baseUrl - why it is directly on browser? should not it be just in configuration?

- at in page do not accept lambda - maybe it could accept it and execute it

- Logger

- write version of browser: https://stackoverflow.com/questions/12556163/get-browser-version-using-selenium-webdriver

- Support for Javascript - See Geb "Javascript, AJAX and dynamic pages"

- Keb rerun failed tests - reseni pluginem do gradlu - viz d.richter


- Highlight support for easy highligt of web elements:
  ```
  def we = browser.find("div.app-header-btn-title",text: "Initial draft").singleElement()
  ((org.openqa.selenium.JavascriptExecutor)browser.driver).executeScript("arguments[0].setAttribute('style', 'background: yellow; border: 2px solid red;');", we)
  ```

## Keb configuration

- do configuration immutable
    - it will be easier to inherit configuration
    - and safely do changes in configuration, which affects only one particular test in one thread

- KebConfig can be defined global, or locally overridden by test  "probably not so important for the beginning"

- Load KebConfig from external file.
  - Preferably Kotlin Script format
  
- add prepared setup of network trottling for chrome
  Resources:  
  https://seleniumjava.com/2019/02/27/how-to-simulate-network-conditions/  
  https://webdriver.io/docs/api/chromium.html#getnetworkconditions  
  e.g.
  ```
  chromeDriver.networkThrottling.slow3G()
  chromeDriver.networkThrottling.set(latency, uploadInBytes, downloadInBytes)```

## Tests semantic

  
## Other

- verify, that setup project and write first working tests is as simple and prepared as possible
    - samples and on boarding have to be super easy

- REACT selectors - similar support we have for bobril for bobril - create support for react
    - https://webdriver.io/blog/2019/04/03/react-selectors.html

- to deprecated repositories gitlab etc. commit message about deprecation and link to kebish.org

- To web site add links to all kinds of logos

- publishing to maven central

- In our pages we need current latest version of our artifact - so everybody can easily search for it

- if i write this val aa by content { ClearableInput(css("#name")) } instead of this val aa by content { module(
  ClearableInput(css("#name"))) } I get ugly error - browser is not initialized - with no hint what is wrong -- Consider
  if method module is really needed When content get object instanceOf module - it can be initialize it - and then the
  first example would work and it is even more readable

## Documentation

- Reporters Documentation - document default values

- Document way, how to use WebDriverManager - So everybody is able to use it

- Initial documentation section
    - what is Keb  (primarne pro psani testu, lze pouzit i pro scraping, ale na to neni primarně laděný)
    - what it resolve for you = advantages = why use keb
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
    
    
- Write and show example, that you can pass instance of page to "to" method
  - _to(JobDetail(jobId = "88489"))_ and this open page "<app_url>/jobDetail?jobId=88489"
    ```kotlin
       class JobDetail(jobId :String) : Page() {
         override url = "jobDetaion/$jobId"
       }
    ```
    - very nice beutiful - even geb is not able to do it :)

  - You want remove duplication from your tests, thats why you dont want to test your navigation menu again and again.
    - In advance if you change your navigation menu, or move your page somewhere else. Your tests will still work.    
  
  - document, that each test can create its own configuration in case you need it. 
    (in general, all your test will share same configuration, but e.g. then you may want write test,
      which works with multiple
       browsers and you may want to pass him different browser provider, only for this test)  
       
       
         
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
      
      

## Keb Sample app
- Upper persist navigation bar
   - this will be done by module and or inheritance   
- Sidebar - which can be shown/hidden
   - This will be page object
- page with multiple tabs
   - abstract page for common things
   - Then separate Page, which inherits abstract page
- Lazy loading content
   - validator waits until page is loaded and prepared
   - optimal = lazy loading content of table   
- Login form
   - rest login in all tests
   - Test for login
   

   
