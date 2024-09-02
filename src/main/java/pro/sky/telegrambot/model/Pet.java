package pro.sky.telegrambot.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Objects;
@Entity
@Table(name = "pets")
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pet_id")
    private long petId;

    //@Column(nullable = false)
    //@Column(name = "name")
    private String name;
    //@Column(nullable = false)
    //@Column(name = "breed")
    private String breed;
    //@Column(nullable = false)
    //@Column(name = "age")
    private int age;
    //@Column(nullable = false)
    //@Column(name = "food")
    private String food;
    //@Column
    //@Column(name = "shelter")
    private String shelter;
    @Column(name = "user_id", insertable = false, updatable = false)
    private Long userId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    @JsonIgnore
    private User user;

    public Pet(long petId, String name, String breed, int age, String food, String shelter, Long userId) {
        this.petId = petId;
        this.name = name;
        this.breed = breed;
        this.age = age;
        this.food = food;
        this.shelter = shelter;
        this.userId = userId;
    }

    public Pet() {

    }

    public long getId() {
        return petId;
    }

    public void setId(long id) {
        this.petId = petId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public String getShelter() {
        return shelter;
    }

    public void setShelter(String shelter) {
        this.shelter = shelter;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pet pet = (Pet) o;
        return petId == pet.petId && age == pet.age && userId == pet.userId && Objects.equals(name, pet.name) && Objects.equals(breed, pet.breed) && Objects.equals(food, pet.food) && Objects.equals(shelter, pet.shelter) && Objects.equals(user, pet.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(petId, name, breed, age, food, shelter, userId, user);
    }

    @Override
    public String toString() {
        return "Pet{" +
                "petId=" + petId +
                ", name='" + name + '\'' +
                ", breed='" + breed + '\'' +
                ", age=" + age +
                ", food='" + food + '\'' +
                ", shelter='" + shelter + '\'' +
                ", userId=" + userId +
                ", user=" + user +
                '}';
    }

}

