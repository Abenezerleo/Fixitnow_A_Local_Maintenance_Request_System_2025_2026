package com.example.allapp.Model.enums

enum class ServiceType(val serviceName: String) {
    PLUMBING("Plumbing"),
    ELECTRICAL("Electrical"),
    CARPENTRY("Carpentry"),
    PAINTING("Painting"),
    CLEANING("Cleaning"),
    GARDENING("Gardening"),
    MECHANICAL("Mechanical"),
    OTHER("Other");

    companion object {
        fun fromString(name: String): ServiceType {
            return values().find { it.serviceName.equals(name, ignoreCase = true) }
                ?: throw IllegalArgumentException("Unknown service type: $name")
        }
    }
} 