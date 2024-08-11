package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.telegrambot.model.Client;

/**
 * Репозиторий для работы с клиентами
 */
public interface ClientRepository extends JpaRepository<Client, Long> {
}
