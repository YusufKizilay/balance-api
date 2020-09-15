package kizilay.yusuf.balanceapi.service;

import kizilay.yusuf.balanceapi.entity.UserBalance;
import kizilay.yusuf.balanceapi.entity.UserBalanceHistory;
import kizilay.yusuf.balanceapi.exception.UserBalanceNotFoundException;
import kizilay.yusuf.balanceapi.repository.UserBalanceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Optional;

@Service
public class UserBalanceService {

    private UserBalanceRepository userBalanceRepository;

    public UserBalanceService(UserBalanceRepository userBalanceRepository) {
        this.userBalanceRepository = userBalanceRepository;
    }

    public UserBalance saveUserBalance(final UserBalance userBalance) {
        return this.userBalanceRepository.save(userBalance);
    }

    public UserBalance findUserBalance(final Long userId) {
        Optional<UserBalance> userBalance = userBalanceRepository.findById(userId);

        if (userBalance.isPresent()) {
            return userBalance.get();
        }

        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public UserBalance updateUserBalance(final Long userId, final double changedAmount) {
        UserBalance userBalance = findUserBalance(userId);

        if (null == userBalance) {
            throw new UserBalanceNotFoundException(String.format("User balance not found for given identifier:%d", userId));
        }

        userBalance.changeBalance(changedAmount);
        userBalance.getBalanceHistories().add(new UserBalanceHistory(userBalance, changedAmount, Calendar.getInstance().getTime()));

        return userBalance;
    }
}
