package com.davidlj95.bonacompra.item

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content


@SpringBootTest
@AutoConfigureMockMvc
class ItemControllerIT {
    val apiPath = "/items"

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var itemRepository: ItemRepository

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun setUp() {
        itemRepository.deleteAll()
    }

    @Test
    fun `should create an item and return it with created status`() {
        val name = "item"
        lateinit var item: Item

        mockMvc.perform(
            post(apiPath)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ItemCreateDto(name)))
        )
            .andExpect {
                val items = itemRepository.findAll()
                assertEquals(1, items.count())
                item = items.first()
                assertEquals(item.name, item.name)
            }
            .andExpect(content().json(objectMapper.writeValueAsString(item)))
            .andExpect(status().isCreated)


    }

    @Test
    fun `should not create an item if text is empty`() {
        mockMvc.perform(
            post(apiPath)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ItemCreateDto("  ")))
        )
            .andExpect(status().is4xxClientError)

        assertEquals(0, itemRepository.count())
    }

    @Test
    fun `should return list of items and ok status`() {
        val item = itemRepository.save(Item(name = "item"))
        val itemsJson = objectMapper.writeValueAsString(listOf(item))

        mockMvc.perform(get(apiPath))
            .andExpect(content().json(itemsJson))
            .andExpect(status().isOk)
    }

    @Test
    fun `should return 404 when getting non-existing item`() {
        mockMvc.perform(get("$apiPath/999")).andExpect(status().isNotFound)
    }

    @Test
    fun `should return item by id and ok status`() {
        val item = itemRepository.save(Item(name = "item"))
        val itemJson = objectMapper.writeValueAsString(item)

        mockMvc.perform(get("$apiPath/${item.id}")).andExpect(content().json(itemJson))
            .andExpect(status().isOk)
    }

    @Test
    fun `should return 404 when updating non-existing item`() {
        mockMvc.perform(
            put("$apiPath/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ItemUpdateDto("updated item")))
        )
            .andExpect(status().isNotFound)
    }

    @Test
    fun `should update an item and return it with ok status`() {
        val item = itemRepository.save(Item(name = "item"));
        val name = "updated item"
        lateinit var updatedItem: Item

        mockMvc.perform(
            put("$apiPath/${item.id}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ItemUpdateDto(name)))
        ).andExpect {
            val items = itemRepository.findAll()
            assertEquals(1, items.count())
            updatedItem = items.first()
            assertEquals(name, updatedItem.name)
        }
            .andExpect(content().json(objectMapper.writeValueAsString(updatedItem)))
            .andExpect(status().isOk)
    }

    @Test
    fun `should return 404 when deleting non-existing item`() {
        mockMvc.perform(
            delete("$apiPath/999")
        )
            .andExpect(status().isNotFound)
    }

    @Test
    fun `should delete an item and return no content status`() {
        val item = itemRepository.save(Item(name = "item"))

        mockMvc.perform(
            delete("$apiPath/${item.id}")
        )
            .andExpect(status().isNoContent)

        assertEquals(0, itemRepository.count())
    }
}