package hac.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameRepository extends JpaRepository<Game, Long> {
    List<Game> findByTitleContainingIgnoreCase(String title);

    List<Game> findByTitleContainingIgnoreCaseAndGenreIgnoreCaseAndMultiplayerAndSingleplayer(
            String title, String genre, boolean multiplayer, boolean singleplayer);
}
