package com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.*;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class SecretsManager {

    private static SecretsManager instance;

    private static SecretsManagerClient secretsManagerClient;

    private Map<String, JsonNode> secretList = new HashMap<String, JsonNode>();

    private String secretName;

    private SecretsManager() {}

    public static SecretsManager getInstance() {
        if (instance == null) {
            instance = new SecretsManager();
        }
        return instance;
    }

    public String get(String key) throws SecretsManagerException {
        return secretList.get(secretName).get(key).textValue();
    }

    public SecretsManager builder( String accessKeyId, String secretAccessKey, String region, String secretName ) throws Exception {
        this.secretName = secretName;

        if ( accessKeyId == null || accessKeyId.isEmpty() || secretAccessKey == null || secretAccessKey.isEmpty() ) {
            secretsManagerClient = SecretsManagerClient.builder()
                    .region( Region.of( region ) )
                    .build();
        } else {
            AwsBasicCredentials awsBasicCredentials = AwsBasicCredentials.create(accessKeyId, secretAccessKey);
            secretsManagerClient = SecretsManagerClient.builder()
                    .region( Region.of( region ) )
                    .credentialsProvider(StaticCredentialsProvider.create(awsBasicCredentials))
                    .build();
        }

        initialize( secretName );
        return this;
    }

    private void initialize( String secretName ){
        String secret;
        GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder().secretId(secretName).build();
        GetSecretValueResponse getSecretValueResponse = null;

        try {
            getSecretValueResponse = secretsManagerClient.getSecretValue(getSecretValueRequest);
        } catch (DecryptionFailureException | InternalServiceErrorException | InvalidParameterException |
                 InvalidRequestException | ResourceNotFoundException e) {
            throw e;
        }

        if (getSecretValueResponse.secretString() != null) {
            secret = getSecretValueResponse.secretString();
        } else {
            secret = new String(Base64.getDecoder().decode(getSecretValueResponse.secretBinary().asByteBuffer()).array());
        }

        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode secretsJson = null;
        try {
            secretsJson = objectMapper.readTree(secret);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        secretList.put(secretName, secretsJson);
    }

}
