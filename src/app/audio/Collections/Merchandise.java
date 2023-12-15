package app.audio.Collections;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents merchandise with a name, description, and price.
 */
@Setter
@Getter
public class Merchandise {
    private final String name;
    private final String description;
    private final double price;

    /**
     * Constructs a Merchandise object with the specified name, description, and price.
     *
     * @param name        The name of the merchandise.
     * @param description The description of the merchandise.
     * @param price       The price of the merchandise.
     */
    public Merchandise(final String name, final String description, final double price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }
}
