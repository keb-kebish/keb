package org.kebish.sample.reservation.gym

interface GymReservationDao {
    fun getAllReservations(): List<GymReservation>

}
