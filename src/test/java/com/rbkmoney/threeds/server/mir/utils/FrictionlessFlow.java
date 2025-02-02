package com.rbkmoney.threeds.server.mir.utils;

import com.rbkmoney.threeds.server.config.utils.JsonMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

@RequiredArgsConstructor
public class FrictionlessFlow {

    private final JsonMapper jsonMapper;

    public void givenDsStub(String testCase) {
        stubFor(post(urlEqualTo("/"))
                .withRequestBody(equalToJson(requestToDsServer(testCase), true, true))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withHeader("x-ul-testcaserun-id", testCase)
                        .withBody(responseFromDsServer(testCase))));
    }

    public String requestToThreeDsServer(String testCase) {
        return readPArq(testCase);
    }

    public String responseFromThreeDsServer(String testCase) {
        return readPArs(testCase);
    }

    private String requestToDsServer(String testCase) {
        return readAReq(testCase);
    }

    private String responseFromDsServer(String testCase) {
        return readARes(testCase);
    }

    private String readPArq(String testCase) {
        return readMessage("mir/" + testCase + "/parq.json");
    }

    private String readPArs(String testCase) {
        return readMessage("mir/" + testCase + "/pars.json");
    }

    private String readAReq(String testCase) {
        return readMessage("mir/" + testCase + "/areq.json");
    }

    private String readARes(String testCase) {
        return readMessage("mir/" + testCase + "/ares.json");
    }

    private String readMessage(String fullPath) {
        return jsonMapper.readStringFromFile(fullPath);
    }
}

