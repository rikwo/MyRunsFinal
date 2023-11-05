package com.example.ricky_kwong_myruns2

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "run_table")
data class Run(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    @ColumnInfo(name = "date_column")
    val date: String,
    @ColumnInfo(name = "time_column")
    val time: String,
    @ColumnInfo(name = "duration_column")
    val duration: String,
    @ColumnInfo(name = "distance_column")
    val distance: String,
    @ColumnInfo(name = "calories_column")
    val calories: String,
    @ColumnInfo(name = "heart_rate_column")
    val heartRate: String,
    @ColumnInfo(name = "comment_column")
    val comment: String,)