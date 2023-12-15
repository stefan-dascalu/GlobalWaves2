package app.user;

import app.Admin;
import app.audio.Collections.AudioCollection;
import app.audio.Collections.PlaylistOutput;
import app.audio.Collections.Album;
import app.audio.Collections.AlbumOutput;
import app.audio.Collections.Podcast;
import app.audio.Collections.PodcastOutput;
import app.audio.Collections.Event;
import app.audio.Collections.Announcement;
import app.audio.Collections.Merchandise;
import app.audio.Collections.Playlist;

import app.audio.Files.AudioFile;
import app.audio.Files.Episode;
import app.audio.Files.Song;
import app.audio.LibraryEntry;
//import app.pages.Host;
//import app.pages.Home;
//import app.pages.LikedContent;
//import app.pages.Artist;
//import app.pages.Page;
import app.player.Player;
import app.player.PlayerStats;
import app.searchBar.Filters;
import app.searchBar.SearchBar;
import app.utils.Enums;
import fileio.input.EpisodeInput;
import fileio.input.SongInput;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The type User.
 */
public class User {
    @Getter
    private String username;
    @Getter
    private int age;
    @Getter
    private String city;
    @Getter
    private ArrayList<Playlist> playlists;
    @Getter
    private ArrayList<Song> likedSongs;
    @Setter
    @Getter
    private ArrayList<Album> albums;
    @Getter
    private ArrayList<Playlist> followedPlaylists;
    @Getter
    private final List<Merchandise> merchandiseList;
    @Getter
    private final List<Event> events;
    @Getter
    private final List<Announcement> announcements;
    @Getter
    private final List<Podcast> createdPodcasts;
    private final Player player;
    private final SearchBar searchBar;
    private boolean lastSearched;
    @Getter
    private final String type;
    @Setter
    private boolean isConnected;


    /**
     * Instantiates a new User.
     *
     * @param username the username
     * @param age      the age
     * @param city     the city
     */
    public User(final String username, final int age, final String city, final String type) {
        this.username = username;
        this.age = age;
        this.city = city;
        this.type = type;
        playlists = new ArrayList<>();
        albums = new ArrayList<>();
        likedSongs = new ArrayList<>();
        followedPlaylists = new ArrayList<>();
        player = new Player();
        searchBar = new SearchBar(username);
        lastSearched = false;
        isConnected = true;
        this.merchandiseList = new ArrayList<>();
        this.events = new ArrayList<>();
        this.announcements = new ArrayList<>();
        this.createdPodcasts = new ArrayList<>();
    }

    /**
     * Search array list.
     *
     * @param filters the filters
     * @param type    the type
     * @return the array list
     */
    public ArrayList<String> search(final Filters filters, final String type) {
        if (!isConnected) {
            return new ArrayList<>();
        }

        searchBar.clearSelection();
        player.stop();

        lastSearched = true;
        ArrayList<String> results = new ArrayList<>();

        if ("artist".equalsIgnoreCase(this.type) || "host".equalsIgnoreCase(this.type)) {
            List<User> users = searchBar.searchUsers(filters, type);
            for (User user : users) {
                results.add(user.getUsername());
            }
        } else {
            List<LibraryEntry> libraryEntries = searchBar.search(filters, type);
            for (LibraryEntry libraryEntry : libraryEntries) {
                results.add(libraryEntry.getName());
            }
        }

        return results;
    }

    /**
     * Select string.
     *
     * @param itemNumber the item number
     * @return the string
     */
    public String select(final int itemNumber) {
        if (!isConnected) {
            return "User is offline. Cannot make a selection.";
        }
        if (!lastSearched) {
            return "Please conduct a search before making a selection.";
        }

        lastSearched = false;

        if ("artist".equalsIgnoreCase(this.type) || "host".equalsIgnoreCase(this.type)) {
            User selectedUser = searchBar.selectUser(itemNumber);

            if (selectedUser == null) {
                return "The selected ID is too high.";
            }

            return "Successfully selected user: " + selectedUser.getUsername();
        } else {
            LibraryEntry selected = searchBar.select(itemNumber);

            if (selected == null) {
                return "The selected ID is too high.";
            }

            return "Successfully selected %s.".formatted(selected.getName());
        }
    }

    /**
     * Load string.
     *
     * @return the string
     */
    public String load() {
        if (!isConnected) {
            return "User is offline. Cannot load the source.";
        }

        if (searchBar.getLastSelected() == null) {
            return "Please select a source before attempting to load.";
        }

        if (!searchBar.getLastSearchType().equals("song")
                && ((AudioCollection) searchBar.getLastSelected()).getNumberOfTracks() == 0) {
            return "You can't load an empty audio collection!";
        }

        player.setSource(searchBar.getLastSelected(), searchBar.getLastSearchType());
        searchBar.clearSelection();

        player.pause();

        return "Playback loaded successfully.";
    }

    /**
     * Play pause string.
     *
     * @return the string
     */
    public String playPause() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before attempting to pause or resume playback.";
        }

        player.pause();

        if (player.getPaused()) {
            return "Playback paused successfully.";
        } else {
            return "Playback resumed successfully.";
        }
    }

    /**
     * Repeat string.
     *
     * @return the string
     */
    public String repeat() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before setting the repeat status.";
        }

        Enums.RepeatMode repeatMode = player.repeat();
        String repeatStatus;

        switch (repeatMode) {
            case NO_REPEAT -> repeatStatus = "no repeat";
            case REPEAT_ONCE -> repeatStatus = "repeat once";
            case REPEAT_ALL -> repeatStatus = "repeat all";
            case REPEAT_INFINITE -> repeatStatus = "repeat infinite";
            case REPEAT_CURRENT_SONG -> repeatStatus = "repeat current song";
            default -> repeatStatus = "";
        }

        return "Repeat mode changed to %s.".formatted(repeatStatus);
    }

    /**
     * Shuffle string.
     *
     * @param seed the seed
     * @return the string
     */
    public String shuffle(final Integer seed) {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before using the shuffle function.";
        }

        if (!player.getType().equals("playlist")) {
            return "The loaded source is not a playlist or an album.";
        }

        player.shuffle(seed);

        if (player.getShuffle()) {
            return "Shuffle function activated successfully.";
        }
        return "Shuffle function deactivated successfully.";
    }

    /**
     * Forward string.
     *
     * @return the string
     */
    public String forward() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before attempting to forward.";
        }

        if (!player.getType().equals("podcast")) {
            return "The loaded source is not a podcast.";
        }

        player.skipNext();

        return "Skipped forward successfully.";
    }

    /**
     * Backward string.
     *
     * @return the string
     */
    public String backward() {
        if (player.getCurrentAudioFile() == null) {
            return "Please select a source before rewinding.";
        }

        if (!player.getType().equals("podcast")) {
            return "The loaded source is not a podcast.";
        }

        player.skipPrev();

        return "Rewound successfully.";
    }

    /**
     * Like string.
     *
     * @return the string
     */
    public String like() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before liking or unliking.";
        }

        if (!player.getType().equals("song") && !player.getType().equals("playlist")) {
            return "Loaded source is not a song.";
        }

        Song song = (Song) player.getCurrentAudioFile();

        if (likedSongs.contains(song)) {
            likedSongs.remove(song);
            song.dislike();
            return "Unlike registered successfully.";
        }

        if (!isConnected()) {
            return username + " is offline.";
        } else {
            likedSongs.add(song);
            song.like();
            return "Like registered successfully.";
        }
    }

    /**
     * Next string.
     *
     * @return the string
     */
    public String next() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before skipping to the next track.";
        }

        player.next();

        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before skipping to the next track.";
        }

        return "Skipped to next track successfully. The current track is %s."
                .formatted(player.getCurrentAudioFile().getName());
    }

    /**
     * Prev string.
     *
     * @return the string
     */
    public String prev() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before returning to the previous track.";
        }

        player.prev();

        return "Returned to previous track successfully. The current track is %s."
                .formatted(player.getCurrentAudioFile().getName());
    }

    /**
     * Create playlist string.
     *
     * @param name      the name
     * @param timestamp the timestamp
     * @return the string
     */
    public String createPlaylist(final String name, final int timestamp) {
        if (playlists.stream().anyMatch(playlist -> playlist.getName().equals(name))) {
            return "A playlist with the same name already exists.";
        }

        playlists.add(new Playlist(name, username, timestamp));

        return "Playlist created successfully.";
    }

    /**
     * Add remove in playlist string.
     *
     * @param id the id
     * @return the string
     */
    public String addRemoveInPlaylist(final int id) {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before adding to or removing from the playlist.";
        }

        if (player.getType().equals("podcast")) {
            return "The loaded source is not a song.";
        }

        if (id > playlists.size()) {
            return "The specified playlist does not exist.";
        }

        Playlist playlist = playlists.get(id - 1);

        if (playlist.containsSong((Song) player.getCurrentAudioFile())) {
            playlist.removeSong((Song) player.getCurrentAudioFile());
            return "Successfully removed from playlist.";
        }

        playlist.addSong((Song) player.getCurrentAudioFile());
        return "Successfully added to playlist.";
    }

    /**
     * Switch playlist visibility string.
     *
     * @param playlistId the playlist id
     * @return the string
     */
    public String switchPlaylistVisibility(final Integer playlistId) {
        if (playlistId > playlists.size()) {
            return "The specified playlist ID is too high.";
        }

        Playlist playlist = playlists.get(playlistId - 1);
        playlist.switchVisibility();

        if (playlist.getVisibility() == Enums.Visibility.PUBLIC) {
            return "Visibility status updated successfully to public.";
        }

        return "Visibility status updated successfully to private.";
    }

    /**
     * Show playlists array list.
     *
     * @return the array list
     */
    public ArrayList<PlaylistOutput> showPlaylists() {
        ArrayList<PlaylistOutput> playlistOutputs = new ArrayList<>();
        for (Playlist playlist : playlists) {
            playlistOutputs.add(new PlaylistOutput(playlist));
        }

        return playlistOutputs;
    }

    /**
     * Follow string.
     *
     * @return the string
     */
    public String follow() {
        LibraryEntry selection = searchBar.getLastSelected();
        String type = searchBar.getLastSearchType();

        if (selection == null) {
            return "Please select a source before following or unfollowing.";
        }

        if (!type.equals("playlist")) {
            return "The selected source is not a playlist.";
        }

        Playlist playlist = (Playlist) selection;

        if (playlist.getOwner().equals(username)) {
            return "You cannot follow or unfollow your own playlist.";
        }

        if (followedPlaylists.contains(playlist)) {
            followedPlaylists.remove(playlist);
            playlist.decreaseFollowers();

            return "Playlist unfollowed successfully.";
        }

        followedPlaylists.add(playlist);
        playlist.increaseFollowers();


        return "Playlist followed successfully.";
    }

    /**
     * Gets player stats.
     *
     * @return the player stats
     */
    public PlayerStats getPlayerStats() {
        PlayerStats currentStats = player.getStats();
        boolean pausedStatus = currentStats.isPaused();

        if (!isConnected) {
            pausedStatus = false;
        }

        Enums.RepeatMode repeatMode = getRepeatModeFromString(currentStats.getRepeat());

        return new PlayerStats(currentStats.getName(), currentStats.getRemainedTime(),
                repeatMode, currentStats.isShuffle(), pausedStatus);
    }


    /**
     * Show preferred songs array list.
     *
     * @return the array list
     */
    public ArrayList<String> showPreferredSongs() {
        ArrayList<String> results = new ArrayList<>();
        for (AudioFile audioFile : likedSongs) {
            results.add(audioFile.getName());
        }

        return results;
    }

    /**
     * Gets preferred genre.
     *
     * @return the preferred genre
     */
    public String getPreferredGenre() {
        String[] genres = {"pop", "rock", "rap"};
        int[] counts = new int[genres.length];
        int mostLikedIndex = -1;
        int mostLikedCount = 0;

        for (Song song : likedSongs) {
            for (int i = 0; i < genres.length; i++) {
                if (song.getGenre().equals(genres[i])) {
                    counts[i]++;
                    if (counts[i] > mostLikedCount) {
                        mostLikedCount = counts[i];
                        mostLikedIndex = i;
                    }
                    break;
                }
            }
        }

        String preferredGenre = mostLikedIndex != -1 ? genres[mostLikedIndex]
                : "unknown";
        return "This user's preferred genre is %s.".formatted(preferredGenre);
    }

    /**
     * Simulate time.
     *
     * @param time the time
     */
    public void simulateTime(final int time) {
        player.simulatePlayer(time);
    }

    /**
     * Converts a string representation of a repeat mode into the
     * corresponding Enums.RepeatMode value.
     * This method maps specific string patterns to their associated Enums.RepeatMode values.
     *
     * @param repeatString The string representation of the repeat mode.
     *                     Expected values are "Repeat All", "Repeat Once", "Repeat Infinite",
     *                     and "Repeat Current Song". Any other value will
     *                     default to Enums.RepeatMode.NO_REPEAT.
     * @return The Enums.RepeatMode value that corresponds to the provided string.
     */
    private Enums.RepeatMode getRepeatModeFromString(final String repeatString) {
        return switch (repeatString) {
            case "Repeat All" -> Enums.RepeatMode.REPEAT_ALL;
            case "Repeat Once" -> Enums.RepeatMode.REPEAT_ONCE;
            case "Repeat Infinite" -> Enums.RepeatMode.REPEAT_INFINITE;
            case "Repeat Current Song" -> Enums.RepeatMode.REPEAT_CURRENT_SONG;
            default -> Enums.RepeatMode.NO_REPEAT;
        };
    }

    /**
     * Check if it is connected.
     */
    public boolean isConnected() {
        return isConnected;
    }

    /**
     * Switches the connection status of the user.
     * Only available for regular users, not artists or hosts.
     *
     * @return a message indicating the new connection status or an error message.
     */
    public String switchConnectionStatus() {
        if ("artist".equalsIgnoreCase(this.type) || "host".equalsIgnoreCase(this.type)) {
            return "This operation is not available for artists or hosts.";
        }

        isConnected = !isConnected;
        player.setConnectionStatus(isConnected);
        return username + " has changed status successfully.";
    }

    /**
     * Adds a new album to the user's collection.
     * Verifies if the album name is unique and checks for
     * duplicate songs within the album.
     *
     * @param albumName   The name of the new album.
     * @param releaseYear The release year of the album.
     * @param songInputs  A list of SongInput objects representing the
     *                    songs to be added to the album.
     * @return A message indicating the success or failure of the album addition.
     */
    public String addAlbum(final String albumName, final int releaseYear,
                           final List<SongInput> songInputs) {
        if (type == null || type.equals("host")) {
            return username + " is not an artist.";
        }
        if (albums.stream().anyMatch(a -> a.getName().equals(albumName))) {
            return username + " has another album with the same name.";
        }

        Set<String> uniqueSongNames = new HashSet<>();
        for (SongInput songInput : songInputs) {
            if (!uniqueSongNames.add(songInput.getName())) {
                return username + " has the same song at least twice in this album.";
            }
        }

        Album newAlbum = getAlbum(albumName, releaseYear, songInputs);
        albums.add(newAlbum);
        Admin.getAlbums().add(newAlbum);
        return username + " has added new album successfully.";
    }

    private Album getAlbum(String albumName, int releaseYear, List<SongInput> songInputs) {
        List<Song> songs = Admin.getSongs();
        ArrayList<Song> songs1 = new ArrayList<>();
        Album newAlbum = new Album(albumName, this.username, releaseYear);
        for (SongInput songInput : songInputs) {
            Song newSong = new Song(
                    songInput.getName(), songInput.getDuration(),
                    albumName, songInput.getTags(), songInput.getLyrics(),
                    songInput.getGenre(), songInput.getReleaseYear(),
                    songInput.getArtist()
            );
            songs.add(newSong);
            songs1.add(newSong);
        }
        newAlbum.setSongs(songs1);
        return newAlbum;
    }

    /**
     * Shows the albums associated with the user.
     * Converts each album to an AlbumOutput format for display or further processing.
     *
     * @return An ArrayList of AlbumOutput objects representing the user's albums.
     */
    public ArrayList<AlbumOutput> showAlbums() {
        ArrayList<AlbumOutput> albumOutputs = new ArrayList<>();
        for (Album album : albums) {
            albumOutputs.add(new AlbumOutput(album));
        }
        return albumOutputs;
    }

    /**
     * Checks if the current audio file being played is a song from a specified album.
     * Verifies if the currently playing audio file is a Song instance and then
     * compares its album name with the provided album name.
     *
     * @param albumName The name of the album to check against the currently playing song's album.
     * @return true if the currently playing audio file is a song from the
     * specified album, false otherwise.
     */
    public boolean isPlayingAlbum(final String albumName) {
        if (player.getCurrentAudioFile() instanceof Song currentSong) {
            return currentSong.getAlbum().equalsIgnoreCase(albumName);
        }
        return false;
    }

    /**
     * Check if it is artist.
     */
    public boolean isArtist() {
        return "artist".equalsIgnoreCase(this.type);
    }

    /**
     * Check if it is host.
     */
    public boolean isHost() {
        return "host".equalsIgnoreCase(this.type);
    }
    /**
     * Checks if merchandise with a given name already exists.
     *
     * @param merchandiseName The name of the merchandise to check.
     * @return true if merchandise with the same name exists, false otherwise.
     */

    /**
     * Checks if merchandise with a given name already exists.
     *
     * @param merchandiseName The name of the merchandise to check.
     * @return true if merchandise with the same name exists, false otherwise.
     */
    public boolean doesMerchandiseExist(final String merchandiseName) {
        return merchandiseList.stream()
                .anyMatch(merch -> merch.getName().equalsIgnoreCase(merchandiseName));
    }

    /**
     * Add a new merch for the user.
     *
     * @param name        Name of the article.
     * @param description Description of the article.
     * @param price       Price of the article.
     * @return Message.
     */
    public String addMerchandise(final String name, final String description, final double price) {
        Merchandise newMerch = new Merchandise(name, description, price);
        if (!this.isArtist()) {
            return username + " is not an artist.";
        }
        if (price < 0) {
            return "Price for merchandise can not be negative.";
        }
        if (doesMerchandiseExist(name)) {
            return username + " has merchandise with the same name.";
        }
        merchandiseList.add(newMerch);
        return username + " has added new merchandise successfully.";
    }

    /**
     * Add a new event for user.
     *
     * @param name        Name of the event.
     * @param date        Event date.
     * @param description Description of the event.
     */
    public void addEvent(final String name, final LocalDate date, final String description) {
        Event newEvent = new Event(name, date, description);
        events.add(newEvent);
    }

    /**
     * Add a new announcement for user.
     *
     * @param name        Name of the announcement.
     * @param description Description of the announcement.
     * @return Message.
     */
    public String addAnnouncement(final String name, final String description) {
        if (!this.isHost()) {
            return username + " is not a host.";
        }

        for (Announcement announcement : announcements) {
            if (announcement.getName().equalsIgnoreCase(name)) {
                return username + " has already added an announcement with this name.";
            }
        }

        Announcement newAnnouncement = new Announcement(name, description);
        announcements.add(newAnnouncement);

        return username + " has successfully added new announcement.";
    }

    /**
     * Check if it has unique.
     *
     * @param eventName name.
     */
    public boolean hasEventWithName(final String eventName) {
        return events.stream().anyMatch(event -> event.getName().equalsIgnoreCase(eventName));
    }

    /**
     * Remove event.
     *
     * @param eventName event.
     */
    public String removeEvent(final String eventName) {
        for (int i = 0; i < events.size(); i++) {
            if (events.get(i).getName().equalsIgnoreCase(eventName)) {
                events.remove(i);
                return "Event '" + eventName + "' has been removed successfully.";
            }
        }
        return "Event '" + eventName + "' not found.";
    }

    /**
     * Remove announcement.
     *
     * @param announcementName announcement.
     */
    public String removeAnnouncement(final String announcementName) {
        for (int i = 0; !announcements.isEmpty(); i++) {
            if (announcements.get(i).getName().equalsIgnoreCase(announcementName)) {
                announcements.remove(i);
                return "Announcement '" + announcementName
                        + "' has been removed successfully.";
            }
        }
        return "Announcement '" + announcementName + "' not found.";
    }

    /**
     * Adds a new podcast with episodes to the user's collection and Admins list.
     * Ensures that each episode in the podcast has a unique name.
     *
     * @param type         The user's type (artist or host).
     * @param podcastName  The name of the new podcast.
     * @param episodeInputs List of EpisodeInput objects representing the episodes to be added.
     * @return A message indicating the success or failure of the podcast addition.
     */
    public String addPodcast(final String type, final String podcastName,
                             final List<EpisodeInput> episodeInputs) {
        if (type == null || type.equals("artist")) {
            return username + " is not a host.";
        }

        if (createdPodcasts.stream().anyMatch(p -> p.getName().equals(podcastName))) {
            return username + " has another podcast with the same name.";
        }

        Set<String> episodeNames = new HashSet<>();
        List<Episode> episodes = new ArrayList<>();
        for (EpisodeInput episodeInput : episodeInputs) {
            // Check for duplicate episode names
            if (!episodeNames.add(episodeInput.getName())) {
                return username + " has the same episode in this podcast.";
            }
            Episode newEpisode = new Episode(episodeInput.getName(),
                    episodeInput.getDuration(), episodeInput.getDescription());
            episodes.add(newEpisode);
        }

        Podcast newPodcast = new Podcast(podcastName, username, episodes);
        createdPodcasts.add(newPodcast);
        Admin.addPodcast(newPodcast);

        return username + " has added new podcast successfully.";
    }

    /**
     * Shows the podcasts associated with the user.
     * Converts each podcast to an podcastOutput format for display or further processing.
     *
     * @return An ArrayList of PodcastOutput objects representing the user's podcasts.
     */
    public ArrayList<PodcastOutput> showPodcasts() {
        ArrayList<PodcastOutput> podcastOutputs = new ArrayList<>();
        for (Podcast podcast : this.createdPodcasts) {
            podcastOutputs.add(new PodcastOutput(podcast));
        }
        return podcastOutputs;
    }

    /**
     * Checks if a podcast with a given name is currently being played by the user.
     * This method determines if the current audio file being played is an
     * episode belonging to the specified podcast.
     *
     * @param podcastName The name of the podcast to check for current playback.
     * @return true if the current audio file is an episode of the
     * specified podcast, false otherwise.
     */
    public boolean isPlayingPodcast(final String podcastName) {
        if (player.getCurrentAudioFile() instanceof Episode currentEpisode) {
            return createdPodcasts.stream()
                    .filter(podcast -> podcast.getName().equalsIgnoreCase(podcastName))
                    .anyMatch(podcast -> podcast.getEpisodes().contains(currentEpisode));
        }
        return false;
    }

    /**
     * Get podcast
     *
     * @return podcast list.
     */
    public List<Podcast> getPodcasts() {
        return createdPodcasts;
    }
}
