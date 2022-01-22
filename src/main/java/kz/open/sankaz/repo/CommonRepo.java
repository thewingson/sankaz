package kz.open.sankaz.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

@NoRepositoryBean
public interface CommonRepo<E> extends JpaRepository<E, Long> {

    List<E> getAllByIdIn(@Param("ids") List<Long> ids);
    List<E> getAllByCreatedBy(@Param("createdBy") String createdBy);
    List<E> getAllByDeletedBy(@Param("deletedBy") String deletedBy);
    List<E> getAllByUpdatedBy(@Param("updatedBy") String updatedBy);
    List<E> getAllByCreateTsBetween(@Param("fromDate") LocalDateTime fromDate, @Param("fromDate") LocalDateTime toDate);
    List<E> getAllByUpdateTsBetween(@Param("fromDate") LocalDateTime fromDate, @Param("fromDate") LocalDateTime toDate);
    List<E> getAllByDeleteTsBetween(@Param("fromDate") LocalDateTime fromDate, @Param("fromDate") LocalDateTime toDate);

}
