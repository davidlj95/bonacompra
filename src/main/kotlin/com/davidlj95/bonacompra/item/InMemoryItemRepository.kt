package com.davidlj95.bonacompra.item

import org.springframework.stereotype.Repository

@Repository
class InMemoryItemRepository: ItemRepository {
    var lastId = 1
    val items = hashMapOf<Int, Item>()

    override fun save(item: Item): Item {
        if(item.id == null) item.id = lastId++
        items.put(item.id!!, item)
        return item
    }

    override fun findAll(): Collection<Item> {
        return items.values
    }

    override fun findById(id: Int): Item? {
        return items[id]
    }

    override fun deleteAll() {
        items.clear()
        lastId = 1
    }

    override fun deleteById(id: Int) {
        items.remove(id)
    }

    override fun existsById(id: Int): Boolean {
        return items.contains(id)
    }
}