package kizilay.yusuf.balanceapi.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChangedAmountResource extends BaseResource {

    private double changedAmount;

    public ChangedAmountResource(double changedAmount) {
        this.changedAmount = changedAmount;
    }
}
