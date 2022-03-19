package uz.context.cardapplication.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface Dao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertCard(cardEntity: CardEntity)

    @Query("SELECT * FROM CardTable")
    fun getAllCards(): List<CardEntity>

    @Query("DELETE FROM CardTable")
    fun deleteAllData()
}