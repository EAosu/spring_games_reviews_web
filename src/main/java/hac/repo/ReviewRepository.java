package hac.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByOrderByTimeDesc();
    List<Review> findAllByUsername(String username);
    Optional<Review> findById(Long id);

}
