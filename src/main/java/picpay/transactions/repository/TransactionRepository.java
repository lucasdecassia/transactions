package picpay.transactions.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import picpay.transactions.model.Transaction;
import picpay.transactions.model.User;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByPayer(User payer);

    List<Transaction> findByPayee(User payee);
}