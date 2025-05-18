package com.curtin.securehire.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class VectorIndexInitializer implements ApplicationListener<ApplicationStartedEvent> {
    private static final Logger logger = LoggerFactory.getLogger(VectorIndexInitializer.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        logger.info("Creating vector indexes...");

        try {
            // Create index for job embeddings
            jdbcTemplate.execute(
                    "CREATE INDEX IF NOT EXISTS idx_job_embeddings_vector " +
                            "ON job_embeddings USING ivfflat(embedding vector_cosine_ops) " +
                            "WITH (lists = 100)"
            );

            // Create index for user embeddings
            jdbcTemplate.execute(
                    "CREATE INDEX IF NOT EXISTS idx_user_embeddings_vector " +
                            "ON user_embeddings USING ivfflat(embedding vector_cosine_ops)"
            );

            logger.info("Vector indexes created successfully");
        } catch (Exception e) {
            logger.error("Failed to create vector indexes", e);
        }
    }
}

