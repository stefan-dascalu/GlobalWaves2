package app.pages;

import app.audio.Collections.Playlist;
import app.audio.Files.Song;
import app.user.User;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents the 'Home' page of a user within the application.
 * This page is the primary landing page for users, showcasing a quick overview
 * of their liked songs and followed playlists.
 *
 * <p>The content is dynamically generated based on the user's interactions
 * with songs and playlists, displaying their most recent or most preferred choices.
 * The page is designed to provide users with immediate access to their favorite content.
 */
public class Home extends Page {
    private static final int LIMIT = 5;
    private final User user;

    /**
     * Constructs a Home page for a specified user.
     *
     * @param user The user for whom the Home page content is to be generated.
     */
    public Home(final User user) {
        super(user.getUsername());
        this.user = user;
    }

    /**
     * Generates and returns the content of the Home page.
     * This includes a list of the user's top liked songs and followed playlists,
     * each limited to a specified number of entries for brevity.
     *
     * @return A string representing the content of the Home page, featuring
     * the user's liked songs and followed playlists.
     */
    public String getContent() {

        List<String> likedSongs = user.getLikedSongs().stream()
                .map(Song::getName)
                .limit(LIMIT)
                .collect(Collectors.toList());

        List<String> followedPlaylists = user.getFollowedPlaylists().stream()
                .map(Playlist::getName)
                .limit(LIMIT)
                .collect(Collectors.toList());

        String songs = String.join(", ", likedSongs);
        String playlists = String.join(", ", followedPlaylists);

        return "Liked songs:\n\t[" + songs + "]\n\nFollowed playlists:\n\t[" + playlists + "]";
    }
}
