package com.example.allapp.Model.enums

enum class RoleType(val roleName: String) {
    REQUESTER("Requester"),
    PROVIDER("Provider"),
    ADMIN("Admin");

    companion object {
        fun fromString(name: String): RoleType {
            return values().find { it.roleName.equals(name, ignoreCase = true) }
                ?: throw IllegalArgumentException("Unknown role: $name")
        }
    }
} 