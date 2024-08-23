package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.telegrambot.model.PetReport;


import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<PetReport, Long> {

    List<PetReport> findNotificationTasksByTaskDate(LocalDateTime time);
}
