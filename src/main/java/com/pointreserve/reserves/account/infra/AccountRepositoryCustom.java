package com.pointreserve.reserves.account.infra;

import com.pointreserve.reserves.account.domain.Account;
import org.springframework.data.jpa.repository.Lock;

import javax.persistence.LockModeType;
import java.util.Optional;

public interface AccountRepositoryCustom {
    Optional<Account> getByMemberId(Long memberId);
}
