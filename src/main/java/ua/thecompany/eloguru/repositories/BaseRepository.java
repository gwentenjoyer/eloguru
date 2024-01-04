package ua.thecompany.eloguru.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import ua.thecompany.eloguru.model.BaseEntity;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity, ID extends Long> extends JpaRepository<T, ID>,
        PagingAndSortingRepository<T, ID> {
    @Override
    @Query("update #{#entityName} e set e.active=false where e.id = ?1 and e.active = true")
    @Transactional
    @Modifying
    void deleteById(Long id);

    Optional<T> findByIdAndActive(Long id, boolean active);
    List<T> findByActive(boolean active);

    @Transactional
    @Modifying
    void deleteByActiveFalse();

}

