import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.calendar.Calendar
import com.google.api.services.calendar.CalendarScopes
import com.google.auth.http.HttpCredentialsAdapter
import com.google.auth.oauth2.GoogleCredentials
import model.*
import model.Recurrence.Companion.toRecurrence
import utils.GoogleCalendarConverter.toGoogleEvent
import utils.RecurrenceRuleFactory.getRecurrence
import utils.RecurrenceRuleFactory.rule
import java.io.FileInputStream
import java.util.*


fun main(args: Array<String>) {
    val dto = getDtoFromClient()
    val googleEvent = dto.toGoogleEvent()
    println(googleEvent)
    println(RecurrenceDTO(interval = 3, freq = "DAILY", count = 5).toRecurrence().rule().getRecurrence().toDto())

    val service = service()

    service.events().insert("maxim.mishchenko@effective.band",googleEvent).execute() //здесь ссылка на спец календарь должна быть

    println(
        service.events()
            .list("maxim.mishchenko@effective.band")
            .execute().items.first() {
                val date = it?.start?.dateTime.toString() ?: ""
                date.contains("2023-04-18")
            }
    )
}

fun getDtoFromClient() = BookingDTO(
    owner = UserDTO(
        id = "",
        fullName = "ownerName",
        active = false,
        role = "",
        avatarUrl = "",
        integrations = listOf(IntegrationDTO("","email","olga.belozerova@effective.band"))
    ),
    participants = listOf(UserDTO(
        id = "",
        fullName = "ownerName",
        active = false,
        role = "",
        avatarUrl = "",
        integrations = listOf(IntegrationDTO("","email","maxim.mishchenko@effective.band"))
    )),
    workspace = WorkspaceDTO(id = "", name = "Sun", utilities = listOf(), zone = null),
    id = "",
    beginBooking = GregorianCalendar().timeInMillis,
    endBooking = GregorianCalendar().apply { add(java.util.Calendar.HOUR, 1) }.timeInMillis,
    recurrence = null//RecurrenceDTO(interval = 3, freq = "DAILY", count = 5)
)

fun service(): Calendar {
    val jsonFactory = GsonFactory.getDefaultInstance()!!
    val httpTransport = GoogleNetHttpTransport.newTrustedTransport()!!
    val inputStream = FileInputStream("/home/max/IdeaProjects/GoogleCalendar/src/main/resources/effective-office.json")
    val googleCredentials = GoogleCredentials.fromStream(inputStream).createScoped(CalendarScopes.CALENDAR).createDelegated("maxim.mishchenko@effective.band") // здесь нужно подставить другую почту

    return Calendar.Builder(httpTransport, jsonFactory, HttpCredentialsAdapter(googleCredentials))
        .setApplicationName("APPLICATION_NAME")
        .build()
}