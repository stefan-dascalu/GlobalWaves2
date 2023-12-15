package app.pages;

import app.user.User;

import java.util.stream.Collectors;

/**
 * Represents a 'Liked Content' page for a user within the application.
 * This page displays content liked by the user, including songs and playlists.
 *
 * <p>The page aggregates and formats the user's liked songs and followed playlists,
 * presenting them in an easily readable format. It relies on the {@link User} class
 * to fetch the necessary data.
 */
public class LikedContent extends Page {
    private final User user;

    /**
     * Constructs a LikedContent page for a specified user.
     *
     * @param user The user whose liked content is to be displayed.
     */
    public LikedContent(final User user) {
        super(user.getUsername());
        this.user = user;
    }

    /**
     * Generates and returns the content of the LikedContent page.
     * The content includes lists of liked songs and followed playlists,
     * each formatted as "Name - Artist/Owner".
     *
     * @return A string representing the content of the LikedContent page.
     */
    public String getContent() {
        String likedSongs = this.user.getLikedSongs().stream()
                .map(song -> song.getName() + " - " + song.getArtist())
                .collect(Collectors.joining(", "));

        String followedPlaylists = this.user.getFollowedPlaylists().stream()
                .map(playlist -> playlist.getName() + " - " + playlist.getOwner())
                .collect(Collectors.joining(", "));

        return "Liked songs:\n\t[" + likedSongs + "]\n\nFollowed playlists:\n\t["
                + followedPlaylists + "]";
    }
}
