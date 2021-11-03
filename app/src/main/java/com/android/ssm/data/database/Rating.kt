package com.android.ssm.data.database
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rating")
data class Rating(val id: String,
    val rating: Float,
    @PrimaryKey(autoGenerate = true) val uid: Int? = null)