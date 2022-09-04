package service;

import exception.CustomerAlreadyExistsException;
import exception.CustomerNotFoundException;
import exception.RoomAlreadyExistsException;
import model.*;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import static java.lang.System.exit;

public class Driver {
    private static void testCustomerService() {
        CustomerService customerService = CustomerService.getInstance();

        // test add customer
        try {
            customerService.addCustomer("john@mail.com", "john", "man");
            System.out.println("customer added.. test PASSED");
        } catch (CustomerAlreadyExistsException e) {
            System.out.println("customer already exists which is not supposed to be..so test FAILED");
            exit(1);
        }

        // test add customer, customer exists
        try {
            customerService.addCustomer("john@mail.com", "john", "man");
            System.out.println("add customer should not have worked..test FAILED");
            exit(1);
        } catch (CustomerAlreadyExistsException e) {
            System.out.println("customer already exists exception thrown..test PASSED");
        }

        // test get customer
        try {
            customerService.getCustomer("john@mail.com");
            System.out.println("customer found.. test PASSED");
        } catch (CustomerNotFoundException e) {
            System.out.println("customer not found?? test FAILED");
            exit(1);
        }

        // test get customer, does not exist
        try {
            customerService.getCustomer("ron@mail.com");
            System.out.println("customer found.. test FAILED");
            exit(1);
        } catch (CustomerNotFoundException e) {
            System.out.println("customer not found exception thrown.. test PASSED");
        }

        // test get all customers
        Collection<Customer> allCustomers = customerService.getAllCustomers();
        System.out.println(allCustomers);
    }

    private static void testReservationService() {
        ReservationService reservationService = ReservationService.getInstance();

        IRoom room = new Room("101", 33.0, RoomType.SINGLE);
        try {
            reservationService.addRoom(room);
            System.out.println("room 101 added.. test PASSED");
        } catch (RoomAlreadyExistsException e) {
            System.out.println("room with id 101 already exists..test FAILED");
            exit(1);
        }

        try {
            reservationService.addRoom(room);
            System.out.println("room 101 added but already exists.. test FAILED");
            exit(1);
        } catch (RoomAlreadyExistsException e) {
            System.out.println("room 101 already exists.. test PASSED");
        }

        IRoom room101 = reservationService.getARoom("101");
        System.out.println("room 101: " + room101);
        if (room101 == null) {
            System.out.println("101 shows null.. test FAILED");
            exit(1);
        }
        IRoom room100 = reservationService.getARoom("100");
        System.out.println(room100);
        if (room100 == null)
            System.out.println("100 shows null.. test PASSED");
        else {
            System.out.println("100 should show null, but not. test FAILED");
            exit(1);
        }

        Date checkInDate = new Date(2022, Calendar.FEBRUARY, 2);
        System.out.println("checkin date: " + checkInDate);
        Date checkOutDate = new Date(2022, Calendar.FEBRUARY, 3);
        System.out.println("checkout date: " + checkOutDate);
        Collection<IRoom> roomsFound = reservationService.findRooms(checkInDate, checkOutDate);
        System.out.println("rooms found: " + roomsFound);

        Customer john = new Customer("john", "man", "john@mail.com");
        Reservation johnReservation = reservationService.reserveARoom(john, room, checkInDate, checkOutDate);
        System.out.println("john's one reservation: " + johnReservation);

        Collection<Reservation> johnReservations = reservationService.getCustomersReservation(john);
        System.out.println("john's all reservations: " + johnReservations);

        roomsFound = reservationService.findRooms(checkInDate, checkOutDate);
        System.out.println("rooms found after booking on same dates: " + roomsFound);

        Date newCheckInDate = new Date(2022, Calendar.FEBRUARY, 3);
        Date newCheckOutDate = new Date(2022, Calendar.FEBRUARY, 5);
        roomsFound = reservationService.findRooms(newCheckInDate, newCheckOutDate);
        System.out.println("rooms found with new checkin date equal to prev checkout date: " + roomsFound);

        Date checkInAfterCheckout = new Date(2022, Calendar.FEBRUARY, 4);
        Date checkOutAfterCheckout = new Date(2022, Calendar.FEBRUARY, 5);
        roomsFound = reservationService.findRooms(checkInAfterCheckout, checkOutAfterCheckout);
        System.out.println("rooms found with new checkin after prev checkout date: " + roomsFound);

        IRoom room102 = new Room("102", 45.0, RoomType.DOUBLE);
        try {
            reservationService.addRoom(room102);
            System.out.println("room 102 added.. test PASSED");
        } catch (RoomAlreadyExistsException e) {
            System.out.println("room already exists.. test FAILED");
            exit(1);
        }

        roomsFound = reservationService.findRooms(checkInDate, checkOutDate);
        System.out.println("rooms found for old checkin and checkout dates after new room added: " + roomsFound);

        Customer david = new Customer("david", "han", "david@mail.com");
        Reservation davidReservation = reservationService.reserveARoom(david, room, checkInDate, checkOutDate);
        System.out.println("david's one reservations: " + davidReservation);

        System.out.print("all reservations: ");
        reservationService.printAllReservation();

        Collection<IRoom> allRooms = reservationService.getAllRooms();
        System.out.println("all rooms: " + allRooms);
    }

    public static void main(String[] args) {
        testCustomerService();
        testReservationService();
    }



}
