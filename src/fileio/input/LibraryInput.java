package fileio.input;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Setter
@Getter
public final class LibraryInput {
    private ArrayList<SongInput> songs;
    private ArrayList<PodcastInput> podcasts;
    private ArrayList<UserInput> users;

    public LibraryInput() {
    }

    @Override
    public String toString() {
        return "LibraryInput{"
                + "songs=" + songs
                + ", podcasts=" + podcasts
                + ", users=" + users
                + '}';
    }
}
