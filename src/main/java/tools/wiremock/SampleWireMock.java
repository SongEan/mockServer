package tools.wiremock;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.common.ConsoleNotifier;
import com.github.tomakehurst.wiremock.core.Options;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

/**
 * Created by IntelliJ Idea IDEA
 * java version "1.8.0_91"
 * Author: Ean Song
 * ProjectName：mockServer
 * DateTime: 2020-09-01 19:17
 */
public class SampleWireMock {
    public static WireMockServer wireMockServer;

    @BeforeAll
    static void beforeAll() {
        wireMockServer = new WireMockServer(wireMockConfig().port(8889));
        configureFor("localhost", 8889);
        WireMockConfiguration.wireMockConfig().notifier(new ConsoleNotifier(true));

//        配置响应的编码方式
        WireMockConfiguration.wireMockConfig().useChunkedTransferEncoding(Options.ChunkedEncodingPolicy.BODY_FILE);
        wireMockServer.start();
    }

    @AfterAll
    static void afterAll() {
        wireMockServer.stop();
    }

    @Test
    void sampleTest() throws InterruptedException {
        stubFor(get(urlEqualTo("/my/resource"))
                .withHeader("Accept", equalTo("text/xml"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "text/xml")
                        .withBody("<response>Some content</response>")));

        TimeUnit.MINUTES.sleep(1);


        stubFor(get(urlEqualTo("/my/resource"))
                .withHeader("Accept", equalTo("text/xml"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "text/xml")
                        .withBody("<response>1分钟后返回错误</response>")));

        TimeUnit.MINUTES.sleep(10);
    }

    @Test
    void baseStubbing() {
//        stubFor(get(urlEqualTo("/some/thing")).)
    }
}
