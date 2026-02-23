package com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClientBuilder;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;
import software.amazon.awssdk.services.secretsmanager.model.ResourceNotFoundException;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class SecretsManagerTest {

    @Mock
    private SecretsManagerClient secretsManagerClient;

    private MockedStatic<SecretsManagerClient> mockedStaticClient;

    @BeforeEach
    void setUp() throws Exception {
        // Limpar a instância Singleton para evitar poluição entre os testes
        Field instanceField = SecretsManager.class.getDeclaredField("instance");
        instanceField.setAccessible(true);
        instanceField.set(null, null);

        // Preparamos o mock estático para o builder da AWS
        mockedStaticClient = Mockito.mockStatic(SecretsManagerClient.class);
        SecretsManagerClientBuilder builderMock = mock(SecretsManagerClientBuilder.class);
        lenient().when(SecretsManagerClient.builder()).thenReturn(builderMock);
        lenient().when(builderMock.region(any(Region.class))).thenReturn(builderMock);
        lenient().when(builderMock.credentialsProvider(any())).thenReturn(builderMock);
        lenient().when(builderMock.build()).thenReturn(secretsManagerClient);
    }

    @AfterEach
    void tearDown() {
        mockedStaticClient.close();
    }

    @Test
    void testGetInstanceIsSingleton() {
        SecretsManager instance1 = SecretsManager.getInstance();
        SecretsManager instance2 = SecretsManager.getInstance();
        assertNotNull(instance1);
        assertSame(instance1, instance2);
    }

    @Test
    void testBuilderWithoutCredentials() throws Exception {
        GetSecretValueResponse mockResponse = GetSecretValueResponse.builder()
                .secretString("{\"dbuser\":\"admin\"}")
                .build();
        when(secretsManagerClient.getSecretValue(any(GetSecretValueRequest.class))).thenReturn(mockResponse);

        SecretsManager manager = SecretsManager.getInstance().builder(
                "", "", "us-east-1", "ProdSecret");

        assertNotNull(manager);
        assertEquals("admin", manager.get("dbuser"));
    }

    @Test
    void testBuilderWithCredentials() throws Exception {
        GetSecretValueResponse mockResponse = GetSecretValueResponse.builder()
                .secretString("{\"dbpass\":\"1234\"}")
                .build();
        when(secretsManagerClient.getSecretValue(any(GetSecretValueRequest.class))).thenReturn(mockResponse);

        SecretsManager manager = SecretsManager.getInstance().builder(
                "key", "secret", "us-east-1", "ProdSecret");

        assertNotNull(manager);
        assertEquals("1234", manager.get("dbpass"));
    }

    @Test
    void testExceptionDuringInitialization() {
        when(secretsManagerClient.getSecretValue(any(GetSecretValueRequest.class)))
                .thenThrow(ResourceNotFoundException.builder().message("Secret not found").build());

        assertThrows(ResourceNotFoundException.class, () -> {
            SecretsManager.getInstance().builder("key", "secret", "us-east-1", "ProdSecret");
        });
    }

    @Test
    void testGetMethodThrowsExceptionWhenSecretNotFound() throws Exception {
        SecretsManager manager = SecretsManager.getInstance();

        // Simular inicialização fake
        Map<String, JsonNode> fakeMap = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        fakeMap.put("MyFakeSecret", mapper.readTree("{\"real_key\":\"val\"}"));

        ReflectionTestUtils.setField(manager, "secretList", fakeMap);
        ReflectionTestUtils.setField(manager, "secretName", "MyFakeSecret");

        assertEquals("val", manager.get("real_key"));
        assertThrows(NullPointerException.class, () -> manager.get("non_existent"));
    }
}
