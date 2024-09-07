package pro.sky.telegrambot.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pro.sky.telegrambot.model.Client;
@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    @Query("select c from Client c where c.reportCount >= 0 and c.reportCount<3")
    List<Client> findAllClientsWithPositiveReportCounts();

    static Client findByChatId(Long chatId) {
        return null;
    }


    // Метод для проверки, существует ли клиент с данным chatId
    boolean existsByChatId(Long chatId);
}
