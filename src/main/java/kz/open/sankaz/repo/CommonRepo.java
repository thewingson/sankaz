package kz.open.sankaz.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface CommonRepo<E> extends JpaRepository<E, Long> {
    Optional<E> getByIdAndDeletedByIsNull(@Param("id") Long id);
    List<E> getAllByDeletedByIsNull();
    List<E> getAllByIdIn(@Param("ids") List<Long> ids);
    List<E> getAllByCreatedBy(@Param("createdBy") String createdBy);
    List<E> getAllByDeletedBy(@Param("deletedBy") String deletedBy);
    List<E> getAllByUpdatedBy(@Param("updatedBy") String updatedBy);
    List<E> getAllByCreateTsBetween(@Param("fromDate") LocalDateTime fromDate, @Param("fromDate") LocalDateTime toDate);
    List<E> getAllByUpdateTsBetween(@Param("fromDate") LocalDateTime fromDate, @Param("fromDate") LocalDateTime toDate);
    List<E> getAllByDeleteTsBetween(@Param("fromDate") LocalDateTime fromDate, @Param("fromDate") LocalDateTime toDate);

}
