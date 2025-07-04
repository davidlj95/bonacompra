package com.davidlj95.bonacompra.item

import jakarta.validation.constraints.NotBlank

data class ItemCreateDto(
    @field:NotBlank
    val name: String
)