package hac.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GameRepository extends JpaRepository<Game, Long> {
    List<Game> findByTitleContainingIgnoreCase(String title);

    List<Game> findByTitleContainingIgnoreCaseAndGenreIgnoreCaseAndMultiplayerAndSingleplayer(
            String title, String genre, boolean multiplayer, boolean singleplayer);


    @Query("SELECT g FROM Game g JOIN g.reviews r GROUP BY g ORDER BY AVG(r.rating) DESC")
    List<Game> findTop10GamesOrderByAverageReviewScore();

}
