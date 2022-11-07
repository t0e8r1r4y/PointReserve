package com.pointreserve.reserves.account.infra;

import com.pointreserve.reserves.account.domain.Account;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.pointreserve.reserves.account.domain.QAccount.account;

@RequiredArgsConstructor
public class AccountRespositoryImpl implements AccountRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public Optional<Account> getByMemberId(Long memberId) {
        return Optional.of(jpaQueryFactory.selectFrom(account)
                .where(account.memberId.eq(memberId))
                .fetchOne());
    }
}
