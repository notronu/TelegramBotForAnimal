package pro.sky.telegrambot.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pro.sky.telegrambot.model.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {

    @Query("select c from Client c where c.reportCount >= 0 and c.reportCount<3")
    List<Client> findAllClientsWithPositiveReportCounts();

    Client findByChatId(Long chatId);
    // Метод для проверки, существует ли клиент с данным chatId
    boolean existsByChatId(Long chatId);
}
