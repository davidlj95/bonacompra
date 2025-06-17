package com.davidlj95.bonacompra.item

import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import kotlin.jvm.optionals.getOrNull

@RestController
@RequestMapping("/items")
class ItemController {
    @Autowired
    lateinit var itemRepository: ItemRepository

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody @Valid itemCreate: ItemCreateDto): Item {
        return itemRepository.save(Item(null, itemCreate.name))
    }

    @GetMapping("")
    fun find(): Iterable<Item> {
        return itemRepository.findAll()
    }

    @GetMapping("{id}")
    fun findById(@PathVariable id: Int): ResponseEntity<Item> {
        val item = itemRepository.findById(id).getOrNull() ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(item)
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Int, @RequestBody @Valid itemUpdate: ItemUpdateDto): ResponseEntity<Item> {
        val existingItem = itemRepository.findById(id).getOrNull()
            ?: return ResponseEntity.notFound().build()
        existingItem.name = itemUpdate.name
        return ResponseEntity.ok(itemRepository.save(existingItem))
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun delete(@PathVariable id: Int): ResponseEntity<Void> {
        if(!itemRepository.existsById(id)) {
           return ResponseEntity.notFound().build()
        }
        itemRepository.deleteById(id)
        return ResponseEntity.noContent().build()
    }
}