package model;


import error.ErrorMessage;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;


public class Person implements Serializable {
    private static final long serialVersionUID = 1L;
    private String firstName;
    private String patronymicName;
    private String lastName;
    private Date dateOfBirth;
    
    
    public Person() {
    }
    
    
    public Person(String firstName, String patronymicName, String lastName, Date dateOfBirth) {
        setFirstName(firstName);
        setPatronymicName(patronymicName);
        setLastName(lastName);
        setDateOfBirth(dateOfBirth);
    }
    
    
    public Person(String firstName, String lastName, Date dateOfBirth) {
        this(firstName, null, lastName, dateOfBirth);
    }
    
    
    public void setFirstName(String firstName) {
        if (firstName == null || firstName.length() == 0)
            throw new IllegalArgumentException(ErrorMessage.NULL_FIRST_NAME);
        
        this.firstName = firstName;
    }
    
    
    public void setPatronymicName(String patronymicName) {
        this.patronymicName = patronymicName;
    }
    
    
    public void setLastName(String lastName) {
        if (lastName == null || lastName.length() == 0)
            throw new IllegalArgumentException(ErrorMessage.NULL_LAST_NAME);
        
        this.lastName = lastName;
    }
    
    
    public void setDateOfBirth(Date dateOfBirth) {
        if (dateOfBirth == null)
            throw new IllegalArgumentException(ErrorMessage.NULL_DATE_OF_BIRTH);
        
        this.dateOfBirth = dateOfBirth;
    }
    
    
    public String getFirstName() {
        return firstName;
    }
    
    
    public String getPatronymicName() {
        return patronymicName;
    }
    
    
    public String getLastName() {
        return lastName;
    }
    
    
    public Date getDateOfBirth() {
        return dateOfBirth;
    }
    
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(firstName, person.firstName) &&
                Objects.equals(patronymicName, person.patronymicName) &&
                Objects.equals(lastName, person.lastName) &&
                Objects.equals(dateOfBirth, person.dateOfBirth);
    }
    
    
    @Override
    public int hashCode() {
        return Objects.hash(firstName, patronymicName, lastName, dateOfBirth);
    }
}
