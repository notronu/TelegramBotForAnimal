package pro.sky.telegrambot.model;

public class PetReportSessions {

    private int id;
    private String name;
    private String diet;
    private String health;
    private String habits;
    private String photoPath;

    public PetReportSessions(int id, String name, String diet, String health, String habits, String photoPath) {
        this.id = id;
        this.name = name;
        this.diet = diet;
        this.health = health;
        this.habits = habits;
        this.photoPath = photoPath;
    }

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

    public String getDiet() {
        return diet;
    }

    public void setDiet(String diet) {
        this.diet = diet;
    }

    public String getHealth() {
        return health;
    }

    public void setHealth(String health) {
        this.health = health;
    }

    public String getHabits() {
        return habits;
    }

    public void setHabits(String habits) {
        this.habits = habits;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }
}
