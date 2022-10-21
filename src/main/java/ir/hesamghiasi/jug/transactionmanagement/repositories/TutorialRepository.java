package ir.hesamghiasi.jug.transactionmanagement.repositories;


import ir.hesamghiasi.jug.transactionmanagement.entities.Tutorial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockModeType;
import java.util.Optional;


@Transactional(propagation = Propagation.SUPPORTS)
public interface TutorialRepository extends JpaRepository<Tutorial, Long> {

    @Override
    Optional<Tutorial> findById(Long aLong);

    @Override
    @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    <S extends Tutorial> S saveAndFlush(S entity);
}
