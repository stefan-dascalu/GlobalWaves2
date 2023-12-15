package fileio.input;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public final class UserInput {
    private String username;
    private int age;
    private String city;
    private String type;

    public UserInput() {
    }

    @Override
    public String toString() {
        return "UserInput{"
                + "username='" + username + '\''
                + ", age=" + age
                + ", city='" + city + '\''
                + ", type ='" + type + '\''
                + '}';
    }
}
