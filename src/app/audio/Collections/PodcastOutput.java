package app.audio.Collections;

import app.audio.Files.Episode;
import lombok.Getter;

import java.util.ArrayList;

/**
 * Represents the output data for a podcast, including its name and a list of episode names.
 */
@Getter
public class PodcastOutput {
    private final String name;
    private final ArrayList<String> episodes;

    /**
     * Constructs a PodcastOutput object based on a Podcast, extracting its name and episode names.
     *
     * @param podcast The Podcast object to create the output for.
     */
    public PodcastOutput(final Podcast podcast) {
        this.name = podcast.getName();
        this.episodes = new ArrayList<>();
        for (Episode episode : podcast.getEpisodes()) {
            episodes.add(episode.getName());
        }
    }
}
