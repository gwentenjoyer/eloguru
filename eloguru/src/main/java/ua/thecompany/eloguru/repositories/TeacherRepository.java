package ua.thecompany.eloguru.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.thecompany.eloguru.model.Teacher;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long>,
        PagingAndSortingRepository<Teacher, Long> {
    @Override
    @Query("update #{#entityName} e set e.account.active=false where e.id = ?1 and e.account.active = true")
    @Transactional
    @Modifying
    void deleteById(Long id);

    @Query("select t from #{#entityName} t where t.id = ?1")
//    @Query("select t from #{#entityName} t where t.account.id = ?1")
    Optional<Teacher> findByIdAndActive(Long id, boolean active);

//    @Query("select t from #{#entityName} t where t.account.id = ?1")
    @Transactional
    @Query("select t from #{#entityName} t where t.account.active = :active and t.account.id = :id")
    Optional<Teacher> findByAccountIdAndActive(Long id, boolean active);

    List<Teacher> findByAccountActive(boolean active);

    @Transactional
    @Modifying
    @Query("delete from #{#entityName} t where t.account.active = false")
    void deleteByActiveFalse();
}
