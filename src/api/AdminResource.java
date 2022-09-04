package api;

import exception.CustomerNotFoundException;
import exception.RoomAlreadyExistsException;
import model.Customer;
import model.IRoom;
import service.CustomerService;
import service.ReservationService;

import java.util.Collection;
import java.util.List;

public class AdminResource {
    private static AdminResource adminResourceSingleton;
    private final CustomerService customerService = CustomerService.getInstance();
    private final ReservationService reservationService = ReservationService.getInstance();

    private AdminResource() {
    }

    public static AdminResource getInstance() {
        if (adminResourceSingleton == null)
            adminResourceSingleton = new AdminResource();
        return adminResourceSingleton;
    }

    public Customer getCustomer(String email) throws CustomerNotFoundException {
        return customerService.getCustomer(email);
    }

    public void addRoom(List<IRoom> rooms) throws RoomAlreadyExistsException {
        for (IRoom room : rooms)
            reservationService.addRoom(room);
    }

    public Collection<IRoom> getAllRooms() {
        return reservationService.getAllRooms();
    }

    public Collection<Customer> getAllCustomer() {
        return customerService.getAllCustomers();
    }

    public void displayAllReservations() {
        reservationService.printAllReservation();
    }
}
