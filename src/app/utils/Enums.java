package app.utils;

public class Enums {
    public enum Genre {
        POP,
        ROCK,
        RAP
    }

    public enum Visibility {
        PUBLIC,
        PRIVATE
    }

    public enum SearchType {
        SONG,
        PLAYLIST,
        PODCAST
    }

    public enum PageType {
        HOME,
        LIKED_CONTENT,
        ARTIST,
        HOST
    }

    public enum RepeatMode {
        REPEAT_ALL, REPEAT_ONCE, REPEAT_INFINITE, REPEAT_CURRENT_SONG, NO_REPEAT,
    }

    public enum PlayerSourceType {
        LIBRARY, PLAYLIST, PODCAST, ALBUM
    }
}
