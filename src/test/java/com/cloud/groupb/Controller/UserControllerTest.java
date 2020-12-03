package com.cloud.groupb.Controller;

import com.cloud.groupb.Service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.cloud.groupb.Entity.User;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    private UserController controller;

    @Test
    @DisplayName("should return users with terms")
    void getUsersByTermsTest(){
        UserService service = Mockito.mock(UserService.class);
        controller = new UserController(service);
        List<User> tab = new ArrayList<>();
        tab.add(new User());
        Mockito.when(service.getUsersBySearch(1,"issou")).thenReturn(tab);
        controller.getUsersByTerm(1,"issou");
        Mockito.verify(service, Mockito.times(1)).getUsersBySearch(1,"issou");
        assertEquals(tab, controller.getUsersByTerm(1,"issou"));
    }

    @Test
    @DisplayName("should return users near the lat lon")
    void getUsersByLatLonTest(){
        UserService service = Mockito.mock(UserService.class);
        controller = new UserController(service);
        List<User> tab = new ArrayList<>();
        tab.add(new User());
        Mockito.when(service.getUsersByNearest(1,45,59)).thenReturn(tab);
        controller.getUsersByLatLon(1,45,59);
        Mockito.verify(service, Mockito.times(1)).getUsersByNearest(1,45,59);
        assertEquals(tab, controller.getUsersByLatLon(1,45,59));
    }

    @Test
    @DisplayName("should return users near the lat lon")
    void getUsersByIdTest(){
        UserService service = Mockito.mock(UserService.class);
        controller = new UserController(service);
        User user = new User();
        Mockito.when(service.getUserById("1")).thenReturn(user);
        controller.getUserById("1");
        Mockito.verify(service, Mockito.times(1)).getUserById("1");
        assertEquals(user, controller.getUserById("1"));
    }

    @Test
    @DisplayName("should return users")
    void getUsersTest(){
        UserService service = Mockito.mock(UserService.class);
        controller = new UserController(service);
        List<User> tab = new ArrayList<>();
        tab.add(new User());
        Mockito.when(service.getUsers(1)).thenReturn(tab);
        controller.getUsers(1);
        Mockito.verify(service, Mockito.times(1)).getUsers(1);
        assertEquals(tab, controller.getUsers(1));
    }

    @Test
    @DisplayName("should post one user")
    void postUsertest(){
        UserService service = Mockito.mock(UserService.class);
        controller = new UserController(service);
        User user = new User();
        controller.postUser(user);
        Mockito.verify(service, Mockito.times(1)).postUser(user);
    }

    @Test
    @DisplayName("Should replace users")
    void putUserTest(){
        UserService service = Mockito.mock(UserService.class);
        controller = new UserController(service);
        List<User> tab = new ArrayList<User>();
        tab.add(new User());
        controller.putUsers(tab);
        Mockito.verify(service, Mockito.times(1)).putUsers(tab);
    }

    @Test
    @DisplayName("Should replace one user")
    void putUserByIdTest(){
        UserService service = Mockito.mock(UserService.class);
        controller = new UserController(service);
        User user = new User();
        Mockito.when(service.putUserById(1,user)).thenReturn(user);
        controller.putUserById(1,user);
        Mockito.verify(service, Mockito.times(1)).putUserById(1,user);
        assertEquals(user, controller.putUserById(1,user));
    }

    @Test
    @DisplayName("should get users by age")
    void getUsersByAgeTest(){
        UserService service = Mockito.mock(UserService.class);
        controller = new UserController(service);
        ArrayList <User> users = new ArrayList<>();
        users.add(Mockito.mock(User.class));
        Mockito.when(service.getUsersByAge(0,1,1)).thenReturn(users);
        controller.getUsersByAge(0,1,1);
        Mockito.verify(service,Mockito.times(1)).getUsersByAge(0,1,1);
        assertEquals(users, controller.getUsersByAge(0,1,1));
    }
    @Test
    @DisplayName("Should delete all users")
    void deleteUsersTest(){
        UserService service = Mockito.mock(UserService.class);
        controller = new UserController(service);
        controller.deleteUsers();
        Mockito.verify(service,Mockito.times(1)).deleteUsers();
    }
    @Test
    @DisplayName("Should delete a specific user")
    void deleteUserByIdTest(){
        UserService service = Mockito.mock(UserService.class);
        controller = new UserController(service);
        controller.deleteUserById("1");
        Mockito.verify(service,Mockito.times(1)).deleteUserById("1");
    }
}