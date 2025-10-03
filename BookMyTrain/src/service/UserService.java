package service;

import Entity.User;

import java.util.HashMap;
import java.util.Map;

// To be managed by user for logging and functionality we use THIS
public class UserService {
    // we have to keep mapping of user with his details
    // so we gonna use map   (userName -----> user)
    private Map<String,User> userMap=new HashMap<>();
    private User currentUser = null;

    public boolean registerUser(String username,String password,String fullName,String contact)
    {
        if(userMap.containsKey(username))
        {
            System.out.println("Username already taken, Please choose another");
            return false;
        }
        User user=new User(username,password,fullName,contact);
        userMap.put(username,user);
        System.out.println("Registration Successful!");
        return true;
    }

    public boolean loginUser(String username,String password)
    {
        if(!userMap.containsKey(username))
        {
            System.out.println("No User Found with this username");
            return false;
        }

        User user=userMap.get(username);   // extract user from map
        if(!user.getPassword().equals(password))
        {
            System.out.println("Incorrect Password.");
            return false;
        }

        currentUser=user;   // means current user is logged in
        System.out.println("Welcome : "+currentUser.getFullName() + "!");
        return true;

    }

    public void logoutUser()
    {
        if(currentUser!=null)
        {
            System.out.println("Logged Out "+currentUser.getFullName());
        }
        // set the current user to null means we have logged out again one wants then login
        currentUser=null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean isLoggedIn()
    {
        return currentUser!=null;
    }
}