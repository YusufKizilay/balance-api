package kizilay.yusuf.balanceapi.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "USER_BALANCE")
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class UserBalance implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "USER_ID")
    private Long userId;

    private double balance;

    @Column(name = "CREATE_DATE")
    @CreatedDate
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm")
    private Date createdDate;

    @Column(name = "UPDATE_DATE")
    @LastModifiedDate
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm")
    private Date updatedDate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userBalance", cascade = CascadeType.PERSIST)
    @JsonIgnore
    private List<UserBalanceHistory> balanceHistories = new ArrayList<>();

    public void changeBalance(double changedAmount) {
        this.balance = this.balance + changedAmount;
    }

}
