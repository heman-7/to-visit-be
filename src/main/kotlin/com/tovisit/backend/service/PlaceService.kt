package com.tovisit.backend.service

import com.tovisit.backend.model.Place
import com.tovisit.backend.repository.PlaceRepository
import org.springframework.stereotype.Service
import java.util.*
import java.util.logging.Logger

@Service
class PlaceService(val placeRepository: PlaceRepository) {

    val log: Logger = Logger.getLogger(this.javaClass.name)

    fun create(place: Place) {
        placeRepository.save(place)
    }

    fun findAll(): List<Place> {
        val places: List<Place> = placeRepository.findAll()
        log.info(" places.size " + places.size)
        return places
    }

    fun delete(id: String) {
        placeRepository.deleteById(id.toLong())
    }

    fun update(updatedPlace: Place): Boolean {
        val place = placeRepository.findById(updatedPlace.id)
        if (place.isPresent) {
            place.map {
                it.city = updatedPlace.city
                it.attraction = updatedPlace.attraction
                it.month = updatedPlace.month
            }
            placeRepository.save(place.get())
            return true
        } else {
            log.info("place not found ")
            return false
        }
    }

    fun find(id: String): Optional<Place> {
        return placeRepository.findById(id.toLong())
    }

}