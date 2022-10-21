package ir.hesamghiasi.jug.transactionmanagement.config;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

/**
 * @author Hesam Ghiasi created on 6/23/22 
 */
@Configuration
@EnableTransactionManagement
public class DbConfig {

//    @Bean
//    @Autowired
//    public PlatformTransactionManager txManager(SessionFactory sessionFactory) {
//        return new HibernateTransactionManager(sessionFactory); // (2)
//    }

    @Autowired
    private DataSource dataSource;

    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean emf() {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource);
        emf.setPackagesToScan(new String[] {"ir.hesamghiasi.*"});
        emf.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        return emf;
    }


    @Bean(name = "transactionManager")
    @Autowired
    public JpaTransactionManager transactionManager(EntityManagerFactory emf) {
        JpaTransactionManager tm = new JpaTransactionManager();
        tm.setEntityManagerFactory(emf);
        tm.setDataSource(dataSource);
        return tm;
    }


}
