package hac.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Here we wrote this queries cause trying to use the jpa functions didn't work for use
 * All of those queries are used by our advanced search options. there are a few cases of what happens if all
 * form's fields are submitted. what happens if some fields are missing, etc.
 *The logic of our search will be in the readme
 */
public interface GameRepository extends JpaRepository<Game, Long> {

    //
    @Query("SELECT g FROM Game g WHERE LOWER(g.title) LIKE %:title% AND LOWER(g.genre) = LOWER(:genre) " +
            "AND (g.singleplayer = :singleplayer OR g.multiplayer = :multiplayer)")
    List<Game> findByTitleGenreAndSingleplayerOrMultiplayer(
            @Param("title") String title,
            @Param("genre") String genre,
            @Param("singleplayer") boolean singleplayer,
            @Param("multiplayer") boolean multiplayer
    );

    @Query("SELECT g FROM Game g WHERE LOWER(g.title) LIKE %:title% AND (g.singleplayer = :singleplayer OR g.multiplayer = :multiplayer)")
    List<Game> findByTitleAndSingleplayerOrMultiplayer(
            @Param("title") String title,
            @Param("singleplayer") boolean singleplayer,
            @Param("multiplayer") boolean multiplayer
    );

    @Query("SELECT g FROM Game g WHERE LOWER(g.genre) = LOWER(:genre) " +
            "AND (g.singleplayer = :singleplayer OR g.multiplayer = :multiplayer)")
    List<Game> findByGenreAndSingleplayerOrMultiplayer(@Param("genre") String genre,
                                                       @Param("singleplayer") boolean singleplayer,
                                                       @Param("multiplayer") boolean multiplayer);

    List<Game> findBySingleplayerOrMultiplayer(
            boolean singleplayer, boolean multiplayer
    );

    @Query("SELECT g FROM Game g JOIN g.reviews r GROUP BY g ORDER BY AVG(r.rating) DESC")
    List<Game> findTop10GamesOrderByAverageReviewScore();
}
