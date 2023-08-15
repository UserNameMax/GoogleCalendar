import model.BookingDTO
import model.RecurrenceDTO
import model.UserDTO
import model.WorkspaceDTO
import utils.GoogleCalendarConverter.toGoogleEvent
import java.util.Calendar
import java.util.GregorianCalendar

fun main(args: Array<String>) {
    val dto = getDtoFromClient()
    val googleEvent = dto.toGoogleEvent()
    println("$googleEvent")
}

fun getDtoFromClient() = BookingDTO(
    owner = UserDTO(
        id = "",
        fullName = "ownerName",
        active = false,
        role = "",
        avatarUrl = "",
        integrations = listOf()
    ),
    participants = listOf(
        UserDTO(
            id = "",
            fullName = "guest1",
            active = false,
            role = "",
            avatarUrl = "",
            integrations = listOf()
        ), UserDTO(id = "", fullName = "guest2", active = false, role = "", avatarUrl = "", integrations = listOf())
    ),
    workspace = WorkspaceDTO(id = "", name = "Room", utilities = listOf(), zone = null),
    id = "",
    beginBooking = GregorianCalendar().timeInMillis,
    endBooking = GregorianCalendar().apply { add(Calendar.HOUR, 1) }.timeInMillis,
    recurrence = RecurrenceDTO(interval = 3, freq = "DAILY", count = 5)
)