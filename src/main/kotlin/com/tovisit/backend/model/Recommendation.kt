package com.tovisit.backend.model

import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToOne

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

class Recommendation(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @OneToOne()
    val place: Place,
    var timesSearched: Int,
    var lastSearchedAt: Long,
    var sourceIps: String
)