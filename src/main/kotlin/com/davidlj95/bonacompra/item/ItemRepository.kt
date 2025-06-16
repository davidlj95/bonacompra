package com.davidlj95.bonacompra.item

interface ItemRepository {
    fun save(item: Item): Item
    fun findAll(): Collection<Item>
    fun deleteAll()
}