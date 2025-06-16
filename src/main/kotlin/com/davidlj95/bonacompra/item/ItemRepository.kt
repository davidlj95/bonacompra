package com.davidlj95.bonacompra.item

interface ItemRepository {
    fun deleteAll()
    fun deleteById(id: Int)
    fun existsById(id: Int): Boolean
    fun findAll(): Collection<Item>
    fun findById(id: Int): Item?
    fun save(item: Item): Item
}