package org.kebish.sample.reservation.gym

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("gym")
class GymReservationRestController(
    val gymReservationService: GymReservationService,
) {

    @GetMapping("/reservations")
    fun hello(): ReservationsResponse =
        ReservationsResponse(
            gymReservationService.getAllReservations()
        )


    @GetMapping()
    fun endpointInfo(): String {
        return "Gym reservations endpoint"
    }

}

class ReservationsResponse(
    val reservations: List<GymReservation>
)