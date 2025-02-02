package com.rbkmoney.threeds.server.service;

import com.rbkmoney.threeds.server.domain.acs.AcsDecConInd;
import com.rbkmoney.threeds.server.domain.device.DeviceChannel;
import com.rbkmoney.threeds.server.dto.ChallengeFlowTransactionInfo;
import com.rbkmoney.threeds.server.service.testplatform.TestPlatformChallengeFlowTransactionInfoStorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class TestPlatformChallengeFlowTransactionInfoStorageServiceTest {

    private static final String TEST_TAG = UUID.randomUUID().toString();
    private static final String TRAP = "TRAP";

    private TestPlatformChallengeFlowTransactionInfoStorageService
            testPlatformChallengeFlowTransactionInfoStorageService;

    @BeforeEach
    public void setUp() {
        testPlatformChallengeFlowTransactionInfoStorageService =
                new TestPlatformChallengeFlowTransactionInfoStorageService();
    }

    @Test
    public void shouldSaveAndGetTransactionInfo() {
        ChallengeFlowTransactionInfo transactionInfo = ChallengeFlowTransactionInfo.builder()
                .threeDsServerTransId(TEST_TAG)
                .deviceChannel(DeviceChannel.APP_BASED)
                .decoupledAuthMaxTime(LocalDateTime.MIN)
                .acsDecConInd(AcsDecConInd.DECOUPLED_AUTH_WILL_BE_USED)
                .dsProviderId("visa")
                .messageVersion("2.1.0")
                .acsUrl("https://mon.rbkmoney.com/grafana/")
                .build();

        ChallengeFlowTransactionInfo trap = ChallengeFlowTransactionInfo.builder().build();
        testPlatformChallengeFlowTransactionInfoStorageService.saveChallengeFlowTransactionInfo(TRAP, trap);

        // When
        testPlatformChallengeFlowTransactionInfoStorageService
                .saveChallengeFlowTransactionInfo(TEST_TAG, transactionInfo);

        // Then
        ChallengeFlowTransactionInfo actual =
                testPlatformChallengeFlowTransactionInfoStorageService.getChallengeFlowTransactionInfo(TEST_TAG);
        assertThat(actual).isEqualTo(transactionInfo);
    }
}
