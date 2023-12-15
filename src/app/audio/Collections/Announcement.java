package app.audio.Collections;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents an announcement with a name and description.
 */

@Setter
@Getter
public class Announcement {
    private final String name;
    private final String description;

    /**
     * Constructs an Announcement object with the specified name and description.
     *
     * @param name        The name of the announcement.
     * @param description The description of the announcement.
     */
    public Announcement(final String name, final String description) {
        this.name = name;
        this.description = description;
    }
}
