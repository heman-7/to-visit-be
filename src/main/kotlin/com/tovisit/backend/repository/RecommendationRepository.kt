package com.tovisit.backend.repository

import com.tovisit.backend.model.Place
import com.tovisit.backend.model.Recommendation
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

interface RecommendationRepository : JpaRepository<Recommendation, Long> {

    companion object {
        const val table: String = "recommendation"
    }

    fun findByPlace(place: Optional<Place>): Optional<Recommendation>

    @Query(nativeQuery = true, value = "select * from $table order by times_searched desc limit :count")
    fun topSearched(count: Int): List<Recommendation>
}