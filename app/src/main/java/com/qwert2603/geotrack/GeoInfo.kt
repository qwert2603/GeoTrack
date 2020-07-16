package com.qwert2603.geotrack

import androidx.lifecycle.LiveData
import androidx.room.*

@Entity
class GeoInfo(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val lat: Double,
    val lon: Double,
    @ColumnInfo(index = true) val millis: Long
)

@Dao
interface GeoInfoDao {
    @Insert
    suspend fun insertGeoInfo(geoInfo: GeoInfo)

    @Query("SELECT * FROM GeoInfo ORDER BY millis DESC")
    suspend fun getAllGeoInfos(): List<GeoInfo>

    @Query("SELECT * FROM GeoInfo ORDER BY millis DESC LIMIT 1")
    fun observeLastGeoInfo(): LiveData<GeoInfo>

    @Query("SELECT COUNT(*) FROM GeoInfo")
    fun observeGeoInfosCount(): LiveData<Int>

    @Query("DELETE FROM GeoInfo")
    suspend fun deleteAllGeoInfos()
}

@Database(entities = [GeoInfo::class], version = 2, exportSchema = false)
abstract class GeoInfoDatabase : RoomDatabase() {
    abstract fun getInfoDao(): GeoInfoDao
}