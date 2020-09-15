package kizilay.yusuf.balanceapi.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "USER_BALANCE_HISTORY")
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class UserBalanceHistory {

    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long userBalanceHistoryId;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private UserBalance userBalance;

    @Column(name = "CHANGED_AMOUNT")
    private double changedAmount;

    @Column(name = "PROCESS_DATE")
    private Date processDate;

    @Column(name = "CREATE_DATE")
    @CreatedDate
    private Date createdDate;

    @Column(name = "UPDATE_DATE")
    @LastModifiedDate
    private Date updatedDate;

    public UserBalanceHistory(UserBalance userBalance, double changedAmount, Date processDate) {
        this.userBalance = userBalance;
        this.changedAmount = changedAmount;
        this.processDate = processDate;
    }
}
