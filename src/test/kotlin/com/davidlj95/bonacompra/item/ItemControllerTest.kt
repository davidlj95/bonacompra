package com.davidlj95.bonacompra.item

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
import kotlin.test.assertEquals


@SpringBootTest
@AutoConfigureMockMvc
class ItemControllerTest {
    val apiPath = "/items"
    
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var itemRepository: ItemRepository

    val item = Item(1, "item")

    @Test
    fun `should create an item and return it with created status`() {
        mockMvc.perform(
            post(apiPath)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"name": "${item.name}"}""")
        )
            .andDo {
                val items = itemRepository.findAll()
                assertEquals(1, items.size)
                assertEquals(item, items.first())
            }
            .andExpect(jsonPath("$.id").value(item.id))
            .andExpect(jsonPath("$.name").value(item.name))
            .andExpect(status().isCreated)
    }

    @Test
    fun `should return list of items and ok status`() {
        val item = itemRepository.save(item);

        mockMvc.perform(get(apiPath))
            .andExpect(jsonPath("$.[0].id").value(item.id))
            .andExpect(jsonPath("$.[0].name").value(item.name))
            .andExpect(status().isOk)
    }
}