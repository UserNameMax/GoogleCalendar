import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.client.util.store.FileDataStoreFactory
import com.google.api.services.calendar.Calendar
import com.google.api.services.calendar.CalendarScopes
import com.google.auth.http.HttpCredentialsAdapter
import com.google.auth.oauth2.GoogleCredentials
import model.BookingDTO
import model.RecurrenceDTO
import model.UserDTO
import model.WorkspaceDTO
import utils.GoogleCalendarConverter.toGoogleEvent
import java.io.FileInputStream
import java.io.InputStreamReader
import java.util.*


fun main(args: Array<String>) {
    val dto = getDtoFromClient()
    val googleEvent = dto.toGoogleEvent()
    println("$googleEvent")

    val httpTransport = GoogleNetHttpTransport.newTrustedTransport()!!
    val inputStream = FileInputStream("/home/max/IdeaProjects/GoogleCalendar/src/main/resources/authcheck.json")
    val googleCredentials = GoogleCredentials.fromStream(inputStream).createScoped(CalendarScopes.CALENDAR_READONLY)
    val service = Calendar.Builder(httpTransport, jsonFactory, HttpCredentialsAdapter(googleCredentials))
        .setApplicationName("APPLICATION_NAME")
        .build()

    service.events().list("302736bc03ecced245b8916f0d48a2ca8c00dd129fa697f44308f27b96f4b348@group.calendar.google.com")


    println(service.events().list("302736bc03ecced245b8916f0d48a2ca8c00dd129fa697f44308f27b96f4b348@group.calendar.google.com").execute().items.first())
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
    endBooking = GregorianCalendar().apply { add(java.util.Calendar.HOUR, 1) }.timeInMillis,
    recurrence = RecurrenceDTO(interval = 3, freq = "DAILY", count = 5)
)

val jsonFactory = GsonFactory.getDefaultInstance()!!
private fun getCred(httpTransport: NetHttpTransport?): Credential? {
    /*
    * InputStream in = SheetsQuickstart.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
if (in == null) {
        throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
}
GoogleCredentials googleCredentials = GoogleCredentials.fromStream(in).createScoped(SCOPES);

Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpCredentialsAdapter(googleCredentials))
                .setApplicationName(APPLICATION_NAME)
                .build();*/
    val inputStream = FileInputStream("/home/max/IdeaProjects/GoogleCalendar/src/main/resources/authcheck.json")
    val googleCredentials = GoogleCredentials.fromStream(inputStream).createScoped(CalendarScopes.CALENDAR_READONLY)

    val clientSecrets = GoogleClientSecrets.load(jsonFactory, InputStreamReader(inputStream))
    val flow = GoogleAuthorizationCodeFlow.Builder(
        httpTransport, jsonFactory, clientSecrets, listOf(CalendarScopes.CALENDAR_READONLY)
    )
        .setDataStoreFactory(FileDataStoreFactory(java.io.File("tokens")))
        .setAccessType("offline")
        .build()
    val receiver = LocalServerReceiver.Builder().setPort(8888).build()
    val credential = AuthorizationCodeInstalledApp(flow, receiver).authorize("user")
    return credential
}