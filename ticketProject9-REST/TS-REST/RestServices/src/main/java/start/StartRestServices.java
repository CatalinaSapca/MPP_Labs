package start;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import ticket.model.validators.ARValidator;
import ticket.persistance.ARRepository;
import ticket.persistance.jdbc.REST.ARRepositoryREST;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Properties;

@ComponentScan("TS")
@SpringBootApplication
public class StartRestServices {
    @Autowired
    private ObjectMapper objectMapper;

    public static void main(String[] args) {

        SpringApplication.run(StartRestServices.class, args);
    }

    @Bean(name="props")
    public Properties getBdProperties(){
        Properties serverProps=new Properties();
        try {
            serverProps.load(StartRestServices.class.getResourceAsStream("/ticketserver.properties"));
            System.out.println("Server properties set. ");
            serverProps.list(System.out);
            return serverProps;
        } catch (IOException e) {
            System.err.println("Cannot find ticketserver.properties "+e);
            return null;
        }
    }

    @Bean(name="validator")
    public ARValidator getARValidator(){
        return new ARValidator();
    }

    @Bean(name="repoAR")
    public ARRepository getARRepository(){
        return new ARRepositoryREST(getARValidator(), getBdProperties());
    }

    @PostConstruct
    public void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
    }
}



