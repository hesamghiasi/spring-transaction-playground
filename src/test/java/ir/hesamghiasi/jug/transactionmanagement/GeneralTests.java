package ir.hesamghiasi.jug.transactionmanagement;

import ir.hesamghiasi.jug.transactionmanagement.entities.Tutorial;
import ir.hesamghiasi.jug.transactionmanagement.services.TutorialService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

@SpringBootTest
@Slf4j
public class GeneralTests {

    @Autowired
    private TutorialService tutorialService;


    @Test
    public void findTutorialTest_noAnnotation() throws ExecutionException, InterruptedException {
        log.info("""
                ====================================================
                starting test
                ====================================================
                """);
        final ExecutorService executorService = Executors.newFixedThreadPool(2);
        Future<Tutorial> submit = executorService
                .submit(() -> tutorialService.findTutorialByIdNotAnnotated(1L, 0L));
        Future<Tutorial> submit2 = executorService
                .submit(() -> tutorialService.findTutorialByIdNotAnnotated(1L, 0L));
        Tutorial tutorial = submit.get();
        assertThat(tutorial).isNull();
        log.info("""
                ====================================================
                ending test
                ====================================================
                """);
    }


    @Test
    public void findTutorialTest_propagationNever() throws ExecutionException, InterruptedException {
        log.info("""
                ====================================================
                starting test
                ====================================================
                """);
        final ExecutorService executorService = Executors.newFixedThreadPool(1);
        Future<Tutorial> submit = executorService
                .submit(() -> tutorialService.findTutorialByIdPropagationNever(1L, 0L));
        Tutorial tutorial = submit.get();
        assertThat(tutorial).isNull();
        log.info("""
                ====================================================
                ending test
                ====================================================
                """);
    }

    @Test
    public void findTutorialTest_defaultAnnotation_repoPropagationNever() throws ExecutionException, InterruptedException {
        log.info("""
                ====================================================
                starting test
                ====================================================
                """);
        final ExecutorService executorService = Executors.newFixedThreadPool(1);
        Future<Tutorial> submit = executorService
                .submit(() -> tutorialService.findTutorialByIdDefaultAnnotation(1L, 0L));
        assertThatExceptionOfType(ExecutionException.class)
                .isThrownBy(() -> submit.get())
                .withMessageContaining("IllegalTransactionStateException");
        executorService.awaitTermination(1, TimeUnit.MINUTES);
        executorService.shutdown();
        log.info("""
                ====================================================
                ending test
                ====================================================
                """);
    }


    @Test
    public void findTutorialTest_defaultAnnotation() throws ExecutionException, InterruptedException {
        log.info("""
                ====================================================
                starting findTutorialTest_propagationNever
                ====================================================
                """);
        final ExecutorService executorService = Executors.newFixedThreadPool(1);
        Future<Tutorial> submit = executorService
                .submit(() -> tutorialService.findTutorialByIdDefaultAnnotation(1L, 0L));
        Tutorial tutorial = submit.get();
        assertThat(tutorial).isNull();
        log.info("""
                ====================================================
                ending findTutorialTest_propagationNever
                ====================================================
                """);
    }
}
