package uz.context.cardapplication.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.Serializable

@Entity(tableName = "CardTable")
data class CardEntity(
    val cardNumber: String,
    val cardDate1: Int,
    val cardDate2: Int,
    val cardHolder: String,
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
): Serializable

//class TypeConvertor {
//    @TypeConverter
//    fun listToJson(value: List<CardEntity>?) = Gson().toJson(value)
//
//    @TypeConverter
//    fun jsonToList(value: String) = Gson().fromJson(value, Array<CardEntity>::class.java).toList()
//}