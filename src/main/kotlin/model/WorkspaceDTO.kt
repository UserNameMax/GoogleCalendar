package model

data class WorkspaceDTO(
    val id: String,
    val name: String,
    val utilities: List<UtilityDTO>,
    val zone: WorkspaceZoneDTO? = null
)
