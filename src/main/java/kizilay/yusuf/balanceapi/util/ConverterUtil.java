package kizilay.yusuf.balanceapi.util;

import kizilay.yusuf.balanceapi.entity.UserBalance;
import kizilay.yusuf.balanceapi.model.UserBalanceResource;

public final class ConverterUtil {

    private ConverterUtil() {
    }

    public static UserBalance toEntity(UserBalanceResource resource) {
        UserBalance userBalance = new UserBalance();

        userBalance.setUserId(resource.getUserId());
        userBalance.setBalance(resource.getBalance());

        return userBalance;
    }
}
