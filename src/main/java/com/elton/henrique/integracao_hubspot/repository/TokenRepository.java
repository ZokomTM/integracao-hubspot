package com.elton.henrique.integracao_hubspot.repository;


import com.elton.henrique.integracao_hubspot.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    Token findFirstByOrderByIdDesc();
}
