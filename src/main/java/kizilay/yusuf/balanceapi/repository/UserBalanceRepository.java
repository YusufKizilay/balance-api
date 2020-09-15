package kizilay.yusuf.balanceapi.repository;

import kizilay.yusuf.balanceapi.entity.UserBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.util.Optional;

@Repository
public interface UserBalanceRepository extends JpaRepository<UserBalance, Long> {

    @Override
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<UserBalance> findById(Long userId);

}
