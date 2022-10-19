package network.dto;

import java.io.Serializable;


public class SellerDTO implements Serializable{
    private String id;
    private String firstName;
    private String lastName;
    private String username;
    private String password;

//    public SellerDTO(String username) {
//        this(username,"");
//    }
//
//    public SellerDTO(String username, String passwd) {
//        this.username = username;
//        this.password = passwd;
//    }

    public SellerDTO(String id, String firstName, String lastName, String username, String password) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public String getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }

    public void setUsername(String username) {
        this.username = username;
    }
    public void setId(String id) { this.id = id; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastNAme(String lastNAme) { this.lastName = lastNAme; }
    public void setPassword(String password) { this.password = password; }

    @Override
    public String toString(){
        return "SellerDTO["+username+";"+password+"]";
    }
}
