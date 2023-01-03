package com.tovisit.backend.service

import com.tovisit.backend.model.Place
import com.tovisit.backend.model.Recommendation
import com.tovisit.backend.model.Searched
import com.tovisit.backend.repository.RecommendationRepository
import org.jboss.logging.Logger
import org.springframework.stereotype.Service
import java.util.*

@Service
class RecommendationService(val recommendationRepository: RecommendationRepository) {

    val log: Logger = Logger.getLogger(this.javaClass)

    fun update(place: Optional<Place>, searched: Searched) {

        val recommendation: Optional<Recommendation> = recommendationRepository.findByPlace(place)
//        log.info("existingRecommendation : $recommendation")

        if (recommendation.isPresent) {
            val existingRecommendation = recommendation.get()
            existingRecommendation.also {
                it.lastSearchedAt = searched.searchedAt
                it.timesSearched = it.timesSearched + 1
                val ips: MutableList<String> = it.sourceIps.split(",").toMutableList()
                ips.add(searched.sourceIP)
                it.sourceIps = ips.joinToString(",")
            }
            log.info("updated existingRecommendation : ${existingRecommendation.place}")
            recommendationRepository.save(existingRecommendation)
        } else {
            val newRecommendation = Recommendation(
                place = place.get(),
                timesSearched = 1,
                lastSearchedAt = searched.searchedAt,
                sourceIps = searched.sourceIP
            )
            log.info("newRecommendation : ${newRecommendation.place}")
            recommendationRepository.save(newRecommendation)
        }
    }

    fun fetch(): List<Recommendation> {
        return recommendationRepository.topSearched(6)
    }

}