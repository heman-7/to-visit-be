package com.tovisit.backend.model

import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor
import javax.persistence.*

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
class Place(

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long,
    @Column(unique = true)
    var city: String,
    var attraction: String,
    var month: String,
    var other: String? = null

) {
    override fun toString(): String {
        return "Place(id=$id, city='$city', attraction='$attraction', month='$month', other=$other)"
    }
}
