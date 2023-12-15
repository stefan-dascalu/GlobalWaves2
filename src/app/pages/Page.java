package app.pages;

import lombok.Getter;

/**
 * Abstract base class for different types of pages in the application. Each page
 * is associated with a specific user and contains content relevant to that page type.
 *
 * <p>Subclasses should implement the {@code getContent()} method to define their
 * specific content. This structure allows different pages (like Home, Profile, etc.)
 * to have their unique content while sharing common properties like the associated username.
 *
 * @see Home
 * @see Artist
 * @see LikedContent
 * @see Host
 */
@Getter
public abstract class Page {
    private final String username;

    /**
     * Constructs a new Page for a given user.
     *
     * @param username Username associated with this page, used for personalizing content.
     */
    public Page(final String username) {
        this.username = username;
    }

    /**
     * Returns the content of the page. Subclasses must provide their specific implementation.
     *
     * @return String representing the page's content.
     */
    public abstract String getContent();
}
