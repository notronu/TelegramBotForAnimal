package pro.sky.telegrambot.model;




import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Volunteer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long chatId;
    private String name;

    @OneToMany(mappedBy = "volunteer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pet> pets;

    @OneToMany(mappedBy = "volunteer")
    @Fetch(FetchMode.JOIN)
    private List<PetReport> petReports = new ArrayList<>();



    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Pet> getPets() {
        return pets;
    }

    public void setPets(List<Pet> pets) {
        this.pets = pets;
    }

    public List<PetReport> getPetReports() {
        return petReports;
    }

    public void setPetReports(List<PetReport> petReports) {
        this.petReports = petReports;
    }
}
