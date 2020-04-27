package com.rbkmoney.threeds.server.service.impl;

import com.rbkmoney.threeds.server.constants.DirectoryServerProvider;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.root.proprietary.PArq;
import com.rbkmoney.threeds.server.exeption.DirectoryServerRoutingException;
import com.rbkmoney.threeds.server.service.CacheService;
import com.rbkmoney.threeds.server.service.DirectoryServerRouter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static java.util.Arrays.stream;

@Service
@RequiredArgsConstructor
public class DirectoryServerRouterImpl implements DirectoryServerRouter {

    private final CacheService cacheService;

    @Override
    public DirectoryServerProvider route(Message message) {
        if (!(message instanceof PArq)) {
            return DirectoryServerProvider.TEST;
        }

        PArq pArq = (PArq) message;

        if (pArq.getXULTestCaseRunId() != null) {
            return DirectoryServerProvider.TEST;
        }

        String acctNumber = pArq.getAcctNumber();

        return stream(DirectoryServerProvider.values())
                .filter(provider -> cacheService.isInCardRange(provider.getTag(), acctNumber))
                .findFirst()
                .orElseThrow(() -> new DirectoryServerRoutingException("Unable to route pArq message with id=" + pArq.getThreeDSServerTransID()));
    }
}
