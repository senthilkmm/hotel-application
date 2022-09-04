package model;

import java.util.Calendar;
import java.util.Date;

public class Driver {
    public static void main(String[] args) {
        // test Customer class
        Customer customer = new Customer("Mike", "Bane", "mike@gmail.com");
        System.out.println(customer);
        try {
            new Customer("first", "second", "invalid");
        } catch (IllegalArgumentException ex) {
            System.out.println("Exception is caught: " + ex);
        }

        // test Room class
        // single room
        IRoom room = new Room("123", 35.0, RoomType.SINGLE);
        System.out.println(room);
        System.out.println("Is this room for free? " + room.isFree());

        // double room
        IRoom doubleRoom = new Room("124", 60.0, RoomType.DOUBLE);
        System.out.println(doubleRoom);
        System.out.println("Is this double room for free? " + doubleRoom.isFree());

        // free room
        IRoom freeRoom = new FreeRoom("300", RoomType.SINGLE);
        System.out.println(freeRoom);
        System.out.println("Is this room for free? " + freeRoom.isFree());

        // test Reservation class
        Reservation reservation = new Reservation(customer,
                room,
                new Date(2022, Calendar.FEBRUARY, 2),
                new Date(2022, Calendar.FEBRUARY, 3));
        System.out.println(reservation);
    }
}
