package ir.hesamghiasi.jug.transactionmanagement;

import ir.hesamghiasi.jug.transactionmanagement.entities.Tutorial;
import ir.hesamghiasi.jug.transactionmanagement.services.TutorialService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Slf4j
public class AnomaliesTest {

    @Autowired
    private TutorialService tutorialService;


    /**
     * ("|  (T1)           | (T2)                    |");
     * ("---------------------------------------------");
     * ("|  Read           |                      |");
     * ("|  update         |                      |");
     * ("|  save           |                      |");
     * ("|                 |  Read                |");
     * ("|  Rollback       |                      |");
     * ("|                 |  Commit              |");
     * ("| Commit          |                      |");
     * ("---------------------------------------------");
     */
    @Test
    public void dirtyRead() throws ExecutionException, InterruptedException {
        log.info("""
                ====================================================
                starting test
                ====================================================
                """);
        Tutorial tutorial = tutorialService.createTutorial(0L);
        final ExecutorService executorService = Executors.newFixedThreadPool(2);
        log.info("""
                ====================================================
                starting update with rollback
                ====================================================
                """);
        executorService
                .submit(() -> tutorialService.updateTutorialWithRollBack(tutorial.getId(), 10000L));
        Thread.sleep(2000L);
        log.info("""
                ====================================================
                starting dirty read
                ====================================================
                """);
        Future<Tutorial> submit_2 = executorService
                .submit(() -> tutorialService.findTutorialByIdReadUncommited(tutorial.getId(), 0L));
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);
        assertThat(submit_2.get().getTitle()).isEqualTo("test-updated");
        log.info("""
                ====================================================
                ending test
                ====================================================
                """);
    }

    @Test
    public void dirtyReadSolved() throws ExecutionException, InterruptedException {
        log.info("""
                ====================================================
                starting test
                ====================================================
                """);
        Tutorial tutorial = tutorialService.createTutorial(0L);
        final ExecutorService executorService = Executors.newFixedThreadPool(2);
        log.info("""
                ====================================================
                starting update with rollback
                ====================================================
                """);
        executorService
                .submit(() -> tutorialService.updateTutorialWithRollBack(tutorial.getId(), 10000L));
        Thread.sleep(2000L);
        log.info("""
                ====================================================
                starting dirty read
                ====================================================
                """);
        Future<Tutorial> submit_2 = executorService
                .submit(() -> tutorialService.findTutorialByIdReadCommited(tutorial.getId(), 0L));
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);
        assertThat(submit_2.get().getTitle()).isEqualTo("test");
        log.info("""
                ====================================================
                ending test
                ====================================================
                """);
    }


    /**
     * ("|  (T1)           | (T2)                    |");
     * ("---------------------------------------------");
     * ("|  Read           |                      |");
     * ("|                 | Read                 |");
     * ("|                 | update               |");
     * ("|                 | commit               |");
     * ("|  Read           |                      |");
     * ("---------------------------------------------");
     */
    @Test
    public void unrepeatableRead() throws InterruptedException {
        log.info("""
                ====================================================
                starting test
                ====================================================
                """);
        Tutorial tutorial = tutorialService.createTutorial(0L);
        final ExecutorService executorService = Executors.newFixedThreadPool(2);
        Future<Tutorial> submit_1 = executorService
                .submit(() -> tutorialService.doubleReadReadCommited(tutorial.getId(), 10000L));
        executorService
                .submit(() -> tutorialService.updateTutorial(tutorial.getId(), 0L));
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);
        log.info("""
                ====================================================
                ending test
                ====================================================
                """);
    }


    @Test
    public void unrepeatableReadSolved() throws InterruptedException {
        log.info("""
                ====================================================
                starting test
                ====================================================
                """);
        Tutorial tutorial = tutorialService.createTutorial(0L);
        final ExecutorService executorService = Executors.newFixedThreadPool(2);
        Future<Tutorial> submit_1 = executorService
                .submit(() -> tutorialService.doubleReadRepeataleRead(tutorial.getId(), 10000L));
        executorService
                .submit(() -> tutorialService.updateTutorial(tutorial.getId(), 0L));
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);
        log.info("""
                ====================================================
                ending test
                ====================================================
                """);
    }


    @Test
    public void tripleReadTest() throws InterruptedException {
        log.info("""
                ====================================================
                starting test
                ====================================================
                """);
        Tutorial tutorial = tutorialService.createTutorial(0L);
        final ExecutorService executorService = Executors.newFixedThreadPool(3);
        Future<Tutorial> submit_1 = executorService
                .submit(() -> tutorialService.findByIdRepeatableRead(tutorial.getId()));
        Thread.sleep(2000L);
        Future<Tutorial> submit_2 = executorService
                .submit(() -> tutorialService.findByIdRepeatableRead(tutorial.getId()));
        Thread.sleep(2000L);
        Future<Tutorial> submit_3 = executorService
                .submit(() -> tutorialService.findByIdRepeatableRead(tutorial.getId()));
        Thread.sleep(2000L);
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);
        log.info("""
                ====================================================
                ending test
                ====================================================
                """);
    }
}
