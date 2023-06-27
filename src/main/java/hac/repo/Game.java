package hac.repo;

import hac.repo.Review;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

@Entity
@Table(name = "games")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews;


    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Genre is required")
    private String genre;

    private Boolean multiplayer;

    private Boolean singleplayer;

    private Double averageScore;


    // Constructors, getters, and setters

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
        calculateAverageScore();
    }

    public void calculateAverageScore() {
        System.out.println("Calculating average");
        double sum = 0.0;
        if (!reviews.isEmpty()) {
            for (Review review : reviews) {
                sum += review.getRating();
            }
            System.out.println("Finished calculating successfully");
            setAverageScore(sum / reviews.size());
        } else {
            System.out.println("Finished calculating unsuccessfully");
            averageScore = null;
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
    public Double getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(Double averageScore) {
        this.averageScore = averageScore;
    }
    public void setSingleplayer(Boolean singleplayer) {
        this.singleplayer = singleplayer;
    }

    public boolean isValidSelection() {
        return (!(multiplayer == null && singleplayer == null));
    }
}
