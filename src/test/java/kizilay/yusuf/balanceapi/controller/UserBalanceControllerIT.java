package kizilay.yusuf.balanceapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kizilay.yusuf.balanceapi.entity.UserBalance;
import kizilay.yusuf.balanceapi.model.ChangedAmountResource;
import kizilay.yusuf.balanceapi.model.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserBalanceControllerIT {

    private static final ObjectMapper mapper = new ObjectMapper();

    @LocalServerPort
    private int port;
    TestRestTemplate restTemplate = new TestRestTemplate();

    private static final String HOST = "http://localhost:";
    private static final String API_URL = "/bilyoner/userBalances";
    private static final String SLASH = "/";


    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "/insert_data.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "/clean_data.sql")
    public void findUserBalance_Should_Return200_When_Request_Valid() {
        ResponseEntity<Response> response = restTemplate.getForEntity(createURI(1), Response.class);

        UserBalance userBalance = deserialize(response.getBody().getSuccess(), UserBalance.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Long.valueOf(1), userBalance.getUserId());
        assertEquals(Double.valueOf(30.00), Double.valueOf(userBalance.getBalance()));

    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "/insert_data.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "/clean_data.sql")
    public void updateUserBalance_Should_Return200_When_Request_Valid() {
        ResponseEntity<Response> response = restTemplate.exchange(createURI(1), HttpMethod.PUT, createUpdateBalanceRequest(-5), Response.class);

        UserBalance userBalance = deserialize(response.getBody().getSuccess(), UserBalance.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Long.valueOf(1), userBalance.getUserId());
        assertEquals(Double.valueOf(25.00), Double.valueOf(userBalance.getBalance()));

    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "/insert_data.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "/clean_data.sql")
    public void updateUserBalance_Should_Return400_When_UserBalanceNotExist() {
        ResponseEntity<Response> response = restTemplate.exchange(createURI(2), HttpMethod.PUT, createUpdateBalanceRequest(-5), Response.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("User balance not found for given identifier:2", response.getBody().getError());
    }

    @Test
    @Sql("/insert_data.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "/insert_data.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "/clean_data.sql")
    public void concurrent_updateUserBalance_Should_Result_ConsistentData() {
        int concurrencyLevel = 100;

        ExecutorService executorService = Executors.newFixedThreadPool(concurrencyLevel);

        /**
         *  Başlangıçta user'ın balance'i 30.00.
         *  100 tane balance update isteğini concurrent olarak gönderiyorum.
         *  Bu isteklerin yarısı user balance'a 5 (+5) ekliyor. Yarısı 5 (-5)çıkarıyor.
         *  Tüm thread'ler bittikten sonra user balance ilk değeri olan 30'u vermeli.
         *  {@link kizilay.yusuf.balanceapi.repository.UserBalanceRepository#findById(Long)} üzerinde PesimisticLock var
         *  bu lock sayesinde test geçiyor. Lock'ı kaldırınca fail ediyor.
         */

        String uri = createURI(1);

        AtomicInteger count = new AtomicInteger();

        for (int i = 0; i < concurrencyLevel; i++) {
            executorService.submit(() -> {
                double changedAmount = count.getAndIncrement() % 2 == 0 ? 5 : -5;
                restTemplate.exchange(uri, HttpMethod.PUT, createUpdateBalanceRequest(changedAmount), Response.class);
            });

        }

        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.MINUTES);
        } catch (InterruptedException e) {

        }

        ResponseEntity<Response> response = restTemplate.getForEntity(createURI(1), Response.class);

        UserBalance userBalance = deserialize(response.getBody().getSuccess(), UserBalance.class);

        assertEquals(Double.valueOf(30.00), Double.valueOf(userBalance.getBalance()));
    }

    private HttpEntity<ChangedAmountResource> createUpdateBalanceRequest(double changedAmount) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        return new HttpEntity<>(new ChangedAmountResource(changedAmount), headers);
    }

    private String createURI(int userId) {
        StringBuilder builder = new StringBuilder(HOST);
        return builder.append(port).append(API_URL).append(SLASH).append(userId).toString();
    }

    private <T> T deserialize(JsonNode node, Class<T> type) {
        try {
            return mapper.treeToValue(node, type);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return null;
    }
}

