package kizilay.yusuf.balanceapi.controller;

import kizilay.yusuf.balanceapi.entity.UserBalance;
import kizilay.yusuf.balanceapi.repository.UserBalanceRepository;
import kizilay.yusuf.balanceapi.service.UserBalanceService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UserBalanceController.class)
public class UserBalanceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserBalanceService userBalanceService;


    @Test
    public void should_CreateUserBalance_When_ValidRequest() throws Exception {
        when(userBalanceService.saveUserBalance(any(UserBalance.class))).thenReturn(createUserBalance(1L, 30.00));

        MvcResult result = mockMvc.perform(post("/bilyoner/userBalances")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"balance\": 30}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).andReturn();

        assertEquals("{\"success\":{\"userId\":1,\"balance\":30.0,\"createdDate\":null,\"updatedDate\":null}}", result.getResponse().getContentAsString());
    }

    @Test
    public void should_ReturnUserBalance_When_UserBalanceExist() throws Exception {
        when(userBalanceService.findUserBalance(1L)).thenReturn(createUserBalance(1L, 30.00));

        MvcResult result = mockMvc.perform(get("/bilyoner/userBalances/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        assertEquals("{\"success\":{\"userId\":1,\"balance\":30.0,\"createdDate\":null,\"updatedDate\":null}}", result.getResponse().getContentAsString());
    }

    @Test
    public void should_ReturnNotFound_When_UserBalanceDoesNotExist() throws Exception {
        when(userBalanceService.findUserBalance(1L)).thenReturn(null);

        mockMvc.perform(get("/bilyoner/userBalances/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void should_ReturnUpdatedUserBalance_When_ValidRequest() throws Exception {
        when(userBalanceService.updateUserBalance(1L, -5)).thenReturn(createUserBalance(1L, 25));

        MvcResult result = mockMvc.perform(put("/bilyoner/userBalances/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"changedAmount\": -5}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        assertEquals("{\"success\":{\"userId\":1,\"balance\":25.0,\"createdDate\":null,\"updatedDate\":null}}", result.getResponse().getContentAsString());
    }

    private UserBalance createUserBalance(Long userId, double balance) {
        UserBalance userBalance = new UserBalance();
        userBalance.setUserId(userId);
        userBalance.setBalance(balance);

        return userBalance;
    }
}
