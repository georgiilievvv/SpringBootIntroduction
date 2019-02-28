package realestate.repository;

import org.springframework.stereotype.Repository;
import realestate.domain.entities.Offer;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface OfferRepository extends JpaRepository<Offer, String> {
}
