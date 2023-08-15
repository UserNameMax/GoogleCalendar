package utils

import com.google.api.client.util.DateTime
import com.google.api.services.calendar.model.Event
import com.google.api.services.calendar.model.Event.Organizer
import com.google.api.services.calendar.model.EventAttendee
import com.google.api.services.calendar.model.EventDateTime
import model.BookingDTO
import model.Recurrence.Companion.toRecurrence
import model.UserDTO
import model.WorkspaceDTO
import utils.RecurrenceRuleFactory.rule
import java.text.SimpleDateFormat

object GoogleCalendarConverter {
    fun Event.toBookingDTO(): BookingDTO {
        TODO()
    }

    fun BookingDTO.toGoogleEvent(): Event {
        val dto = this
        return Event().apply {
            organizer = owner.toGoogleOrganizer()
            attendees = participants.map { it.toAttendee() }
            location = workspace.toLocation()
            recurrence = listOf(dto.recurrence.toRecurrence().rule())
            start = beginBooking.toGoogleDateTime()
            end = endBooking.toGoogleDateTime()
        }
    }

    private fun Long.toGoogleDateTime(): EventDateTime? {
        return EventDateTime().also {
            it.dateTime = DateTime(this)
            it.timeZone = "Asia/Omsk"
        }
    }

    private fun WorkspaceDTO.toLocation(): String? {
        return name  //TODO i don know how it cast
    }

    private fun UserDTO.toAttendee(): EventAttendee {
        return EventAttendee().also {
            it.email = "" //TODO need email
            it.displayName = fullName
        }
    }

    private fun UserDTO.toGoogleOrganizer(): Event.Organizer? {
        return Organizer().also {
            it.email = "" //TODO need email
            it.displayName = fullName
        }
    }
}