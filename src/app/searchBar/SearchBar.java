package app.searchBar;


import app.Admin;
import app.audio.Collections.Album;
import app.audio.LibraryEntry;
import app.user.User;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static app.searchBar.FilterUtils.filterByTags;
import static app.searchBar.FilterUtils.filterAlbumsAndSongs;
import static app.searchBar.FilterUtils.filterByOwner;
import static app.searchBar.FilterUtils.filterByName;
import static app.searchBar.FilterUtils.filterByAlbum;
import static app.searchBar.FilterUtils.filterByLyrics;
import static app.searchBar.FilterUtils.filterByReleaseYear;
import static app.searchBar.FilterUtils.filterByFollowers;
import static app.searchBar.FilterUtils.filterByPlaylistVisibility;
import static app.searchBar.FilterUtils.filterByGenre;
import static app.searchBar.FilterUtils.filterByArtist;

/**
 * The type Search bar.
 */
public final class SearchBar {
    private List<LibraryEntry> results;
    private List<User> userResults;
    private final String user;
    private static final Integer MAX_RESULTS = 5;
    @Getter
    private String lastSearchType;

    @Getter
    private LibraryEntry lastSelected;

    /**
     * Instantiates a new Search bar.
     *
     * @param user the user
     */
    public SearchBar(final String user) {
        this.results = new ArrayList<>();
        this.user = user;
        this.userResults = new ArrayList<>();
    }

    /**
     * Clear selection.
     */
    public void clearSelection() {
        lastSelected = null;
        lastSearchType = null;
    }

    /**
     * Searches for users based on the specified filters and user type.
     * The search is performed differently for artists and hosts, depending
     * on the type parameter.
     * The results are limited to a maximum number as defined by MAX_RESULTS.
     *
     * @param filters The filters to be applied in the search process. These may
     *                include criteria like name, genre, etc.
     * @param type    The type of users to search for. It can be "artist" or "host".
     *                Depending on this type, the search will be tailored to
     *                either artists or hosts.
     * @return A list of users that match the search criteria and type, limited to MAX_RESULTS.
     */
    public List<User> searchUsers(final Filters filters, final String type) {
        List<User> entries = new ArrayList<>();

        if ("artist".equals(type)) {
            entries = searchArtists(filters);
        } else if ("host".equals(type)) {
            entries = searchHosts(filters);
        }

        this.userResults = entries.stream().limit(MAX_RESULTS).collect(Collectors.toList());

        return this.userResults;
    }

    /**
     * Searches for artists within the system based on specified filters.
     * This method filters all users in the system to find those who are artists.
     * It then applies additional filtering based on the provided filter criteria.
     *
     * @param filters The filters to be applied to the search. Can include filters such as name.
     * @return A list of User objects representing artists that match the search criteria.
     */
    private List<User> searchArtists(final Filters filters) {
        List<User> artists = Admin.getUsers().stream()
                .filter(User::isArtist)
                .collect(Collectors.toList());

        if (filters.getName() != null) {
            artists = FilterUtils.filterUsersByName(artists, filters.getName());
        }

        return artists;
    }

    /**
     * Searches for hosts within the system based on specified filters.
     * This method filters all users in the system to find those who are hosts.
     * It then applies additional filtering based on the provided filter criteria.
     *
     * @param filters The filters to be applied to the search. Can include filters such as name.
     * @return A list of User objects representing hosts that match the search criteria.
     */
    private List<User> searchHosts(final Filters filters) {
        List<User> hosts = Admin.getUsers().stream()
                .filter(User::isHost)
                .collect(Collectors.toList());

        if (filters.getName() != null) {
            hosts = FilterUtils.filterUsersByName(hosts, filters.getName());
        }

        return hosts;
    }

    /**
     * Search list.
     *
     * @param filters the filters
     * @param type    the type
     * @return the list
     */
    public List<LibraryEntry> search(final Filters filters, final String type) {
        List<LibraryEntry> entries;

        switch (type) {
            case "song":
                entries = new ArrayList<>(Admin.getSongs());

                if (filters.getName() != null) {
                    entries = filterByName(entries, filters.getName());
                }

                if (filters.getAlbum() != null) {
                    entries = filterByAlbum(entries, filters.getAlbum());
                }

                if (filters.getTags() != null) {
                    entries = filterByTags(entries, filters.getTags());
                }

                if (filters.getLyrics() != null) {
                    entries = filterByLyrics(entries, filters.getLyrics());
                }

                if (filters.getGenre() != null) {
                    entries = filterByGenre(entries, filters.getGenre());
                }

                if (filters.getReleaseYear() != null) {
                    entries = filterByReleaseYear(entries, filters.getReleaseYear());
                }

                if (filters.getArtist() != null) {
                    entries = filterByArtist(entries, filters.getArtist());
                }

                break;
            case "playlist":
                entries = new ArrayList<>(Admin.getPlaylists());

                entries = filterByPlaylistVisibility(entries, user);

                if (filters.getName() != null) {
                    entries = filterByName(entries, filters.getName());
                }

                if (filters.getOwner() != null) {
                    entries = filterByOwner(entries, filters.getOwner());
                }

                if (filters.getFollowers() != null) {
                    entries = filterByFollowers(entries, filters.getFollowers());
                }

                break;
            case "podcast":
                entries = new ArrayList<>(Admin.getPodcasts());

                if (filters.getName() != null) {
                    entries = filterByName(entries, filters.getName());
                }

                if (filters.getOwner() != null) {
                    entries = filterByOwner(entries, filters.getOwner());
                }

                break;
            case "album":
                entries = new ArrayList<>(Admin.getAlbums());

                entries = filterAlbumsAndSongs(entries, filters);

                if (filters.getName() != null) {
                    entries = filterByName(entries, filters.getName());
                }

                if (filters.getOwner() != null) {
                    entries = filterByOwner(entries, filters.getOwner());
                }

                if (filters.getFollowers() != null) {
                    entries = filterByFollowers(entries, filters.getFollowers());
                }

                Set<String> uniqueAlbumNames = new HashSet<>();

                entries.removeIf(entry -> !(entry instanceof Album)
                        || !uniqueAlbumNames.add(entry.getName()));

                break;
            default:
                entries = new ArrayList<>();
        }

        while (entries.size() > MAX_RESULTS) {
            entries.remove(entries.size() - 1);
        }

        this.results = entries;
        this.lastSearchType = type;
        return this.results;
    }

    /**
     * Select library entry.
     *
     * @param itemNumber the item number
     * @return the library entry
     */
    public LibraryEntry select(final Integer itemNumber) {
        if (this.results.size() < itemNumber) {
            results.clear();

            return null;
        } else {
            lastSelected =  this.results.get(itemNumber - 1);
            results.clear();

            return lastSelected;
        }
    }

    /**
     * Select a user based on the item number from the user search results.
     *
     * @param itemNumber the item number to select
     * @return the selected user or null if the item number is invalid
     */
    public User selectUser(final Integer itemNumber) {
        if (this.userResults.size() < itemNumber) {
            userResults.clear();
            return null;
        } else {
            User selectedUser = this.userResults.get(itemNumber - 1);
            userResults.clear();
            return selectedUser;
        }
    }
}
