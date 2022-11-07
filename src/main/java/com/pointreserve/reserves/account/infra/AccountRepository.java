package com.pointreserve.reserves.account.infra;

import com.pointreserve.reserves.account.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long>, AccountRepositoryCustom {
}
