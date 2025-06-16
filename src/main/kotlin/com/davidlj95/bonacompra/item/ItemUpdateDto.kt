package com.davidlj95.bonacompra.item

import jakarta.validation.constraints.NotBlank

data class ItemUpdateDto(
    @field:NotBlank
    val name: String
)
