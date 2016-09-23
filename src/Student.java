/**
 * Created by Leonardo on 9/19/2016.
 */
public class Student
        extends User{

    private  Address address;

    public Student(String firstName, String lastName, String email, String phone, String uuid){
        super( firstName, lastName, email, phone, uuid);
    }

    public void setAddress(Address address){ this.address = address; }
    public Address getAddress(){ return  this.address;}

    public void Enroll(){

    }

    public void Disenroll(){

    }
}
