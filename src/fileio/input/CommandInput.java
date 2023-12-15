package fileio.input;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public final class CommandInput {
    private String command;
    private String username;
    private String city;
    private List<SongInput> songs;
    private List<EpisodeInput> episodes;
    private String date;
    private double price;

    private Integer timestamp;
    private int age;
    private String name;
    private String artistName;
    private Integer releaseYear;
    private String type;
    private FiltersInput filters;
    private Integer itemNumber;
    private Integer repeatMode;
    private Integer playlistId;
    private String playlistName;
    private Integer seed;


    private String description;

    public CommandInput() {
    }

    @Override
    public String toString() {
        return "CommandInput{"
                + "command='" + command + '\''
                + ", username='" + username + '\''
                + ", timestamp=" + timestamp
                + ", type='" + type + '\''
                + ", filters=" + filters
                + ", itemNumber=" + itemNumber
                + ", repeatMode=" + repeatMode
                + ", playlistId=" + playlistId
                + ", playlistName='" + playlistName + '\''
                + ", name='" + name + '\''
                + ", seed=" + seed
                + '}';
    }

}
