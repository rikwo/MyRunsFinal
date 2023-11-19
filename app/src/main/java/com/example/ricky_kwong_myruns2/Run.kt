package com.example.ricky_kwong_myruns2

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "run_table")
data class Run(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,
    @ColumnInfo(name = "entry_type_column")
    var entryType: String = "",
    @ColumnInfo(name = "activity_type_column")
    var activityType: String = "",
    @ColumnInfo(name = "date_time_column")
    var dateTime: String = "",
    @ColumnInfo(name = "duration_column")
    var duration: Float = 0F,
    @ColumnInfo(name = "distance_column")
    var distance: Float = 0F,
    @ColumnInfo(name = "calories_column")
    var calories: Int = 0,
    @ColumnInfo(name = "heart_rate_column")
    var heartRate: Int = 0,
    @ColumnInfo(name = "comment_column")
    var comment: String = "")