package pro.sky.telegrambot.model;

import org.springframework.stereotype.Service;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;


@Entity
@Table(name = "pet_report")
@Service
public class PetReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String animalsDiet;
    private String animalHealth;
    private String animalHabits;
    private String photoFileId;
    private LocalDateTime data;


    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany
    @JoinTable(name = "volunteer_pet_report",
            joinColumns = @JoinColumn(name = "pet_report_id"),
            inverseJoinColumns = @JoinColumn(name = "volunteer_id"))
    private Set<Volunteer> volunteers = new HashSet<>();


    @Enumerated(EnumType.STRING)
    private ApprovalStatus approvalStatus = ApprovalStatus.PENDING;


    public PetReport() {
    }


    public String getPhotoFileId() {
        return photoFileId;
    }

    public void setPhotoFileId(String photoFileId) {
        this.photoFileId = photoFileId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }



    public String getAnimalsDiet() {
        return animalsDiet;
    }

    public void setAnimalsDiet(String animalsDiet) {
        this.animalsDiet = animalsDiet;
    }

    public String getAnimalHealth() {
        return animalHealth;
    }

    public void setAnimalHealth(String animalHealth) {
        this.animalHealth = animalHealth;
    }

    public String getAnimalHabits() {
        return animalHabits;
    }

    public void setAnimalHabits(String animalHabits) {
        this.animalHabits = animalHabits;
    }


    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public ApprovalStatus getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(ApprovalStatus approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public Set<Volunteer> getVolunteers() {
        return volunteers;
    }

    public void setVolunteers(Set<Volunteer> volunteers) {
        this.volunteers = volunteers;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PetReport petReport = (PetReport) o;
        return id == petReport.id && Objects.equals(name, petReport.name) && Objects.equals(animalsDiet, petReport.animalsDiet) && Objects.equals(animalHealth, petReport.animalHealth) && Objects.equals(animalHabits, petReport.animalHabits) && Objects.equals(photoFileId, petReport.photoFileId) && Objects.equals(data, petReport.data) && Objects.equals(user, petReport.user) && Objects.equals(volunteers, petReport.volunteers) && approvalStatus == petReport.approvalStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, animalsDiet, animalHealth, animalHabits, photoFileId, data, user, volunteers, approvalStatus);
    }

    @Override
    public String toString() {
        return "PetReport{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", animalsDiet='" + animalsDiet + '\'' +
                ", animalHealth='" + animalHealth + '\'' +
                ", animalHabits='" + animalHabits + '\'' +
                ", photoFileId='" + photoFileId + '\'' +
                ", data=" + data +
                ", user=" + user +
                ", volunteers=" + volunteers +
                ", approvalStatus=" + approvalStatus +
                '}';
    }
}