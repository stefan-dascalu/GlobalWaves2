package app.audio.Collections;

import app.audio.Files.Song;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

/**
 * Represents an album, a collection of songs, with associated
 * details such as the release year and artist.
 */

@Getter
public final class Album extends AudioCollection {
    @Getter
    @Setter
    private ArrayList<Song> songs;
    private final int releaseYear;
    @Setter
    @Getter
    private String artist;

    /**
     * Constructs an Album object with the specified name, owner, and release year.
     *
     * @param name        The name of the album.
     * @param owner       The owner or creator of the album.
     * @param releaseYear The year in which the album was released.
     */
    public Album(final String name, final String owner, final int releaseYear) {
        super(name, owner);
        this.songs = new ArrayList<>();
        this.releaseYear = releaseYear;
    }

    /**
     * Get the number of tracks in the album.
     *
     * @return The number of tracks in the album.
     */
    @Override
    public int getNumberOfTracks() {
        return songs.size();
    }

    /**
     * Get a track from the album by its index.
     *
     * @param index The index of the track to retrieve.
     * @return The Song object representing the track.
     */
    @Override
    public Song getTrackByIndex(final int index) {
        return songs.get(index);
    }
}
