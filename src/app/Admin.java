package app;

import app.audio.Collections.Album;
import app.audio.Collections.Playlist;
import app.audio.Collections.Podcast;
import app.audio.Files.Episode;
import app.audio.Files.Song;
import app.user.User;
import fileio.input.EpisodeInput;
import fileio.input.PodcastInput;
import fileio.input.SongInput;
import fileio.input.UserInput;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The type Admin.
 */
public final class Admin {
    @Getter
    private static List<User> users = new ArrayList<>();
    @Getter
    private static List<Song> songs = new ArrayList<>();
    private static List<Podcast> podcasts = new ArrayList<>();
    @Getter
    private static List<Album> albums = new ArrayList<>();
    private static int timestamp = 0;
    private static final int LIMIT = 5;

    private Admin() {
    }

    private static Admin instance = null;

    /**
     * Singleton.
     */
    public static Admin getInstance() {
        if (instance == null) {
            synchronized (Admin.class) {
                if (instance == null) {
                    instance = new Admin();
                }
            }
        }
        return instance;
    }

    /**
     * Sets users.
     *
     * @param userInputList the user input list
     */
    public static void setUsers(final List<UserInput> userInputList) {
        users = new ArrayList<>();
        for (UserInput userInput : userInputList) {
            users.add(new User(userInput.getUsername(), userInput.getAge(),
                    userInput.getCity(), userInput.getType()));
        }
    }

    /**
     * Sets songs.
     *
     * @param songInputList the song input list
     */
    public static void setSongs(final List<SongInput> songInputList) {
        songs = new ArrayList<>();
        for (SongInput songInput : songInputList) {
            songs.add(new Song(songInput.getName(), songInput.getDuration(), songInput.getAlbum(),
                    songInput.getTags(), songInput.getLyrics(), songInput.getGenre(),
                    songInput.getReleaseYear(), songInput.getArtist()));
        }
    }


    /**
     * Sets podcasts.
     *
     * @param podcastInputList the podcast input list
     */
    public static void setPodcasts(final List<PodcastInput> podcastInputList) {
        podcasts = new ArrayList<>();
        for (PodcastInput podcastInput : podcastInputList) {
            List<Episode> episodes = new ArrayList<>();
            for (EpisodeInput episodeInput : podcastInput.getEpisodes()) {
                episodes.add(new Episode(episodeInput.getName(),
                                         episodeInput.getDuration(),
                                         episodeInput.getDescription()));
            }
            podcasts.add(new Podcast(podcastInput.getName(), podcastInput.getOwner(), episodes));
        }
    }

    /**
     * Gets podcasts.
     *
     * @return the podcasts
     */
    public static List<Podcast> getPodcasts() {
        return new ArrayList<>(podcasts);
    }

    /**
     * Gets playlists.
     *
     * @return the playlists
     */
    public static List<Playlist> getPlaylists() {
        List<Playlist> playlists = new ArrayList<>();
        for (User user : users) {
            playlists.addAll(user.getPlaylists());
        }
        return playlists;
    }

    /**
     * Gets user.
     *
     * @param username the username
     * @return the user
     */
    public static User getUser(final String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Update timestamp.
     *
     * @param newTimestamp the new timestamp
     */
    public static void updateTimestamp(final int newTimestamp) {
        int elapsed = newTimestamp - timestamp;
        timestamp = newTimestamp;
        if (elapsed == 0) {
            return;
        }

        for (User user : users) {
            user.simulateTime(elapsed);
        }
    }

    /**
     * Gets top 5 songs.
     *
     * @return the top 5 songs
     */
    public static List<String> getTop5Songs() {
        List<Song> sortedSongs = new ArrayList<>(songs);
        sortedSongs.sort(Comparator.comparingInt(Song::getLikes).reversed());
        List<String> topSongs = new ArrayList<>();
        int count = 0;
        for (Song song : sortedSongs) {
            if (count >= LIMIT) {
                break;
            }
            topSongs.add(song.getName());
            count++;
        }
        return topSongs;
    }

    /**
     * Gets top 5 playlists.
     *
     * @return the top 5 playlists
     */
    public static List<String> getTop5Playlists() {
        List<Playlist> sortedPlaylists = new ArrayList<>(getPlaylists());
        sortedPlaylists.sort(Comparator.comparingInt(Playlist::getFollowers)
                .reversed()
                .thenComparing(Playlist::getTimestamp, Comparator.naturalOrder()));
        List<String> topPlaylists = new ArrayList<>();
        int count = 0;
        for (Playlist playlist : sortedPlaylists) {
            if (count >= LIMIT) {
                break;
            }
            topPlaylists.add(playlist.getName());
            count++;
        }
        return topPlaylists;
    }

    /**
     * Reset.
     */
    public static void reset() {
        users = new ArrayList<>();
        songs = new ArrayList<>();
        podcasts = new ArrayList<>();
        timestamp = 0;
    }

    public static void setAlbums(final List<Album> albumList) {
        albums = new ArrayList<>(albumList);
    }

    /**
     * Retrieves the top 5 most liked albums from the system.
     * This method first calculates the total likes for each album by summing up the
     * likes of all songs in each album.
     * It then sorts the albums based on the total number of likes (in descending order)
     * and alphabetically by album name as a tiebreaker.
     * The method returns a list of album names, limited to the top 5 most liked albums.
     *
     * @return A list of strings representing the names of the top 5 most liked albums.
     */
    public static List<String> getTop5Albums() {
        List<Album> albums = new ArrayList<>(getAlbums());

        // Map to store total likes for each album
        Map<Album, Integer> albumLikes = new HashMap<>();
        for (Album album : albums) {
            int totalLikes = album.getSongs().stream()
                    .mapToInt(Song::getLikes)
                    .sum();
            albumLikes.put(album, totalLikes);
        }

        // Set to store unique album names, preserving the insertion order
        Set<String> uniqueAlbumNames = new LinkedHashSet<>();

        // Sort albums by total likes (descending) and then by name, and add to set
        albums.stream()
                .sorted(Comparator.comparingInt((Album a) -> albumLikes.get(a)).reversed()
                        .thenComparing(Album::getName))
                .forEachOrdered(album -> uniqueAlbumNames.add(album.getName()));

        return uniqueAlbumNames.stream()
                .limit(LIMIT)
                .collect(Collectors.toList());
    }

    /**
     * Removes an album from the system after ensuring that it is
     * not currently being played by any user.
     * The method iterates through all users to check if any of
     * them are playing the specified album.
     * If the album is not being played, it attempts to remove
     * it from the list of albums.
     * The album can only be removed by the artist who owns it.
     *
     * @param albumName  The name of the album to be removed.
     * @param artistName The name of the artist who owns the album.
     * @return A message indicating the success or failure of the operation.
     */
    public static String removeAlbum(final String albumName, final String artistName) {
        User artistUser = getUser(artistName);

        // Check if the user exists and is an artist
        if (artistUser == null || !artistUser.isArtist()) {
            return artistName + " is not an artist or doesn't exist.";
        }

        // Check if any user is playing the specified album
        for (User user : users) {
            if (user.isPlayingAlbum(albumName)) {
                return artistName + " can't delete this album.";
            }
        }

        boolean removed = albums.removeIf(album -> album.getName().equalsIgnoreCase(albumName));
        if (removed) {
            return "Album '" + albumName + "' removed successfully.";
        } else {
            return "Album '" + albumName + "' not found.";
        }
    }

    /**
     * Adds a single user to the list of users.
     *
     * @param user the user to be added
     */
    public static void addUser(final User user) {
        if (user != null && !users.contains(user)) {
            users.add(user);
        }
    }

    /**
     * Retrieves a list of all users in the system.
     * This method simply collects the usernames of all users stored in the system,
     * regardless of their roles or online status.
     *
     * @return List<String> - A list of usernames representing all users.
     */
    public static List<String> getAllUsers() {
        return users.stream().map(User::getUsername).collect(Collectors.toList());
    }

    /**
     * Retrieves a list of all users who are currently online and are
     * neither hosts nor artists.
     * This method checks each user's connection status and type to determine
     * if they are online and a regular user.
     *
     * @return A list of User objects representing online regular users.
     */
    public static List<User> getOnlineUsers() {
        // List to store online regular users
        List<User> onlineUsers = new ArrayList<>();

        // Check each user's status and type
        for (User user : users) {
            if (user.isConnected() && !Objects.equals(user.getType(), "host")
                    && !Objects.equals(user.getType(), "artist")) {
                onlineUsers.add(user);
            }
        }

        return onlineUsers;
    }

    /**
     * Adds a podcast in the list.
     *
     * @param podcast the podcast to be added
     */
    public static void addPodcast(final Podcast podcast) {
        podcasts.add(podcast);
    }

    /**
     * Removes a podcast from the system after ensuring that it
     * is not currently being played by any user.
     * The method iterates through all users to check if any of
     * them are playing the specified podcast.
     * If the podcast is not being played, it attempts to
     * remove it from the list of podcasts.
     *
     * @param podcastName The name of the podcast to be removed.
     * @return A message indicating the success or failure of the operation.
     */
    public static String removePodcast(final String podcastName) {
        // Check if any user is playing the specified podcast
        for (User user : users) {
            if (user.isPlayingPodcast(podcastName)) {
                return "Cannot remove the podcast '" + podcastName
                        + "' as it is currently being played.";
            }
        }

        boolean removed = podcasts.removeIf(podcast -> podcast.getName().
                equalsIgnoreCase(podcastName));
        if (removed) {
            return "Podcast '" + podcastName + "' removed successfully.";
        } else {
            return "Podcast '" + podcastName + "' not found.";
        }
    }
}
