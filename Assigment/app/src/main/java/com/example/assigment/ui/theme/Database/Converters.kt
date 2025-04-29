package com.example.assigment.ui.theme.Database

import androidx.room.TypeConverter
import com.example.assigment.ui.theme.Enum.ServiceType
import com.example.assigment.ui.theme.Enum.StatusType
import java.util.Date

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromServiceType(value: ServiceType): String {
        return value.name
    }

    @TypeConverter
    fun toServiceType(value: String): ServiceType {
        return ServiceType.valueOf(value)
    }

    @TypeConverter
    fun fromStatusType(value: StatusType): String {
        return value.name
    }

    @TypeConverter
    fun toStatusType(value: String): StatusType {
        return StatusType.valueOf(value)
    }
} 