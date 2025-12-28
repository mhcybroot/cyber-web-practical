package root.cyb.mh.cyberweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import root.cyb.mh.cyberweb.model.VulnerableUser;

import java.util.List;

@Repository
public interface VulnerableUserRepository extends JpaRepository<VulnerableUser, Long> {
    List<VulnerableUser> findTop5ByOrderByIdDesc();
}
