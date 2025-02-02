package com.rbkmoney.threeds.server.handle.constraint.rbkmoneyplatform.rbkmoneyauthenticationrequest.merchantriskindicator;

import com.rbkmoney.threeds.server.domain.merchant.MerchantRiskIndicatorWrapper;
import com.rbkmoney.threeds.server.domain.root.rbkmoney.RBKMoneyAuthenticationRequest;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.commonplatform.utils.StringValidator;
import com.rbkmoney.threeds.server.handle.constraint.rbkmoneyplatform.rbkmoneyauthenticationrequest.RBKMoneyAuthenticationRequestConstraintValidationHandler;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.dto.ConstraintType.PATTERN;
import static com.rbkmoney.threeds.server.utils.Wrappers.getGarbageValue;

@Component
@RequiredArgsConstructor
public class RBKMoneyAuthenticationRequestMerchantRiskIndicatorContentConstraintValidationHandlerImpl
        implements RBKMoneyAuthenticationRequestConstraintValidationHandler {

    private final StringValidator stringValidator;
    private final EmailValidator emailValidator = EmailValidator.getInstance();

    @Override
    public boolean canHandle(RBKMoneyAuthenticationRequest o) {
        return o.getMerchantRiskIndicator() != null;
    }


    @Override
    public ConstraintValidationResult handle(RBKMoneyAuthenticationRequest o) {
        MerchantRiskIndicatorWrapper merchantRiskIndicator = o.getMerchantRiskIndicator();

        if (!emailValidator.isValid(merchantRiskIndicator.getDeliveryEmailAddress())) {
            return ConstraintValidationResult.failure(PATTERN, "merchantRiskIndicator.deliveryEmailAddress");
        }

        if (stringValidator.isNotNull(merchantRiskIndicator.getGiftCardAmount())) {
            ConstraintValidationResult validationResult = stringValidator
                    .validateStringWithMaxLength("merchantRiskIndicator.giftCardAmount", 15,
                            merchantRiskIndicator.getGiftCardAmount());
            if (!validationResult.isValid()) {
                return validationResult;
            }

            if (!NumberUtils.isCreatable(merchantRiskIndicator.getGiftCardAmount())) {
                return ConstraintValidationResult.failure(PATTERN, "merchantRiskIndicator.giftCardAmount");
            }
        }

        if (stringValidator.isNotNull(merchantRiskIndicator.getGiftCardCount())) {
            ConstraintValidationResult validationResult = stringValidator
                    .validateStringWithConstLength("merchantRiskIndicator.giftCardCount", 2,
                            merchantRiskIndicator.getGiftCardCount());
            if (!validationResult.isValid()) {
                return validationResult;
            }

            if (!NumberUtils.isCreatable(merchantRiskIndicator.getGiftCardCount())) {
                return ConstraintValidationResult.failure(PATTERN, "merchantRiskIndicator.giftCardCount");
            }
        }

        if (stringValidator.isNotNull(merchantRiskIndicator.getGiftCardCurr())) {
            ConstraintValidationResult validationResult = stringValidator
                    .validateStringWithConstLength("merchantRiskIndicator.giftCardCurr", 3,
                            merchantRiskIndicator.getGiftCardCurr());
            if (!validationResult.isValid()) {
                return validationResult;
            }

            if (!NumberUtils.isCreatable(merchantRiskIndicator.getGiftCardCurr())) {
                return ConstraintValidationResult.failure(PATTERN, "merchantRiskIndicator.giftCardCurr");
            }
        }

        if (getGarbageValue(merchantRiskIndicator.getDeliveryTimeframe()) != null) {
            return ConstraintValidationResult.failure(PATTERN, "merchantRiskIndicator.deliveryTimeframe");
        }

        if (getGarbageValue(merchantRiskIndicator.getPreOrderDate()) != null) {
            return ConstraintValidationResult.failure(PATTERN, "merchantRiskIndicator.preOrderDate");
        }

        if (getGarbageValue(merchantRiskIndicator.getPreOrderPurchaseInd()) != null) {
            return ConstraintValidationResult.failure(PATTERN, "merchantRiskIndicator.preOrderPurchaseInd");
        }

        if (getGarbageValue(merchantRiskIndicator.getReorderItemsInd()) != null) {
            return ConstraintValidationResult.failure(PATTERN, "merchantRiskIndicator.reorderItemsInd");
        }

        if (getGarbageValue(merchantRiskIndicator.getShipIndicator()) != null) {
            return ConstraintValidationResult.failure(PATTERN, "merchantRiskIndicator.shipIndicator");
        }

        return ConstraintValidationResult.success();
    }
}
