- Share browser between tests. (plus do it configurable)
    - Probably Implementation of "KebTest" will be needed.
    - Let there some possibility to write tests, which use multiple Browsers...


- How to debug selecting of web element? - time when element is selected
  - I would expect closure for that. It would solve it all.
  - In advance, how to select elements, which needs to be selected by multiple selections...?
  - for example if I put this into Module
  
     ```val label = html("label").text```
     
     Module stop working, because its trying to load it even before page is put into "url"
     
     
  

- In which style should be tests written?  I like style with closures
  - it enables you write it in the same way as now, but add possibliity to have
  - each page enclosed inside closure
  - it prevent using "stale" Pages  it helps a lot.  
  
  
  
- validator "at" - by default I would make it mandatory (default implementation would throw exception)
  - It could be disabled in config
  
- KebConfig can be defined global, or localy overriden by test  "probably not so important for the beginning"
     
     
- Why consider about wrapper around WebElement?
   - for example, because this:
      
      ```assertThat(pageWithModulesPage.surname.textInput.getAttribute("value")).isEqualTo("Doe")```
      
     is really awful. .getAttribute('valeu') for input i really don't like
     
     
     
- ScopedModule cannot be used in validator.
   ```
       some page
       ...
       override fun at() = name  //TODO Validation for module doesn't work well (its not web element its not wrapper...)
   
       val name = scopedModule(::ClearableInputModule, css("#name"))  
  ```