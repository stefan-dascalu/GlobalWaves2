package app.pages;

import app.user.User;

import java.util.stream.Collectors;

/**
 * Represents the 'Artist' page within the application, dedicated to a user
 * who is categorized as an artist. This page displays the artist's albums,
 * merchandise, and events, offering a comprehensive view of their artistic
 * output and engagements.
 *
 * <p>Each section of the page is meticulously crafted to provide a snapshot
 * of the artist's current and past works, along with upcoming events and
 * available merchandise, enabling fans and followers to stay updated with
 * the artist's latest endeavors.
 */
public class Artist extends Page {
    private final User artist;

    /**
     * Constructs an Artist page for a specific artist user.
     * This constructor initializes the page with the artist's information.
     *
     * @param artist The artist user for whom the page is being created.
     */
    public Artist(final User artist) {
        super(artist.getUsername());
        this.artist = artist;
    }

    /**
     * Generates and returns the content of the Artist page.
     * This includes a list of the artist's albums, merchandise, and events,
     * formatted for easy reading and quick access.
     *
     * @return A string representing the content of the Artist page,
     * encompassing albums, merchandise, and events related to the artist.
     */
    @Override
    public String getContent() {
        String albumsContent = artist.getAlbums().stream()
                .map(album -> album.getName() + " (" + album.getReleaseYear()
                        + ")")
                .collect(Collectors.joining(", "));

        String merchandiseContent = artist.getMerchandiseList().stream()
                .map(merch -> merch.getName() + " - " + merch.getPrice() + ": "
                        + merch.getDescription())
                .collect(Collectors.joining(", "));

        String eventsContent = artist.getEvents().stream()
                .map(event -> event.getName() + " (" + event.getDate() + "): "
                        + event.getDescription())
                .collect(Collectors.joining(", "));

        return "Albums:\n" + albumsContent + "\n\nMerchandise:\n" + merchandiseContent
                + "\n\nEvents:\n" + eventsContent;
    }
}
