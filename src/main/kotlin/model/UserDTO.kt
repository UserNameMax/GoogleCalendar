package model

data class UserDTO(
    val id: String,         // UUIDv4
    val fullName: String,
    val active: Boolean,
    val role: String,
    val avatarUrl: String,
    val integrations: List<IntegrationDTO>?
)
