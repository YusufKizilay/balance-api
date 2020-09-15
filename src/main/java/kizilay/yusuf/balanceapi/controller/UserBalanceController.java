package kizilay.yusuf.balanceapi.controller;


import kizilay.yusuf.balanceapi.entity.UserBalance;
import kizilay.yusuf.balanceapi.model.ChangedAmountResource;
import kizilay.yusuf.balanceapi.model.Response;
import kizilay.yusuf.balanceapi.model.UserBalanceResource;
import kizilay.yusuf.balanceapi.service.UserBalanceService;
import kizilay.yusuf.balanceapi.util.ConverterUtil;
import kizilay.yusuf.balanceapi.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserBalanceController extends BaseController {

    private UserBalanceService userBalanceService;

    @Autowired
    public UserBalanceController(UserBalanceService userBalanceService) {
        this.userBalanceService = userBalanceService;
    }

    @PostMapping("/userBalances")
    public ResponseEntity<Response> saveUserBalance(@RequestBody UserBalanceResource resource) {
        UserBalance savedUserBalance = userBalanceService.saveUserBalance(ConverterUtil.toEntity(resource));

        return ResponseUtil.successResponse(savedUserBalance, HttpStatus.CREATED);
    }

    @GetMapping("/userBalances/{userId}")
    public ResponseEntity<Response> findUserBalance(@PathVariable Long userId) {
        UserBalance userBalance = userBalanceService.findUserBalance(userId);


        if (null != userBalance) {
            return ResponseUtil.successResponse(userBalance, HttpStatus.OK);
        }

        return ResponseUtil.errorResponse("", HttpStatus.NOT_FOUND);

    }

    @PutMapping("/userBalances/{userId}")
    public ResponseEntity<Response> updateUserBalance(@RequestBody ChangedAmountResource changedAmountResource, @PathVariable Long userId) {
        UserBalance userBalance = userBalanceService.updateUserBalance(userId, changedAmountResource.getChangedAmount());

        return ResponseUtil.successResponse(userBalance, HttpStatus.OK);
    }

}
