package com.tovisit.backend.repository

import com.tovisit.backend.model.Place
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface PlaceRepository : JpaRepository<Place, Long> {

    fun findByCity(name: String): Optional<Place>

}