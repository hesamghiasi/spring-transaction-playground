package ir.hesamghiasi.jug.transactionmanagement.services;

import ir.hesamghiasi.jug.transactionmanagement.entities.Tutorial;
import ir.hesamghiasi.jug.transactionmanagement.repositories.TutorialRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.persistence.EntityManager;
import java.util.Optional;

/**
 * @author Hesam Ghiasi created on 6/23/22
 */
@Service
@Slf4j
public class TutorialService {

    private final TutorialRepository tutorialRepository;
    private final EntityManager entityManager;

    public TutorialService(TutorialRepository tutorialRepository, EntityManager entityManager) {
        this.tutorialRepository = tutorialRepository;
        this.entityManager = entityManager;
    }


    public Tutorial findTutorialByIdNotAnnotated(Long id, Long sleepTime) {
        Tutorial tutorial = tutorialRepository.findById(id).orElseGet(() -> null);
        sleep(sleepTime);
        return tutorial;
    }

    @Transactional(propagation = Propagation.NEVER)
    public Tutorial findTutorialByIdPropagationNever(Long id, Long sleepTime) {
        Tutorial tutorial = tutorialRepository.findById(id).orElseGet(() -> null);
        sleep(sleepTime);
        return tutorial;
    }

    @Transactional
    public Tutorial findTutorialByIdDefaultAnnotation(Long id, Long sleepTime) {
        Tutorial tutorial = tutorialRepository.findById(id).orElseGet(() -> null);
        sleep(sleepTime);
        return tutorial;
    }

    @Transactional
    public Tutorial createTutorial(Long sleepTime) {
        Tutorial tutorial = new Tutorial();
        tutorial.setTitle("test");
        tutorial.setDescription("desc");
        tutorial = tutorialRepository.save(tutorial);
        sleep(sleepTime);
        return tutorial;
    }

    @Transactional
    public void updateTutorialWithRollBack(Long id, Long sleepTime) {
        Optional<Tutorial> byId = tutorialRepository.findById(id);
        Tutorial tutorial = byId.get();
        tutorial.setTitle(tutorial.getTitle() + "-updated");
        tutorialRepository.saveAndFlush(tutorial);
        log.info("""
            ====================================================\n
            updateTutorialWithRollBack is going to sleep\n
            ====================================================
        """);
        sleep(sleepTime);
        log.info("""
            ====================================================\n
            updateTutorialWithRollBack is waking up\n
            ====================================================
        """);
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
    }

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public Tutorial findTutorialByIdReadUncommited(Long id, Long sleepTime) {
        entityManager.clear();
        Tutorial tutorial = tutorialRepository.findById(id).orElseGet(() -> null);
        sleep(sleepTime);
        return tutorial;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Tutorial findTutorialByIdReadCommited(Long id, Long sleepTime) {
        entityManager.clear();
        Tutorial tutorial = tutorialRepository.findById(id).orElseGet(() -> null);
        sleep(sleepTime);
        return tutorial;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Tutorial doubleReadReadCommited(Long id, Long sleepTime) {
        entityManager.clear();
        Tutorial tutorial_1 = tutorialRepository.findById(id).orElseGet(() -> null);
        System.out.println("first read: " + tutorial_1.getTitle());
        sleep(sleepTime);
        entityManager.clear();
        Tutorial tutorial_2 = tutorialRepository.findById(id).orElseGet(() -> null);
        System.out.println("second read: " + tutorial_2.getTitle());
        return tutorial_1;
    }

    @Transactional
    public void updateTutorial(Long id, Long sleepTime) {
        Optional<Tutorial> byId = tutorialRepository.findById(id);
        Tutorial tutorial = byId.get();
        tutorial.setTitle(tutorial.getTitle() + "-updated");
        tutorialRepository.saveAndFlush(tutorial);
        log.info("""
            ====================================================\n
            updateTutorial is going to sleep\n
            ====================================================
        """);
        sleep(sleepTime);
        log.info("""
            ====================================================\n
            updateTutorial is waking up\n
            ====================================================
        """);
    }


    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Tutorial doubleReadRepeataleRead(Long id, Long sleepTime) {
        entityManager.clear();
        Tutorial tutorial_1 = tutorialRepository.findById(id).orElseGet(() -> null);
        System.out.println("first read: " + tutorial_1.getTitle());
        sleep(sleepTime);
        entityManager.clear();
        Tutorial tutorial_2 = tutorialRepository.findById(id).orElseGet(() -> null);
        System.out.println("second read: " + tutorial_2.getTitle());
        return tutorial_1;
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Tutorial findByIdRepeatableRead(Long id) {
        entityManager.clear();
        Tutorial tutorial = tutorialRepository.findById(id).orElseGet(() -> null);
        return tutorial;
    }

    private void sleep(Long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


}
