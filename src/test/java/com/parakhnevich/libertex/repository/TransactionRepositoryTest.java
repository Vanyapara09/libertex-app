package com.parakhnevich.libertex.repository;

import java.math.BigDecimal;
import java.util.Optional;

import com.parakhnevich.libertex.constant.TransactionStatus;
import com.parakhnevich.libertex.repository.entity.AccountEntity;
import com.parakhnevich.libertex.repository.entity.TransactionEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TestEntityManager em;

    @Test
    void testSaveTransaction() {
        //given
        TransactionEntity transactionEntity = createTransactionEntity();

        //when
        Long savedTransactionId = saveAndGetFromDatabase(transactionEntity);

        //then
        TransactionEntity savedTransaction = transactionRepository.getOne(savedTransactionId);
        assertThat(savedTransaction.getId()).isNotNull();
        assertThat(savedTransaction.getAccount().getId()).isEqualTo(transactionEntity.getAccount().getId());
    }

    @Test
    void testFindById() {
        //given
        TransactionEntity transactionEntity = createTransactionEntity();
        Long savedTransactionId = saveAndGetFromDatabase(transactionEntity);

        //when
        Optional<TransactionEntity> result = transactionRepository.findById(savedTransactionId);

        //then
        assertThat(result).isPresent();
        assertThat(result.get().getAmount()).isEqualTo(transactionEntity.getAmount());
    }

    @Test
    void testUpdateStatus() {
        //given
        TransactionEntity transactionEntity = createTransactionEntity();
        transactionEntity = transactionRepository.save(transactionEntity);
        assertThat(transactionEntity.getStatus()).isEqualTo(TransactionStatus.INITIATED);

        //when
        transactionRepository.updateStatus(transactionEntity.getId(), TransactionStatus.FAILED);
        em.clear();
        //then
        TransactionEntity updatedTransaction = transactionRepository.findById(transactionEntity.getId()).get();
        assertThat(updatedTransaction.getStatus()).isEqualTo(TransactionStatus.FAILED);
    }

    private TransactionEntity createTransactionEntity() {
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setId(1L);
        accountEntity.setVersion(0);

        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setAmount(BigDecimal.valueOf(10.05));
        transactionEntity.setStatus(TransactionStatus.INITIATED);
        transactionEntity.setAccount(accountEntity);
        transactionEntity.setCurrency("USD");
        return transactionEntity;
    }

    private Long saveAndGetFromDatabase(TransactionEntity transactionEntity) {
        Long savedTransactionId = transactionRepository.saveAndFlush(transactionEntity).getId();
        em.clear();
        return savedTransactionId;
    }

}
