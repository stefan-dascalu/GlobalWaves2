package app.pages;

import app.user.User;

import java.util.stream.Collectors;

/**
 * Represents a 'Host' page for a user within the application.
 * This page is specifically designed for users who host podcasts,
 * allowing them to view and manage their podcasts and announcements.
 *
 * <p>The page compiles and displays details about the user's podcasts
 * and their episodes, as well as any announcements they've made. It relies
 * on the {@link User} class to fetch this data.
 */
public class Host extends Page {
    private final User user;

    /**
     * Constructs a Host page for a specified user.
     *
     * @param user The user who is a host and whose content is to be displayed on this page.
     */
    public Host(final User user) {
        super(user.getUsername());
        this.user = user;
    }

    /**
     * Generates and returns the content of the Host page.
     * The content includes a detailed list of podcasts with their episodes
     * and a list of announcements, each formatted for easy reading.
     *
     * @return A string representing the content of the Host page,
     * including podcast details and announcements.
     */
    @Override
    public String getContent() {
        String podcastsContent = user.getPodcasts().stream()
                .map(podcast -> podcast.getName() + ": ["
                        + podcast.getEpisodes().stream()
                        .map(episode -> episode.getName() + " - "
                                + episode.getDescription())
                        .collect(Collectors.joining(", "))
                        + "]")
                .collect(Collectors.joining("\n"));

        String announcementsContent = user.getAnnouncements().stream()
                .map(announcement -> announcement.getName() + ": "
                        + announcement.getDescription())
                .collect(Collectors.joining("\n"));

        return "Podcasts:\n" + podcastsContent + "\n\nAnnouncements:\n" + announcementsContent;
    }
}
