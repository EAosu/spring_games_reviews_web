package hac.repo;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "games")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "game")
    private List<Review> reviews;

    private String title;
    private String genre;
    private boolean multiplayer;
    private boolean singleplayer;

    // Constructors, getters, and setters

    // Constructors
    public Game() {
    }

    public Game(String title, String genre, boolean multiplayer, boolean singleplayer) {
        this.title = title;
        this.genre = genre;
        this.multiplayer = multiplayer;
        this.singleplayer = singleplayer;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public boolean isMultiplayer() {
        return multiplayer;
    }

    public void setMultiplayer(boolean multiplayer) {
        this.multiplayer = multiplayer;
    }

    public boolean isSingleplayer() {
        return singleplayer;
    }

    public void setSingleplayer(boolean singleplayer) {
        this.singleplayer = singleplayer;
    }

    // Other getters and setters, if any

}
