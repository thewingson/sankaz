package kz.open.sankaz.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import java.util.List;

@NoRepositoryBean
public interface CommonRepo<E> extends JpaRepository<E, Long> {
    List<E> getAllByIdIn(@Param("ids") List<Long> ids);

}
