package pro.sky.telegrambot.model;

public class PetReportState {

    private PetReportStep step = PetReportStep.NAME;
    private String name;
    private String animalsDiet;
    private String animalHealth;
    private String animalHabits;
    private String photoPath;

    public PetReportStep getStep() {
        return step;
    }

    public void nextStep() {
        if (step == PetReportStep.NAME) {
            step = PetReportStep.DIET;
        } else if (step == PetReportStep.DIET) {
            step = PetReportStep.HEALTH;
        } else if (step == PetReportStep.HEALTH) {
            step = PetReportStep.HABITS;
        } else if (step == PetReportStep.HABITS) {
            step = PetReportStep.PHOTO;
        }
    }

    // Геттеры и сеттеры

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }
}

