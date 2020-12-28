package org.kebish.sample.reservation.gym

import java.time.LocalDateTime

data class GymReservation(
    val name: String,
    val from: LocalDateTime,
    val to: LocalDateTime,
)