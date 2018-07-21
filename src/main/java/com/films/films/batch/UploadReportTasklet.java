package com.films.films.batch;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.films.films.FilmsApplication;
import com.films.films.batch.configuration.ExecutionConfiguration;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.sql.Date;

@Component
@AllArgsConstructor
@Slf4j
public class UploadReportTasklet implements Tasklet {
    private final ExecutionConfiguration executionConfiguration;
    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        try {
            String fileName = executionConfiguration.getFilePath();
            Path filePath = Paths.get(fileName);
            String apiUrl = "https://www.googleapis.com/upload/storage/v1/b/film-reports/o?uploadType=media&name="+fileName;
            
            ClassLoader classLoader = getClass().getClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream("credentials.json");
            GoogleCredential credential = GoogleCredential.fromStream(inputStream);
            PrivateKey privateKey = credential.getServiceAccountPrivateKey();
            String privateKeyId = credential.getServiceAccountPrivateKeyId();

            Algorithm algorithm = Algorithm.RSA256(null, (RSAPrivateKey) privateKey);
            String signedJwt = JWT.create()
                    .withKeyId(privateKeyId)
                    .withIssuer("films-batch-service@films-batch.iam.gserviceaccount.com")
                    .withSubject("films-batch-service@films-batch.iam.gserviceaccount.com")
                    .withAudience("https://www.googleapis.com/storage")
                    .withIssuedAt(DateTime.now().toDate())
                    .withExpiresAt(new Date(DateTime.now().getMillis() + 3600 * 1000L))
                    .sign(algorithm);

            RestTemplate restTemplate = new RestTemplate();
            LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
            map.add("file", filePath);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            headers.set("Authorization", "Bearer "+signedJwt);

            HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);
            restTemplate.postForEntity(apiUrl, requestEntity, String.class);

            log.info("file path is "+ fileName);
        } catch (Exception ex) {
            log.error(ex.toString());
        }
        return null;
    }
}
