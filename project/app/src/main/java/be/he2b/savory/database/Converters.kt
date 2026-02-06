package be.he2b.savory.database

import androidx.room.TypeConverter

/**
 * Helps Room save lists by turning them into simple text.
 * For example: ["Eggs", "Milk"] becomes "Eggs||Milk" so they can be stored in the database
 */
class Converters {
    @TypeConverter
    fun fromList(list: List<String>?): String {
        return list?.joinToString("||") ?: ""
    }

    @TypeConverter
    fun toList(data: String?): List<String> {
        return data?.split("||")?.map { it.trim() } ?: emptyList()
    }

}