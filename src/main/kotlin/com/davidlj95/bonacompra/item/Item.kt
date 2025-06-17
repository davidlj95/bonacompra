package com.davidlj95.bonacompra.item

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

@Entity
class Item(
    @Id @GeneratedValue
    var id: Int? = null,

    var name: String
)