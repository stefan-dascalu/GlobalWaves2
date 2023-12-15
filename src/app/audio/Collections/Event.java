package app.audio.Collections;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Represents an event with a name, date, and description.
 */

@Setter
@Getter
public class Event {
    private final String name;
    private final LocalDate date;
    private final String description;

    /**
     * Constructs an Event object with the specified name, date, and description.
     *
     * @param name        The name of the event.
     * @param date        The date of the event.
     * @param description The description of the event.
     */
    public Event(final String name, final LocalDate date, final String description) {
        this.name = name;
        this.date = date;
        this.description = description;
    }
}
