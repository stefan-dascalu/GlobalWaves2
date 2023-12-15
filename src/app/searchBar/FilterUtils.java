package app.searchBar;

import app.audio.Collections.Album;
import app.audio.Files.Song;
import app.audio.LibraryEntry;
import app.user.User;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Filter utils.
 */
public final class FilterUtils {

    private FilterUtils() {
    }

    /**
     * Filter by name list.
     *
     * @param entries the entries
     * @param name    the name
     * @return the list
     */
    public static List<LibraryEntry> filterByName(final List<LibraryEntry> entries,
                                                  final String name) {
        List<LibraryEntry> result = new ArrayList<>();
        for (LibraryEntry entry : entries) {
            if (entry.matchesName(name)) {
                result.add(entry);
            }
        }
        return result;
    }

    /**
     * Filter by album list.
     *
     * @param entries the entries
     * @param album   the album
     * @return the list
     */
    public static List<LibraryEntry> filterByAlbum(final List<LibraryEntry> entries,
                                                   final String album) {
        return filter(entries, entry -> entry.matchesAlbum(album));
    }

    /**
     * Filter by tags list.
     *
     * @param entries the entries
     * @param tags    the tags
     * @return the list
     */
    public static List<LibraryEntry> filterByTags(final List<LibraryEntry> entries,
                                                  final ArrayList<String> tags) {
        return filter(entries, entry -> entry.matchesTags(tags));
    }

    /**
     * Filter by lyrics list.
     *
     * @param entries the entries
     * @param lyrics  the lyrics
     * @return the list
     */
    public static List<LibraryEntry> filterByLyrics(final List<LibraryEntry> entries,
                                                    final String lyrics) {
        return filter(entries, entry -> entry.matchesLyrics(lyrics));
    }

    /**
     * Filter by genre list.
     *
     * @param entries the entries
     * @param genre   the genre
     * @return the list
     */
    public static List<LibraryEntry> filterByGenre(final List<LibraryEntry> entries,
                                                   final String genre) {
        return filter(entries, entry -> entry.matchesGenre(genre));
    }

    /**
     * Filter by artist list.
     *
     * @param entries the entries
     * @param artist  the artist
     * @return the list
     */
    public static List<LibraryEntry> filterByArtist(final List<LibraryEntry> entries,
                                                    final String artist) {
        return filter(entries, entry -> entry.matchesArtist(artist));
    }

    /**
     * Filter by release year list.
     *
     * @param entries     the entries
     * @param releaseYear the release year
     * @return the list
     */
    public static List<LibraryEntry> filterByReleaseYear(final List<LibraryEntry> entries,
                                                         final String releaseYear) {
        return filter(entries, entry -> entry.matchesReleaseYear(releaseYear));
    }

    /**
     * Filter by owner list.
     *
     * @param entries the entries
     * @param user    the user
     * @return the list
     */
    public static List<LibraryEntry> filterByOwner(final List<LibraryEntry> entries,
                                                   final String user) {
        return filter(entries, entry -> entry.matchesOwner(user));
    }

    /**
     * Filter by playlist visibility list.
     *
     * @param entries the entries
     * @param user    the user
     * @return the list
     */
    public static List<LibraryEntry> filterByPlaylistVisibility(final List<LibraryEntry> entries,
                                                                final String user) {
        return filter(entries, entry -> entry.isVisibleToUser(user));
    }

    /**
     * Filter users by name.
     *
     * @param users the users
     * @param name the name
     * @return the list
     */
    public static List<User> filterUsersByName(final List<User> users, final String name) {
        List<User> filteredUsers = new ArrayList<>();
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(name)) {
                filteredUsers.add(user);
            }
        }
        return filteredUsers;
    }
    /**
     * Filter by followers list.
     *
     * @param entries   the entries
     * @param followers the followers
     * @return the list
     */
    public static List<LibraryEntry> filterByFollowers(final List<LibraryEntry> entries,
                                                       final String followers) {
        return filter(entries, entry -> entry.matchesFollowers(followers));
    }

    private static List<LibraryEntry> filter(final List<LibraryEntry> entries,
                                             final FilterCriteria criteria) {
        List<LibraryEntry> result = new ArrayList<>();
        for (LibraryEntry entry : entries) {
            if (criteria.matches(entry)) {
                result.add(entry);
            }
        }
        return result;
    }

    @FunctionalInterface
    private interface FilterCriteria {
        /**
         * Matches boolean.
         *
         * @param entry the entry
         * @return the boolean
         */
        boolean matches(LibraryEntry entry);
    }

    /**
     * Filters a list of library entries (albums and songs) based on specified criteria.
     * This method examines each entry in the provided list and applies filters to determine
     * if an album or its songs should be included in the filtered list.
     *
     * @param entries The list of library entries (albums and songs) to be filtered.
     * @param filters The filters to be applied, including criteria like name, artist, etc.
     * @return A list of LibraryEntry objects that match the filtering criteria.
     */
    public static List<LibraryEntry> filterAlbumsAndSongs(final List<LibraryEntry> entries,
                                                          final Filters filters) {
        List<LibraryEntry> filteredEntries = new ArrayList<>();

        for (LibraryEntry entry : entries) {
            if (entry instanceof Album album) {
                // Filter albums based on provided filters
                if (matchesAlbumFilters(album, filters)) {
                    filteredEntries.add(album);

                    // Filter songs on the album based on provided filters
                    for (Song song : album.getSongs()) {
                        if (matchesSongFilters(song, filters)) {
                            filteredEntries.add(song);
                        }
                    }
                }
            }
        }

        return filteredEntries;
    }

    /**
     * Checks if an album matches specified filter criteria.
     *
     * @param album   The album to be checked.
     * @param filters The filter criteria to apply.
     * @return True if the album matches the filter criteria, false otherwise.
     */
    private static boolean matchesAlbumFilters(final Album album, final Filters filters) {
        return (filters.getName() == null || album.getName().equalsIgnoreCase(filters.getName()))
                && (filters.getArtist() == null
                || album.getArtist().equalsIgnoreCase(filters.getArtist()))
                && (filters.getReleaseYear() == null
                || String.valueOf(album.getReleaseYear()).equals(filters.getReleaseYear()));
    }

    /**
     * Checks if a song matches specified filter criteria.
     *
     * @param song    The song to be checked.
     * @param filters The filter criteria to apply.
     * @return True if the song matches the filter criteria, false otherwise.
     */
    private static boolean matchesSongFilters(final Song song, final Filters filters) {
        return (filters.getGenre() == null || song.getGenre().equalsIgnoreCase(filters.getGenre()))
                && (filters.getTags() == null || song.getTags().containsAll(filters.getTags()))
                && (filters.getLyrics() == null || song.getLyrics().contains(filters.getLyrics()));
    }


}
