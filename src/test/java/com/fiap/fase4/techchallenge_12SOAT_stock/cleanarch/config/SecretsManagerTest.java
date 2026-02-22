package com.fiap.fase4.techchallenge_12SOAT_stock.cleanarch.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class SecretsManagerTest {

    @BeforeEach
    void setUp() {
        // Reset instance se necessário futuramente usando reflection
    }

    @Test
    void testGetInstance() {
        // Act
        SecretsManager instance1 = SecretsManager.getInstance();
        SecretsManager instance2 = SecretsManager.getInstance();

        // Assert
        assertNotNull(instance1);
        assertNotNull(instance2);
        // Verifica padrão Singleton
        assert (instance1 == instance2);
    }
}
