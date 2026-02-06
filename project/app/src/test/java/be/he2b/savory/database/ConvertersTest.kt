package be.he2b.savory.database

import org.junit.Assert.assertEquals
import org.junit.Test

class ConvertersTest {

    private val converters = Converters()

    @Test
    fun fromList_convertsListToStringCorrectly() {
        val inputList = listOf("Beef", "Potato", "Onion")
        val result = converters.fromList(inputList)
        assertEquals("Beef||Potato||Onion", result)
    }

    @Test
    fun toList_convertsStringToListCorrectly() {
        val inputString = "Beef||Potato||Onion"

        val result = converters.toList(inputString)

        assertEquals(3, result.size)
        assertEquals("Beef", result[0])
        assertEquals("Onion", result[2])
    }

    @Test
    fun fromList_handleEmptyList() {
        val result = converters.fromList(emptyList())
        assertEquals("", result)
    }

    @Test
    fun toList_handleEmptyString() {
        val result = converters.toList("")
        assert(result.isEmpty() || result[0].isBlank())
    }
}