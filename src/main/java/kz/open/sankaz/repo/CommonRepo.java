package kz.open.sankaz.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface CommonRepo<E> extends JpaRepository<E, Long> {
}
