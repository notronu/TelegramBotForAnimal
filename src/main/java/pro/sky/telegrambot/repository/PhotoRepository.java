package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pro.sky.telegrambot.model.Photo;

import java.util.List;

public interface PhotoRepository extends JpaRepository<Photo, Long> {

    @Query(value = "SELECT * FROM photo WHERE sent_time < CURRENT_DATE", nativeQuery = true)
    List<Photo> findPhotoByDate();

    @Query(value = "SELECT * FROM photo WHERE sent_time = CURRENT_DATE", nativeQuery = true)
    List<Photo> findPhotoToday();
}
