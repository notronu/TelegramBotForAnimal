package pro.sky.telegrambot.model;

/**
 * Класс, представляющий приют.
 */
public class Shelter {

    private String name;
    private String address;
    private AnimalType type;

    // Конструкторы, геттеры и сеттеры

    public Shelter(String name, String address, AnimalType type) {
        this.name = name;
        this.address = address;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public AnimalType getType() {
        return type;
    }

    public void setType(AnimalType type) {
        this.type = type;
    }
}
