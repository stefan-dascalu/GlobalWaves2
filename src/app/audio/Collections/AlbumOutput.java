package app.audio.Collections;

import lombok.Getter;

import java.util.ArrayList;

/**
 * Represents an output representation of an album with a name and songs.
 */

@Getter
public class AlbumOutput {
    private final String name;
    private final ArrayList<String> songs;

    /**
     * Constructs an AlbumOutput object based on the provided Album.
     *
     * @param album The Album object from which to create the output.
     */
    public AlbumOutput(final Album album) {
        this.name = album.getName();
        this.songs = new ArrayList<>();
        for (int i = 0; i < album.getSongs().size(); i++) {
            songs.add(album.getSongs().get(i).getName());
        }
    }
}
