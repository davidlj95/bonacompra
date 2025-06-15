package com.davidlj95.bonacompra.item

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/items")
class ItemController {
    @Autowired
    lateinit var itemRepository: ItemRepository

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody itemCreate: ItemCreateDto): Item {
        return itemRepository.save(Item(null, itemCreate.name))
    }

    @GetMapping("")
    fun find(): Iterable<Item> {
        return itemRepository.findAll()
    }
}