package hac.repo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LastLoginRepository extends JpaRepository<LastLogin, Long> {

    LastLogin findByEmail(String email);
}
