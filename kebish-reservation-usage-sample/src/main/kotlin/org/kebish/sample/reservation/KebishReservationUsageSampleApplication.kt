package org.kebish.sample.reservation

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

fun main(args: Array<String>) {
    runApplication<KebishReservationUsageSampleApplication>(*args)
}

@SpringBootApplication
@RestController
class KebishReservationUsageSampleApplication {

    @GetMapping("/hello")
    fun hello(@RequestParam(value = "name", defaultValue = "World") name: String): String {
        return String.format("Hello %s!", name)
    }
}
