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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
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

    val item = Item(1, "item")

    @BeforeEach
    fun setUp() {
        itemRepository.deleteAll()
    }

    @Test
    fun `should create an item and return it with created status`() {
        val itemJson = objectMapper.writeValueAsString(item)

        mockMvc.perform(
            post(apiPath)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ItemCreateDto(item.name)))
        )
            .andExpect(content().json(itemJson))
            .andExpect(status().isCreated)

        val items = itemRepository.findAll()
        assertEquals(1, items.size)
        assertEquals(item, items.first())
    }

    @Test
    fun `should not create an item if text is empty`() {
        mockMvc.perform(
            post(apiPath)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ItemCreateDto("  ")))
        )
            .andExpect(status().is4xxClientError)

        val items = itemRepository.findAll()
        assertEquals(0, items.size)
    }

    @Test
    fun `should return list of items and ok status`() {
        val item = itemRepository.save(item);
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
        val itemJson = objectMapper.writeValueAsString(item)
        itemRepository.save(item)

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
        val item = itemRepository.save(item);
        val name = "updated item"
        val updatedItemJson = objectMapper.writeValueAsString(item.copy(name = name))

        mockMvc.perform(
            put("$apiPath/${item.id}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ItemUpdateDto(name)))
        )
            .andExpect(content().json(updatedItemJson))
            .andExpect(status().isOk)

        val items = itemRepository.findAll()
        assertEquals(1, items.size)
        val actualItem = items.first()
        assertEquals(name, actualItem.name)
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
        itemRepository.save(item)

        mockMvc.perform(
            delete("$apiPath/${item.id}")
        )
            .andExpect(status().isNoContent)

        assertEquals(0, itemRepository.findAll().size)
    }
}