package com.nailgun.jhtest.config;


import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.nailgun.jhtest.config.oauth2.OAuth2AuthenticationReadConverter;
import org.mongeez.Mongeez;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.convert.CustomConversions;
import org.springframework.data.mongodb.core.mapping.event.ValidatingMongoEventListener;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Configuration
@Profile("!" + Constants.SPRING_PROFILE_CLOUD)
@EnableMongoRepositories("com.nailgun.jhtest.repository")
@Import(value = MongoAutoConfiguration.class)
@EnableMongoAuditing(auditorAwareRef = "springSecurityAuditorAware")
public class DatabaseConfiguration extends AbstractMongoConfiguration  {

    private final Logger log = LoggerFactory.getLogger(DatabaseConfiguration.class);

    @Value("${spring.data.mongodb.host:localhost}")
    private String host;
    @Value("${spring.data.mongodb.port:27017}")
    private Integer port;
    @Value("${spring.data.mongodb.username:#{null}}")
    private String username;
    @Value("${spring.data.mongodb.database}")
    private String database;
    @Value("${spring.data.mongodb.password:#{null}}")
    private String password;

    @Bean
    public ValidatingMongoEventListener validatingMongoEventListener() {
        return new ValidatingMongoEventListener(validator());
    }

    @Bean
    public LocalValidatorFactoryBean validator() {
        return new LocalValidatorFactoryBean();
    }

    @Override
    protected String getDatabaseName() {
        return database;
    }

    @Override
    public Mongo mongo() throws Exception {
        boolean isDefaultCredentials = username == null || password == null;
        List<ServerAddress> serverAddressList = Collections.singletonList(
            new ServerAddress(host, port));
        return isDefaultCredentials ? new MongoClient(serverAddressList) :
            new MongoClient(serverAddressList,
            Collections.singletonList(MongoCredential.createCredential(
                username, database, password.toCharArray())));
    }

    @Bean
    public CustomConversions customConversions() {
        List<Converter<?, ?>> converterList = new ArrayList<>();
        OAuth2AuthenticationReadConverter converter = new OAuth2AuthenticationReadConverter();
        converterList.add(converter);
        return new CustomConversions(converterList);
    }

    @Bean
    @Profile("!" + Constants.SPRING_PROFILE_PRODUCTION)
    public Mongeez mongeez() throws Exception {
        log.debug("Configuring Mongeez");
        Mongeez mongeez = new Mongeez();
        mongeez.setFile(new ClassPathResource("/config/mongeez/master.xml"));
        mongeez.setMongo(mongo());
        mongeez.setDbName(database);
        mongeez.process();
        return mongeez;
    }
}
