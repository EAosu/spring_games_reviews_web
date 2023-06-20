package hac.repo;

import hac.repo.Review;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

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

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Genre is required")
    private String genre;

    @NotNull(message = "Multiplayer flag is required")
    private Boolean multiplayer;

    @NotNull(message = "Singleplayer flag is required")
    private Boolean singleplayer;

    // Constructors, getters, and setters

    // Constructors
    public Game() {
    }

    public Game(String title, String genre, Boolean multiplayer, Boolean singleplayer) {
        this.title = title;
        this.genre = genre;
        this.multiplayer = multiplayer;
        this.singleplayer = singleplayer;
    }

    public void addReview(Review review) {
        review.setGame(this);
        reviews.add(review);
        for(int i = 0; i < reviews.size(); i++) {
            System.out.println(reviews.get(i));
        }
    }

    // Getters and setters
    // ...

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
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

    public Boolean getMultiplayer() {
        return multiplayer;
    }

    public void setMultiplayer(Boolean multiplayer) {
        this.multiplayer = multiplayer;
    }

    public Boolean getSingleplayer() {
        return singleplayer;
    }

    public void setSingleplayer(Boolean singleplayer) {
        this.singleplayer = singleplayer;
    }
}
