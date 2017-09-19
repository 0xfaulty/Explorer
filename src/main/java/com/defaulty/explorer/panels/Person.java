package com.defaulty.explorer.panels;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.Image;

public class Person {

    private final SimpleStringProperty firstName;
    private final SimpleStringProperty lastName;
    private final SimpleStringProperty email;
    private final SimpleStringProperty likes;

    public Person(String fName, String lName, String email, String likes) {
        this.firstName = new SimpleStringProperty(fName);
        this.lastName = new SimpleStringProperty(lName);
        this.email = new SimpleStringProperty(email);
        this.likes = new SimpleStringProperty(likes);
    }

    public String getFirstName() {
        return firstName.get();
    }

    public void setFirstName(String fName) {
        firstName.set(fName);
    }

    public String getLastName() {
        return lastName.get();
    }

    public void setLastName(String fName) {
        lastName.set(fName);
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String fName) {
        email.set(fName);
    }

    public String getLikes() {
        return likes.get();
    }

    public void setLikes(String likes) {
        this.likes.set(likes);
    }

}