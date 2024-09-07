package pro.sky.telegrambot.model;

public class PetSession {
    private int id;
    private String name;
    private String breed;
    private String photoPath;

    public PetSession(int id, String name, String breed, String photoPath) {
        this.id = id;
        this.name = name;
        this.breed = breed;
        this.photoPath = photoPath;
    }

    // Геттеры и сеттеры

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }
}
