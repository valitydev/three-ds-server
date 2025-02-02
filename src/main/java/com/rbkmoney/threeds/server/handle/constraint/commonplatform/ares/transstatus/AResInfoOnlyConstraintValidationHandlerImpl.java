package com.rbkmoney.threeds.server.handle.constraint.commonplatform.ares.transstatus;

import com.rbkmoney.threeds.server.config.properties.EnvironmentProperties;
import com.rbkmoney.threeds.server.domain.root.emvco.AReq;
import com.rbkmoney.threeds.server.domain.root.emvco.ARes;
import com.rbkmoney.threeds.server.domain.threedsrequestor.ThreeDSRequestorChallengeInd;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.commonplatform.ares.AResConstraintValidationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.domain.transaction.TransactionStatus.INFORMATIONAL_ONLY;
import static com.rbkmoney.threeds.server.dto.ConstraintType.PATTERN;
import static com.rbkmoney.threeds.server.utils.Wrappers.getValue;

@Component
@ConditionalOnProperty(name = "platform.mode", havingValue = "TEST_PLATFORM")
@RequiredArgsConstructor
public class AResInfoOnlyConstraintValidationHandlerImpl implements AResConstraintValidationHandler {

    private final EnvironmentProperties environmentProperties;

    @Override
    public boolean canHandle(ARes o) {
        return getValue(o.getTransStatus()) == INFORMATIONAL_ONLY;
    }

    @Override
    public ConstraintValidationResult handle(ARes o) {
        AReq aReq = (AReq) o.getRequestMessage();
        ThreeDSRequestorChallengeInd threeDSRequestorChallengeInd = aReq.getThreeDSRequestorChallengeInd();

        if (!(isSatisfactoryChallengeIndForTransStatus(threeDSRequestorChallengeInd)
                || (threeDSRequestorChallengeInd == ThreeDSRequestorChallengeInd.RESERVED_FOR_DS_USED_82
                && environmentProperties.getThreeDsServerRefNumber().equals("3DS_LOA_SER_DIPL_020200_00236")))) {
            return ConstraintValidationResult.failure(PATTERN, "transStatus");
        }

        return ConstraintValidationResult.success();
    }

    private boolean isSatisfactoryChallengeIndForTransStatus(
            ThreeDSRequestorChallengeInd threeDSRequestorChallengeInd) {
        return threeDSRequestorChallengeInd == ThreeDSRequestorChallengeInd.NO_CHALLENGE_RISK_ANALYSIS_PERFORMED
                || threeDSRequestorChallengeInd == ThreeDSRequestorChallengeInd.NO_CHALLENGE_DATA_SHARE_ONLY
                || threeDSRequestorChallengeInd == ThreeDSRequestorChallengeInd.NO_CHALLENGE_AUTH_ALREADY_PERFORMED;
    }
}
