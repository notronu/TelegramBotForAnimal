package pro.sky.telegrambot.model;

public class PetRegistrationState {
    private PetRegistrationStep step = PetRegistrationStep.NAME;
    private String name;
    private String breed;
    private String photoPath;

    public PetRegistrationStep getStep() {
        return step;
    }

    public void nextStep() {
        if (step == PetRegistrationStep.NAME) {
            step = PetRegistrationStep.BREED;
        } else if (step == PetRegistrationStep.BREED) {
            step = PetRegistrationStep.PHOTO;
        }
    }

    // Геттеры и сеттеры

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
