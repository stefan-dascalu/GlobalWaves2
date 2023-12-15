package app;

import app.audio.Collections.*;
import app.player.PlayerStats;
import app.searchBar.Filters;
import app.user.User;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The type Command runner.
 */
public final class CommandRunner {
    /**
     * The Object mapper.
     */
    private static ObjectMapper objectMapper = new ObjectMapper();
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.
            ofPattern("dd-MM-yyyy");

    private static class DateConstants {
        public static final int START_YEAR = 1900;
        public static final int CURRENT_YEAR = 2023;
        public static final int MONTHS_IN_YEAR = 12;
        public static final int MAX_DAY_IN_MONTH = 31;
        public static final int DAYS_IN_FEBRUARY = 28;
        public static final int FEBRUARY = 2;
    }

    private static class GeneralConstants {
        public static final int MIN_DAY = 1;
        public static final int MIN_MONTH = 1;
        public static final int LIMIT = 5;
    }
    private CommandRunner() {
    }

    private static ObjectNode createUserResponseNode(final CommandInput commandInput,
                                                     final User user,
                                                     final String message) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());

        if (user == null) {
            objectNode.put("message", "The username " + commandInput.getUsername()
                    + " doesn't exist.");
        } else {
            objectNode.put("message", message);
        }

        return objectNode;
    }


    /**
     * Search object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode search(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());

        if (user == null) {
            ObjectNode objectNode = objectMapper.createObjectNode();
            objectNode.put("error", "User not found");
            return objectNode;
        }

        Filters filters = new Filters(commandInput.getFilters());

        String type = commandInput.getType();

        ArrayList<String> results = user.search(filters, type);

        String message = user.isConnected() ? "Search returned " + results.size()
                + " results" : commandInput.getUsername() + " is offline.";

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);
        objectNode.set("results", objectMapper.valueToTree(results));

        return objectNode;
    }


    /**
     * Select object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode select(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());

        if (user == null) {
            ObjectNode objectNode = objectMapper.createObjectNode();
            objectNode.put("error", "User not found");
            return objectNode;
        }

        String message = user.select(commandInput.getItemNumber());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Load object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode load(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());

        if (user == null) {
            ObjectNode objectNode = objectMapper.createObjectNode();
            objectNode.put("error", "User not found");
            return objectNode;
        }

        String message = user.load();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Play pause object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode playPause(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message = user.playPause();

        if (user == null) {
            ObjectNode objectNode = objectMapper.createObjectNode();
            objectNode.put("error", "User not found");
            return objectNode;
        }

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Repeat object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode repeat(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());

        if (user == null) {
            ObjectNode objectNode = objectMapper.createObjectNode();
            objectNode.put("error", "User not found");
            return objectNode;
        }

        String message = user.repeat();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Shuffle object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode shuffle(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        Integer seed = commandInput.getSeed();
        String message = user.shuffle(seed);

        if (user == null) {
            ObjectNode objectNode = objectMapper.createObjectNode();
            objectNode.put("error", "User not found");
            return objectNode;
        }

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Forward object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode forward(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());

        if (user == null) {
            ObjectNode objectNode = objectMapper.createObjectNode();
            objectNode.put("error", "User not found");
            return objectNode;
        }

        String message = user.forward();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Backward object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode backward(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message = user.backward();

        if (user == null) {
            ObjectNode objectNode = objectMapper.createObjectNode();
            objectNode.put("error", "User not found");
            return objectNode;
        }

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Like object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode like(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());

        if (user == null) {
            ObjectNode objectNode = objectMapper.createObjectNode();
            objectNode.put("error", "User not found");
            return objectNode;
        }

        String message = user.like();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Next object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode next(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message = user.next();

        if (user == null) {
            ObjectNode objectNode = objectMapper.createObjectNode();
            objectNode.put("error", "User not found");
            return objectNode;
        }

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Prev object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode prev(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message = user.prev();

        if (user == null) {
            ObjectNode objectNode = objectMapper.createObjectNode();
            objectNode.put("error", "User not found");
            return objectNode;
        }

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Create playlist object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode createPlaylist(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());

        if (user == null) {
            ObjectNode objectNode = objectMapper.createObjectNode();
            objectNode.put("error", "User not found");
            return objectNode;
        }

        String message = user.createPlaylist(commandInput.getPlaylistName(),
                commandInput.getTimestamp());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Add remove in playlist object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode addRemoveInPlaylist(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());

        if (user == null) {
            ObjectNode objectNode = objectMapper.createObjectNode();
            objectNode.put("error", "User not found");
            return objectNode;
        }

        String message = user.addRemoveInPlaylist(commandInput.getPlaylistId());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Switch visibility object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode switchVisibility(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message = user.switchPlaylistVisibility(commandInput.getPlaylistId());

        if (user == null) {
            ObjectNode objectNode = objectMapper.createObjectNode();
            objectNode.put("error", "User not found");
            return objectNode;
        }

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Show playlists object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode showPlaylists(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        ArrayList<PlaylistOutput> playlists = user.showPlaylists();

        if (user == null) {
            ObjectNode objectNode = objectMapper.createObjectNode();
            objectNode.put("error", "User not found");
            return objectNode;
        }

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(playlists));

        return objectNode;
    }

    /**
     * Follow object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode follow(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());

        if (user == null) {
            ObjectNode objectNode = objectMapper.createObjectNode();
            objectNode.put("error", "User not found");
            return objectNode;
        }

        String message = user.follow();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Status object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode status(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());

        if (user == null) {
            ObjectNode objectNode = objectMapper.createObjectNode();
            objectNode.put("error", "User not found");
            return objectNode;
        }

        PlayerStats stats = user.getPlayerStats();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("stats", objectMapper.valueToTree(stats));

        return objectNode;
    }

    /**
     * Show liked songs object node.
     *
     * @param commandInput the command input
     * @return the object node
     */
    public static ObjectNode showLikedSongs(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        ArrayList<String> songs = user.showPreferredSongs();

        if (user == null) {
            ObjectNode objectNode = objectMapper.createObjectNode();
            objectNode.put("error", "User not found");
            return objectNode;
        }

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(songs));

        return objectNode;
    }

    /**
     * Gets preferred genre.
     *
     * @param commandInput the command input
     * @return the preferred genre
     */
    public static ObjectNode getPreferredGenre(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());

        String preferredGenre = user.getPreferredGenre();

        if (user == null) {
            ObjectNode objectNode = objectMapper.createObjectNode();
            objectNode.put("error", "User not found");
            return objectNode;
        }

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(preferredGenre));

        return objectNode;
    }

    /**
     * Gets top 5 songs.
     *
     * @param commandInput the command input
     * @return the top 5 songs
     */
    public static ObjectNode getTop5Songs(final CommandInput commandInput) {
        List<String> songs = Admin.getTop5Songs();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(songs));

        return objectNode;
    }

    /**
     * Gets top 5 playlists.
     *
     * @param commandInput the command input
     * @return the top 5 playlists
     */
    public static ObjectNode getTop5Playlists(final CommandInput commandInput) {
        List<String> playlists = Admin.getTop5Playlists();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(playlists));

        return objectNode;
    }

    /**
     * Switches the connection status of a user.
     *
     * @param commandInput Contains the username of the user whose connection
     *                     status is to be switched.
     * @return An ObjectNode containing the result of the operation,
     * including any error messages.
     */
    public static ObjectNode switchConnectionStatus(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());

        if (user == null) {
            objectNode.put("message", "The username " + commandInput.getUsername()
                    + " doesn't exist.");
        } else if ("user".equalsIgnoreCase(user.getType())) {
            objectNode.put("message", commandInput.getUsername() + " is not a normal user.");
        } else {
            String message = user.switchConnectionStatus();
            objectNode.put("message", message);
        }
        return objectNode;
    }

    /**
     * Retrieves a list of online users.
     *
     * @param commandInput Command input with details for the operation.
     * @return An ObjectNode containing a list of usernames of online users.
     */
    public static ObjectNode getOnlineUsers(final CommandInput commandInput) {
        List<User> onlineUsers = Admin.getOnlineUsers();

        // Extracts and collects the usernames of all online users into a list.
        List<String> onlineUsernames = onlineUsers.stream().map(User::getUsername).
                collect(Collectors.toList());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());

        JsonNode usernamesNode = objectMapper.valueToTree(onlineUsernames);

        objectNode.set("result", usernamesNode);

        return objectNode;
    }

    /**
     * Adds a new user to the system.
     *
     * @param commandInput Contains details for the new user,
     *                     including username, age, and city.
     * @return An ObjectNode containing a message about the
     * success or failure of the operation.
     */
    public static ObjectNode addUser(final CommandInput commandInput) {
        String username = commandInput.getUsername();
        int age = commandInput.getAge();
        String city = commandInput.getCity();
        String type = commandInput.getType();

        if (Admin.getUser(username) != null) {
            ObjectNode objectNode = objectMapper.createObjectNode();
            objectNode.put("command", "addUser");
            objectNode.put("user", username);
            objectNode.put("timestamp", commandInput.getTimestamp());
            objectNode.put("message", "The username " + username
                    + " is already taken.");
            return objectNode;
        }

        User newUser = new User(username, age, city, type);
        Admin.addUser(newUser);

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", "addUser");
        objectNode.put("user", username);
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", "The username " + username
                + " has been added successfully.");
        return objectNode;
    }

    /**
     * Converts a list of strings into a JSON array node.
     * This method iterates over each string in the provided list and
     * adds them to a new JSON array node.
     *
     * @param list The list of strings to be converted into a JSON array.
     * @return ArrayNode containing the elements of the provided list.
     */
    private static ArrayNode convertListToJsonNode(final List<String> list) {
        ArrayNode arrayNode = objectMapper.createArrayNode();
        for (String item : list) {
            arrayNode.add(item);
        }
        return arrayNode;
    }

    /**
     * Retrieves all users from the system, categorized as normal users, artists, and hosts.
     * This method uses Java Streams to efficiently process the user list and categorize them
     * based on their roles. It employs groupingBy for categorization and mapping to extract
     * usernames. The categorized lists are then converted to JSON nodes for response.
     *
     * @param commandInput The input containing command details.
     * @return An ObjectNode containing lists of all users categorized
     * by their roles along with the command and timestamp.
     */
    public static ObjectNode getAllUsers(final CommandInput commandInput) {
        List<User> allUsers = Admin.getUsers();

        // Categorize users into artists, hosts, and normal users using Stream API
        // groupingBy categorizes, and mapping transforms User objects to their usernames
        Map<String, List<String>> categorizedUsers = allUsers.stream()
                .collect(Collectors.groupingBy(
                        user -> user.isArtist() ? "artists" : user.isHost()
                                ? "hosts" : "normalUsers",
                        Collectors.mapping(User::getUsername, Collectors.toList())
                ));
        // Retrieve categorized lists, defaulting to empty list if no users in category
        List<String> artists = categorizedUsers.getOrDefault("artists", new ArrayList<>());
        List<String> hosts = categorizedUsers.getOrDefault("hosts", new ArrayList<>());
        List<String> normalUsers = categorizedUsers.getOrDefault("normalUsers", new ArrayList<>());


        ArrayNode usersArray = objectMapper.createArrayNode();
        usersArray.addAll(convertListToJsonNode(normalUsers));
        usersArray.addAll(convertListToJsonNode(artists));
        usersArray.addAll(convertListToJsonNode(hosts));

        ObjectNode responseNode = objectMapper.createObjectNode();
        responseNode.put("command", commandInput.getCommand());
        responseNode.put("timestamp", commandInput.getTimestamp());
        responseNode.set("result", usersArray);

        return responseNode;
    }
    /**
     * Adds a new album for a user.
     *
     * @param commandInput Contains album details including the name,
     *                     release year, and song inputs.
     * @return An ObjectNode with a message indicating the success
     * or failure of the operation.
     */
    public static ObjectNode addAlbum(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());

        if (user == null) {
            ObjectNode errorNode = objectMapper.createObjectNode();
            errorNode.put("command", commandInput.getCommand());
            errorNode.put("user", commandInput.getUsername());
            errorNode.put("timestamp", commandInput.getTimestamp());
            errorNode.put("message", "The username " + commandInput.getUsername()
                    + " doesn't exist.");
            return errorNode;
        }

        String message = user.addAlbum(commandInput.getName(), commandInput.
                getReleaseYear(), commandInput.getSongs());

        ObjectNode responseNode = objectMapper.createObjectNode();
        responseNode.put("command", "addAlbum");
        responseNode.put("user", user.getUsername());
        responseNode.put("timestamp", commandInput.getTimestamp());
        responseNode.put("message", message);

        return responseNode;
    }

    /**
     * Displays the albums of a specific user.
     *
     * @param commandInput Contains the username of the user whose albums are to be displayed.
     * @return An ObjectNode containing the albums or an error message if the user is not found.
     */
    public static ObjectNode showAlbums(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());

        if (user == null) {
            ObjectNode errorNode = objectMapper.createObjectNode();
            errorNode.put("command", commandInput.getCommand());
            errorNode.put("user", commandInput.getUsername());
            errorNode.put("timestamp", commandInput.getTimestamp());
            errorNode.put("message", "The username " + commandInput.getUsername()
                    + " doesn't exist.");
            return errorNode;
        }

        ArrayList<AlbumOutput> albums = user.showAlbums();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.set("result", objectMapper.valueToTree(albums));

        return objectNode;
    }

    /**
     * Retrieves the top 5 albums sorted by release year in descending order.
     *
     * @param commandInput The input containing command details.
     * @return An ObjectNode containing the list of top 5 albums
     * along with the command and timestamp.
     */
    public static ObjectNode getTop5Albums(final CommandInput commandInput) {
        List<String> albums = Admin.getTop5Albums();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(albums));

        return objectNode;
    }

    /**
     * Removes an event from a user's list of events.
     *
     * @param commandInput The input containing the username and the
     *                     name of the event to be removed.
     * @return An ObjectNode indicating the success or failure
     * of the event removal.
     */
    public static ObjectNode removeEvent(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        ObjectNode responseNode = objectMapper.createObjectNode();
        responseNode.put("command", commandInput.getCommand());
        responseNode.put("user", commandInput.getUsername());
        responseNode.put("timestamp", commandInput.getTimestamp());

        if (user == null) {
            responseNode.put("message", "The username " + commandInput.getUsername()
                    + " doesn't exist.");
            return responseNode;
        }

        if (!user.isArtist()) {
            responseNode.put("message", commandInput.getUsername()
                    + " is not an artist.");
            return responseNode;
        }

        List<Event> events = user.getEvents();
        String eventName = commandInput.getName();
        boolean removed = events.removeIf(event -> event.getName().equalsIgnoreCase(eventName));

        String message = removed ? commandInput.getUsername()
                + " deleted the event successfully"
                : commandInput.getUsername()
                + " doesn't have an event with the given name.";
        responseNode.put("message", message);

        return responseNode;
    }

    /**
     * Removes an announcement from a user's list of announcements.
     *
     * @param commandInput The input containing the username and the
     *                     name of the announcement to be removed.
     * @return An ObjectNode indicating the success or failure
     * of the announcement removal.
     */
    public static ObjectNode removeAnnouncement(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        ObjectNode responseNode = objectMapper.createObjectNode();
        responseNode.put("command", commandInput.getCommand());
        responseNode.put("user", commandInput.getUsername());
        responseNode.put("timestamp", commandInput.getTimestamp());

        if (user == null) {
            responseNode.put("message", "The username "
                    + commandInput.getUsername() + " doesn't exist.");
            return responseNode;
        }

        if (!user.isHost()) {
            responseNode.put("message", commandInput.getUsername()
                    + " is not a host.");
            return responseNode;
        }

        List<Announcement> announcements = user.getAnnouncements();
        String announcementName = commandInput.getName();
        boolean removed = announcements.removeIf(announcement ->
                announcement.getName().equalsIgnoreCase(announcementName));

        String message = removed ? commandInput.getUsername()
                + " has successfully deleted the announcement."
                : commandInput.getUsername() + " has no announcement with the given name.";
        responseNode.put("message", message);

        return responseNode;
    }


    /**
     * Removes a podcast from the system.
     *
     * @param commandInput Contains the name of the podcast to be removed.
     * @return An ObjectNode indicating the success or failure of the operation.
     */
    public static ObjectNode removePodcast(final CommandInput commandInput) {
        String podcastName = commandInput.getName();
        String username = commandInput.getUsername();

        boolean removed = Admin.getPodcasts().removeIf(podcast ->
                podcast.getName().equalsIgnoreCase(podcastName));

        ObjectNode responseNode = objectMapper.createObjectNode();
        responseNode.put("command", commandInput.getCommand());
        responseNode.put("user", commandInput.getUsername());
        responseNode.put("timestamp", commandInput.getTimestamp());
        responseNode.put("message", !removed ? "Podcast removed successfully."
                : username + " can't delete this podcast.");

        return responseNode;
    }

    /**
     *  * Retrieves a list of the top 5 artists from the system based on specific criteria.
     *  * This method identifies artists with the highest number of albums released.
     *  * It iterates through all albums in the system, counts the number of albums for each artist,
     *  * and then sorts these artists based on the count in descending order. The top 5 artists
     *  * are then selected based on this sorted order.
     *
     * @param commandInput The input containing command details.
     * @return An ObjectNode containing the list of top 5 artists.
     */
    public static ObjectNode getTop5Artists(final CommandInput commandInput) {
        Map<String, Integer> artistAlbumCount = new HashMap<>();

        for (Album album : Admin.getAlbums()) {
            artistAlbumCount.put(album.getArtist(),
                    artistAlbumCount.getOrDefault(album.getArtist(), 0) + 1);
        }

        List<String> topArtists = artistAlbumCount.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(GeneralConstants.LIMIT)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        ObjectNode responseNode = objectMapper.createObjectNode();
        responseNode.put("command", commandInput.getCommand());
        responseNode.put("timestamp", commandInput.getTimestamp());
        responseNode.set("result", objectMapper.valueToTree(topArtists));

        return responseNode;
    }

    /**
     * Adds a new podcast to the system.
     *
     * @param commandInput The input containing the podcast details
     *                     and the username of the user adding the podcast.
     * @return An ObjectNode containing the response of the operation,
     * with error details if the user is not found or if the user is not a host.
     */
    public static ObjectNode addPodcast(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());

        if (user == null) {
            ObjectNode errorNode = objectMapper.createObjectNode();
            errorNode.put("command", commandInput.getCommand());
            errorNode.put("user", commandInput.getUsername());
            errorNode.put("timestamp", commandInput.getTimestamp());
            errorNode.put("message", "The username " + commandInput.getUsername()
                    + " doesn't exist.");
            return errorNode;
        }

        String message = user.addPodcast(user.getType(), commandInput.getName(),
                commandInput.getEpisodes());

        ObjectNode responseNode = objectMapper.createObjectNode();
        responseNode.put("command", "addPodcast");
        responseNode.put("user", commandInput.getUsername());
        responseNode.put("timestamp", commandInput.getTimestamp());
        responseNode.put("message", message);

        return responseNode;
    }

/**
 * Shows podcasts available in the system.
 *
 * @param commandInput the command input
 * @return the object node containing the podcasts and their episodes
 */
public static ObjectNode showPodcasts(final CommandInput commandInput) {
    User user = Admin.getUser(commandInput.getUsername());

    if (user == null) {
        ObjectNode errorNode = objectMapper.createObjectNode();
        errorNode.put("command", commandInput.getCommand());
        errorNode.put("user", commandInput.getUsername());
        errorNode.put("timestamp", commandInput.getTimestamp());
        errorNode.put("message", "The username " + commandInput.getUsername()
                + " doesn't exist.");
        return errorNode;
    }

    ArrayList<PodcastOutput> podcastsOutputs = user.showPodcasts();

    ObjectNode responseNode = objectMapper.createObjectNode();
    responseNode.put("command", commandInput.getCommand());
    responseNode.put("user", commandInput.getUsername());
    responseNode.put("timestamp", commandInput.getTimestamp());
    responseNode.set("result", objectMapper.valueToTree(podcastsOutputs));

    return responseNode;
}

/**
 * Adds an announcement for a user.
 *
 * @param commandInput The input containing announcement details.
 * @return An ObjectNode with the operation result.
 */
public static ObjectNode addAnnouncement(final CommandInput commandInput) {
    User user = Admin.getUser(commandInput.getUsername());

    if (user == null) {
        ObjectNode errorNode = objectMapper.createObjectNode();
        errorNode.put("command", commandInput.getCommand());
        errorNode.put("user", commandInput.getUsername());
        errorNode.put("timestamp", commandInput.getTimestamp());
        errorNode.put("message", "The username " + commandInput.getUsername()
                + " doesn't exist.");
        return errorNode;
    }

    String message = user.addAnnouncement(commandInput.getName(),
            commandInput.getDescription());

    ObjectNode responseNode = objectMapper.createObjectNode();
    responseNode.put("command", commandInput.getCommand());
    responseNode.put("user", commandInput.getUsername());
    responseNode.put("timestamp", commandInput.getTimestamp());
    responseNode.put("message", message);

    return responseNode;
}

/**
 * Adds a new event for a specified user if they are an artist.
 * This method allows artists to create events and add them to their profile.
 * The method first verifies if the user exists and is an artist. It then parses
 * and validates the date of the event. If the date is not valid or if an event
 * with the same name already exists for the artist, an error message is returned.
 * Otherwise, the event is added successfully to the artist's profile.
 * @param commandInput The input containing event details.
 * @return An ObjectNode with the operation result.
 */
public static ObjectNode addEvent(final CommandInput commandInput) {
    User user = Admin.getUser(commandInput.getUsername());

    if (user == null) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", "addEvent");
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", "The username " + commandInput.getUsername()
                + " doesn't exist.");
        return objectNode;
    }

    if (!user.isArtist()) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", "addEvent");
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", commandInput.getUsername()
                + " is not an artist.");
        return objectNode;
    }

    LocalDate eventDate;
    try {
        eventDate = LocalDate.parse(commandInput.getDate(), FORMATTER);
        if (!isValidDate(eventDate)) {
            ObjectNode objectNode = objectMapper.createObjectNode();
            objectNode.put("command", "addEvent");
            objectNode.put("user", commandInput.getUsername());
            objectNode.put("timestamp", commandInput.getTimestamp());
            objectNode.put("message", "Event for " + commandInput.getUsername()
                    + " does not have a valid date.");
            return objectNode;
        }
    } catch (DateTimeParseException e) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", "addEvent");
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", "Event for " + commandInput.getUsername()
                + " does not have a valid date.");
        return objectNode;
    }

    if (user.hasEventWithName(commandInput.getName())) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", "addUser");
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", commandInput.getUsername()
                + " has another event with the same name.");
        return objectNode;
    }

    user.addEvent(commandInput.getName(), eventDate, commandInput.getDescription());

    ObjectNode responseNode = objectMapper.createObjectNode();
    responseNode.put("command", commandInput.getCommand());
    responseNode.put("user", commandInput.getUsername());
    responseNode.put("timestamp", commandInput.getTimestamp());
    responseNode.put("message", commandInput.getUsername()
            + " has added new event successfully.");

    return responseNode;
}

/**
 * Validates if the given date is within the acceptable range
 * and conforms to calendar rules.
 *
 * This method checks whether the provided date is valid based on several criteria:
 * 1. The year must fall within the defined start year and the current year.
 * 2. The month must be within the range of valid months in a year (1 to 12).
 * 3. The day must be within the usual range of days in a month (1 to 31).
 * 4. For February, the day must not exceed the number of days typically in February.
 *
 * This method is especially useful for validating dates in user input, ensuring that they
 * are not only correctly formatted but also logically valid.
 *
 * @param date The date to be validated, provided as a LocalDate object.
 * @return true if the date is valid based on the criteria outlined above; false otherwise.
 */
private static boolean isValidDate(final LocalDate date) {
    final int year = date.getYear();
    final int month = date.getMonthValue();
    final int day = date.getDayOfMonth();

    if (year < DateConstants.START_YEAR
            || year > DateConstants.CURRENT_YEAR) {
        return false;
    }

    if (month < GeneralConstants.MIN_MONTH
            || month > DateConstants.MONTHS_IN_YEAR) {
        return false;
    }

    if (day < GeneralConstants.MIN_DAY
            || day > DateConstants.MAX_DAY_IN_MONTH) {
        return false;
    }

    if (month == DateConstants.FEBRUARY
            && day > DateConstants.DAYS_IN_FEBRUARY) {
        return false;
    }

    return true;
}

/**
 * Adds merchandise for a user.
 *
 * @param commandInput The input containing merchandise details.
 * @return An ObjectNode with the operation result.
 */
public static ObjectNode addMerch(final CommandInput commandInput) {
    User user = Admin.getUser(commandInput.getUsername());

    if (user == null) {
        ObjectNode responseNode = objectMapper.createObjectNode();
        responseNode.put("command", commandInput.getCommand());
        responseNode.put("user", commandInput.getUsername());
        responseNode.put("timestamp", commandInput.getTimestamp());
        responseNode.put("message", "The username " + commandInput.getUsername()
                + " doesn't exist.");
        return responseNode;
    }

    String message = user.addMerchandise(commandInput.getName(),
            commandInput.getDescription(), commandInput.getPrice());

    ObjectNode responseNode = objectMapper.createObjectNode();
    responseNode.put("command", commandInput.getCommand());
    responseNode.put("user", commandInput.getUsername());
    responseNode.put("timestamp", commandInput.getTimestamp());
    responseNode.put("message", message);

    return responseNode;
}
    /**
     * Deletes a user from the system.
     *
     * @param commandInput The input containing the username of the user
     *                     to be deleted.
     * @return An ObjectNode containing the outcome of the delete
     * operation, with error details if the user is not found.
     */
    public static ObjectNode deleteUser(final CommandInput commandInput) {
        User userToDelete = Admin.getUser(commandInput.getUsername());

        if (userToDelete == null) {
            ObjectNode responseNode = objectMapper.createObjectNode();
            responseNode.put("command", "deleteUser");
            responseNode.put("user", commandInput.getUsername());
            responseNode.put("timestamp", commandInput.getTimestamp());
            responseNode.put("message", commandInput.getUsername()
                    + " can't be deleted.");
            return responseNode;
        }

        Admin.getUsers().remove(userToDelete);

        ObjectNode responseNode = objectMapper.createObjectNode();
        responseNode.put("command", "deleteUser");
        responseNode.put("user", commandInput.getUsername());
        responseNode.put("timestamp", commandInput.getTimestamp());
        responseNode.put("message", commandInput.getUsername()
                + " was successfully deleted.");

        return responseNode;
    }

    /**
     * Removes an album from a user's collection.
     *
     * @param commandInput The input containing the username and the
     *                     name of the album to be removed.
     * @return An ObjectNode indicating the success or failure
     * of the album removal.
     */
    public static ObjectNode removeAlbum(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());

        if (user == null) {
            ObjectNode responseNode = objectMapper.createObjectNode();
            responseNode.put("command", commandInput.getCommand());
            responseNode.put("user", commandInput.getUsername());
            responseNode.put("timestamp", commandInput.getTimestamp());
            responseNode.put("message", "The username "
                    + commandInput.getUsername() + " doesn't exist.");
            return responseNode;
        }

        String albumRemovalMessage = Admin.removeAlbum(commandInput.getName(),
                commandInput.getUsername());

        ObjectNode responseNode = objectMapper.createObjectNode();
        responseNode.put("command", commandInput.getCommand());
        responseNode.put("user", commandInput.getUsername());
        responseNode.put("timestamp", commandInput.getTimestamp());
        responseNode.put("message", albumRemovalMessage);

        return responseNode;
    }
}