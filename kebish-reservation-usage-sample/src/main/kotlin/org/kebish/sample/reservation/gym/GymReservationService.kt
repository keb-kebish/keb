package org.kebish.sample.reservation.gym

import org.springframework.stereotype.Service

@Service
class GymReservationService(
    val gymReservationDao: GymReservationDao
) {

    fun getAllReservations(): List<GymReservation> =
        gymReservationDao.getAllReservations()

}