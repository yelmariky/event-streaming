package fr.nextdigital.lab.account.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import fr.nextdigital.lab.account.web.domain.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findAccountByEmail(@Param("email") String email);
}
