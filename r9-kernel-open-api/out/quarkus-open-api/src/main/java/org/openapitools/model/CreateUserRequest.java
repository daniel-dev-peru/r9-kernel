package org.openapitools.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.List;

//import org.openapitools.jackson.nullable.JsonNullable;


import javax.validation.Valid;
import javax.validation.constraints.*;







/**
 * create user request object
 */

@javax.annotation.Generated(value = "org.raise9.kernel.open.api.QuarkusOpenApiGenerator", date = "2022-03-31T19:00:46.384006100-05:00[America/Bogota]")
public class CreateUserRequest   {
  @JsonProperty("firstName")
  private String firstName;

  @JsonProperty("lastName")
  private String lastName;

  @JsonProperty("room")
  private String room;

  @JsonProperty("email")
  private String email;

  @JsonProperty("phoneNumber")
  private String phoneNumber;

  @JsonProperty("cmp")
  private String cmp;

  @JsonProperty("speciality")
  @Valid
  private List<String> speciality = null;

  @JsonProperty("password")
  private String password;

  public CreateUserRequest firstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

  /**
   * firstName field
   * @return firstName
  */

  @NotNull


  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public CreateUserRequest lastName(String lastName) {
    this.lastName = lastName;
    return this;
  }

  /**
   * lastName field
   * @return lastName
  */

  @NotNull


  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public CreateUserRequest room(String room) {
    this.room = room;
    return this;
  }

  /**
   * room field
   * @return room
  */



  public String getRoom() {
    return room;
  }

  public void setRoom(String room) {
    this.room = room;
  }

  public CreateUserRequest email(String email) {
    this.email = email;
    return this;
  }

  /**
   * email field
   * @return email
  */

  @NotNull

@javax.validation.constraints.Email
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public CreateUserRequest phoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
    return this;
  }

  /**
   * phoneNumber field
   * @return phoneNumber
  */

  @NotNull


  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public CreateUserRequest cmp(String cmp) {
    this.cmp = cmp;
    return this;
  }

  /**
   * cmp field
   * @return cmp
  */

  @NotNull


  public String getCmp() {
    return cmp;
  }

  public void setCmp(String cmp) {
    this.cmp = cmp;
  }

  public CreateUserRequest speciality(List<String> speciality) {
    this.speciality = speciality;
    return this;
  }

  public CreateUserRequest addSpecialityItem(String specialityItem) {
    if (this.speciality == null) {
      this.speciality = new ArrayList<>();
    }
    this.speciality.add(specialityItem);
    return this;
  }

  /**
   * speciality field
   * @return speciality
  */



  public List<String> getSpeciality() {
    return speciality;
  }

  public void setSpeciality(List<String> speciality) {
    this.speciality = speciality;
  }

  public CreateUserRequest password(String password) {
    this.password = password;
    return this;
  }

  /**
   * password field
   * @return password
  */

  @NotNull


  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreateUserRequest createUserRequest = (CreateUserRequest) o;
    return Objects.equals(this.firstName, createUserRequest.firstName) &&
        Objects.equals(this.lastName, createUserRequest.lastName) &&
        Objects.equals(this.room, createUserRequest.room) &&
        Objects.equals(this.email, createUserRequest.email) &&
        Objects.equals(this.phoneNumber, createUserRequest.phoneNumber) &&
        Objects.equals(this.cmp, createUserRequest.cmp) &&
        Objects.equals(this.speciality, createUserRequest.speciality) &&
        Objects.equals(this.password, createUserRequest.password);
  }

  @Override
  public int hashCode() {
    return Objects.hash(firstName, lastName, room, email, phoneNumber, cmp, speciality, password);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreateUserRequest {\n");
    
    sb.append("    firstName: ").append(toIndentedString(firstName)).append("\n");
    sb.append("    lastName: ").append(toIndentedString(lastName)).append("\n");
    sb.append("    room: ").append(toIndentedString(room)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    phoneNumber: ").append(toIndentedString(phoneNumber)).append("\n");
    sb.append("    cmp: ").append(toIndentedString(cmp)).append("\n");
    sb.append("    speciality: ").append(toIndentedString(speciality)).append("\n");
    sb.append("    password: ").append(toIndentedString(password)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

