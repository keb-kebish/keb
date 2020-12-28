package org.kebish.sample.reservation.gym

import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class GymReservationDaoInMemoryImpl : GymReservationDao {

    private val reservations: List<GymReservation> = mutableListOf(
        GymReservation(
            name = "Testing reservation",
            from = LocalDateTime.of(2020, 12, 26, 17, 0),
            to = LocalDateTime.of(2020, 12, 26, 18, 0),
        )
    )

    override fun getAllReservations(): List<GymReservation> = reservations

}