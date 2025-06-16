package com.davidlj95.bonacompra.item

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class ItemCreateDto (
    @field:NotNull @field:NotBlank
    val name: String
)