package ir.hesamghiasi.jug.transactionmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = { DataSourceTransactionManagerAutoConfiguration.class })
public class TransactionManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(TransactionManagementApplication.class, args);
	}

}
