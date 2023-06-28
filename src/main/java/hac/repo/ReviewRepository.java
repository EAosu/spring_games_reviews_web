package hac.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    //All reviews ordered by time desc
    List<Review> findAllByOrderByTimeDesc();
    //All of the user's reviews
    List<Review> findAllByUsername(String username);
    //Finding a review by it's id
    Optional<Review> findById(Long id);

}
