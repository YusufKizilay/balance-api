package kizilay.yusuf.balanceapi.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserBalanceResource extends BaseResource {
    private long userId;

    private double balance;
}
