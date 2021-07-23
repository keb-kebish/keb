# Keb
Web Testing, Browser Automation, Page Object Pattern and more

http://kebish.org/

## Content
1. [Page object pattern](#page-object-pattern)
2. [Modules](#modules)
3. [Navigation](#navigation)
4. [Waiting](#waiting)
5. [Browser Management](#browser-management)
6. [Installation](#installation)
6. [Reporters](#reporters)
7. [Usage](#full-usage---keb--junit)
8. [About project](#about-project)
    1. [Committers](#committers)
    2. [Contributors](#contributors)
    3. [Change log](#change-log)




## Page object pattern
Keb is a https://gebish.org inspired Selenium wrapper written in Kotlin that allows you to modularize pages of your web application into logic units represented by Kotlin classes.
```kotlin
class KotlinHomePage : Page() {
    override fun url() = "https://kotlinlang.org"
}
```

### Page elements
To select web element on your page use the following methods.
```kotlin
css(".my-selector") // returns single DOM element by CSS selector
cssList(".my-select") // returns list of all elements found by CSS selector
// following selectors are also available
html("h1")
htmlList("h1")
xpath("/html/body/h1")
xpathList("/html/body/h1")
by(MyCustomBy()) // can be used with custom implementation of org.openqa.selenium.By
byList(MyCustomBy())
```    

For details see https://www.selenium.dev/documentation/en/getting_started_with_webdriver/locating_elements/

Module "nuc-bobril" adds support for [Bobril](https://bobril.com/) selectors. 

#### Content

In order to lazily access page content use the `content` delegate.
```kotlin
class KotlinHomePage : Page() {
    override fun url() = "https://kotlinlang.org"
    
    val header by content { css(".global-header-logo") }
    val headerText by content { header.text }
}
```
 There are multiple options how to handle the selected content:
- cache 
  - content can be cached - i.e. only initialized on first access, otherwise invoked on every access
  - usage: `val title by content(cache=true) { html("h1") }`
  - default value: `false`
- required:
  - you can specify whether the content is required to be present on the page
  - if required and the content is not present, then the `org.openqa.selenium.NoSuchElementException` exception is thrown, 
  instance of `org.kebish.core.content.EmptyContent` is returned otherwise
  - usage: `val title by content(required=true) { html("h1") }`
  - default value: `true`
- wait:
  - wheter the keb should wait for the given content
  - usage: 
  ```kotlin
  val title by content(wait = true) { html("h1") } // uses the default wait preset
  
  val title by content(wait = 60) { html("h1") } // uses custom timeout value with retryInterval from the default wait preset
  
  val title by content(wait = "quick") { html("h1") } // uses the given wait preset
  
  val title by content(waitTimeout = 10, waitRetryInterval = 2) { html("h1") } // specify both timeout and retry interval
  ```
  - if content is still not present after the specified interval, the `org.kebish.core.WaitTimeoutException` exception is thrown
  - default value: `false`
  - for more info on waiting, refer to the Waiting section below
  
  For basic content example see test [ContentUsageSample](https://github.com/keb-kebish/keb/blob/master/keb-core/src/test/kotlin/org/kebish/usage/content/ContentUsageSample.kt)

### Page verifier
In order to verify, that you successfully landed on your page, you can use `at` method.
```kotlin
class KotlinHomePage : Page() {
    override fun url() = "https://kotlinlang.org"
    override fun at() = header
    
    val header by content { css(".global-header-logo") }
}
```
Whether every page has to define `at` verifier can be controlled by configuration property `atVerifierRequired`. If no
verifier is defined and neither is required, no page verification is performed.

## Modules
If you want to reuse page content present on multiple pages, you can use modules. Module has an optional constructor
parameter `scope`, which can be used as a search root for all the module's content. To initialize module with use the `module` method.
```kotlin
class NavMenuModule(scope: WebElement) : Module(scope) {
    val menuItems by content { htmlList("a") }
}

class KotlinHomePage : Page() {
    override fun url() = "https://kotlinlang.org"
    
    // either initialize the module by passing the module object into the 'module' method
    val menu by content { module(NavMenuModule(css(".nav-links"))) }
    
    // or initialize the module by calling the 'module' method on a WebElement and pass reference to your module
    val menu2 by content { css(".nav-links").module(::NavMenuModule) }
}
```       

## Navigation
`at(::MyPage)` create instance of page and wait until validator `at()` is satisfied

`to(::MyPage)` write url of page into browser and call method `at()`

`at(::MyPage) { methodOnMyPage() }` methods can have closure. Inside closure `this` is created page

`at(::MyPage) { clickMainLink() }` return result of closure

All the methods defined above can be used with constructor reference `to(::MyPage)` or with instance of a page `to(MyPage())`.
Please note that constructor reference variant can only be used if the required page has no primary constructor parameters.

Both `to` and `at` methods can wait for the transition/verification of page to happen. Wait definition is same as for the content method.
```kotlin
val myPage = to(::MyPage, wait = true)
val myPage = to(::MyPage, wait = 30)
val myPage = to(::MyPage, wait = "quick")
val myPage = to(::MyPage, waitTimeout = 10, waitRetryInterval = 1)
// same for the 'at' methods
```

### Navigating pages using fluent API
```kotlin
val joeContact = to(::MyPage)
    .via { clickOnContacts() }
    .via { findContact("joe") } 
``` 

```kotlin
// Same as method above, but explicitly validate, that method is called on correct page
val joeContact = to(::MyPage)
    .via(MyPage::class) { clickOnContacts() }
    .via(ContactsPage::class) { findContact("joe") } 
``` 


## Waiting
Waiting for some page element to be present, you can use `waitFor` method.
```kotlin
css(".form-input").click()
val result = waitFor {
    css(".result")
}
``` 
Timeout duration and retry interval can be defined either by custom wait preset or directly through `waitFor` method.
```kotlin
waitFor(preset = "quick") { css(".result") }
waitFor(timeout = 2, retryInterval = 0.1) { css(".result") }
```
Custom wait presets can be defined using Kotlin DSL in the `Configuration`. Default values when no custom configuration 
is defined are 15 seconds for timeout and 1 second for retry interval.
```kotlin
kebConfig {
    waiting {
        timeout = 15
        retryInterval = 1
        "quick" {
            timeout = 2
            retryInterval = 0.1
        }
        "slow" {
            timeout = 60        
        }
    }
}
```

## Browser Management
For creating, providing and quiting browser is responsible `BrowserProvider`
Which can be set in configuration e.g.
```kotlin
kebConfig {
   browserProvider = NewBrowserForEachTestProvider(this) 
}
```
Prepared providers:
  -  `NewBrowserForEachTestProvider` - Quit WebDriver (close browser) after each test.<br>
       This is the most robust option because your tests are truly independent.
       But this approach is very SLOW because creating a new WebDriver takes approximately 7 seconds per test.
       
  - `StaticBrowserProvider` (used by default) - Reuse the same browser(WebDriver) among tests.<br>
   Browser is stored in `static` field, all tests with StaticBrowserProvider use the same Browser.
   That's why this is not suitable for parallel execution of tests.  
   Attributes: 
     - `clearCookiesAfterEachTest` 
       default value is `true`   
       After each test clear all cookies from browser.  
       Cost is approximately 15ms per test.      
     - `clearWebStorageAfterEachTest`  
       default value is `false`  
       After each test clear WebStorage. More precise it clear
       `org.openqa.selenium.html5.LocalStorage` and `org.openqa.selenium.html5.SessionStorage`
       Cost is approximately 45ms per test.
     - `openNewEmptyWindowAndCloseOtherAfterEachTest`  
       default value is `true`  
       After each test it will open a new tab and close all other tabs and windows.  
       Thanks to this - each test starts with one empty tab.
       And you will avoid issues caused by tabs opened by previous tests.
     - `autoCloseAlerts`  
       default value is `true`  
       An additional option for the previous attribute.
       If closing the window will be prevented by an alert dialog.
       This option will try to close this alert and then attept to close the window again.
            

Anybody can write BrowserProvider, but for most use cases prepared implementation will be sufficient.
If you need implement for example Provider, which share browser per thread 
(it would be suitable for parallel execution of tests)
You can do it on your own or do not hesitate to ask us, and we will add this feature.

## Reporters
Reporters work like plugins. A custom reporter can be configured or prepared Reporter can be used.
  - `ScreenshotReporter` save `png` screenshot into "reporterDir"
  - `PageSourceReporter` save `html` screenshot into "reproterDir"
  
e.g - save `png` and `html` after each failed test:
```kotlin  
    kebConfig  {
        reports {
            reporterDir = File("keb-reports")
            testFailReporters.add(ScreenshotReporter())
            testFailReporters.add(PageSourceReporter())
        }
    }
```     

## Installation
Relesed jar files are available in jcenter().

Gradle setup:
```kotlin
repositories {
    jcenter()
}

val kebVersion = "1.0"
dependencies{
  testImplementation("org.kebish:keb-core:$kebVersion")
  testImplementation("org.kebish:keb-junit5:$kebVersion")
  testImplementation("org.kebish:keb-bobril:$kebVersion")


  // For downloading correct WebDriver we recommend to use WebDriverManager
  // https://github.com/bonigarcia/webdrivermanager  
  testImplementation("io.github.bonigarcia:webdrivermanager:<insert_webdrivermanager_version>")

}
```
If you use Keb in tests you will probably use configuration `testImplementation` instead of `implementation`


## Full usage - keb + JUnit
```kotlin
package org.kebish.usage

import io.github.bonigarcia.wdm.WebDriverManager
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.kebish.core.config.kebConfig
import org.kebish.core.module.Module
import org.kebish.core.page.Page
import org.kebish.junit5.KebTest
import org.openqa.selenium.WebElement
import org.openqa.selenium.firefox.FirefoxDriver

class KotlinSiteKebTest : KebTest(kebConfig {
    WebDriverManager.firefoxdriver().setup()
    driver = { FirefoxDriver() }
    baseUrl = "https://kotlinlang.org"
}) {

    @Test
    fun `testing kotlin lang page`() {
        // given
        val homePage = to(::KotlinHomePage)

        // when
        val title = homePage.header

        // then
        Assertions.assertEquals("Kotlin", title.text)
        Assertions.assertTrue(homePage.licensedUnderApacheLicense())

        // when
        val docsPage = homePage.openDocumentation()

        // then
        Assertions.assertEquals("Kotlin docs", docsPage.title.text)
    }

}

class KotlinHomePage : Page() {
    override fun url() = "/"
    override fun at() = header

    val header by content { css(".global-header-logo") }
    val menu by content { module(NavMenuModule(css(".nav-links"))) }
    val footer by content { module(FooterModule(html("footer"))) }

    fun openDocumentation(): KotlinDocumentationPage {
        menu.menuItems.first { it.text.contains("Docs", ignoreCase = true) }.click()
        return at(::KotlinDocumentationPage)
    }

    fun licensedUnderApacheLicense() = footer.licenseNotice.text.contains("apache", ignoreCase = true)

}

class KotlinDocumentationPage : Page() {
    override fun url() = "/docs/home.html"
    override fun at() = css("ul.toc")

    val title by content { html("h1") }

}

class NavMenuModule(scope: WebElement) : Module(scope) {
    val menuItems by content { htmlList("a") }
}

class FooterModule(scope: WebElement) : Module(scope) {
    val licenseNotice by content { css(".terms-foundation_link[href*=LICENSE]") }
    val sponsor by content { css(".terms-sponsor") }
}
```
For full usage example please refer to [/keb-core/src/test/kotlin/org/kebish/usage](https://github.com/keb-kebish/keb/tree/master/keb-core/src/test/kotlin/org/kebish/usage).

## About project
Any questions, issues, consultation ... ?

Do not hesitate to contact us at [info@kebish.org](mailto:info@kebish.org)

### Committers
- Michal Horčičko
- Jan Vondrouš 

### Contributors
- Pavel Sajda - [http://pseudofotograf.cz/](http://pseudofotograf.cz/)
  - Keb logo
- [Petr Kostka](https://github.com/mierak)
- David Richter

### Change log
- **1.0**
    - _Breaking changes_
        - Changed package names
    - RelativeUrlResolver - Browser.url - can accept relative url

- **0.4**
    - Basic configuration for Browser Management
        - **`StaticBrowserProvider`**
        - `NewBrowserForEachTestProvider`
        - see ["Browser Management"](#browser-management) section for details
    - Keb close all windows and tabs after each test
        - even close forgotten alert dialogs
        - see section "Browser Management" => "StaticBrowserProvider" => "openNewEmptyWindowAndCloseOtherAfterEachTest"
    - "Reporter" - possibility to implement own reporters
        - ScreenshotReporter - which can take screenshot after test
        - see ["Reporters"](#reporters) section for details
    - PasswordInput module

- **0.3**
    - _Breaking changes_
        - KebTest - Take Configuration object instead of Browser
    - By default keb reuse the same WebDriver (Browser window) and hold it in static field