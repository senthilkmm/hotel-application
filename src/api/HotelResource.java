package api;

import exception.CustomerAlreadyExistsException;
import exception.CustomerNotFoundException;
import model.Customer;
import model.IRoom;
import model.Reservation;
import service.CustomerService;
import service.ReservationService;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

public class HotelResource {
    private static HotelResource hotelResourceSingleton;
    private final CustomerService customerService = CustomerService.getInstance();
    private final ReservationService reservationService = ReservationService.getInstance();

    private HotelResource() {
    }

    public static HotelResource getInstance() {
        if (hotelResourceSingleton == null)
            hotelResourceSingleton = new HotelResource();
        return hotelResourceSingleton;
    }

    public Customer getCustomer(String email) throws CustomerNotFoundException {
        return customerService.getCustomer(email);
    }

    public void createACustomer(String email, String firstName, String lastName) throws CustomerAlreadyExistsException {
        customerService.addCustomer(email, firstName, lastName);
    }

    public IRoom getRoom(String roomNumber) {
        return reservationService.getARoom(roomNumber);
    }

    public Reservation bookARoom(String customerEmail, IRoom room, Date checkInDate, Date checkOutDate) throws CustomerNotFoundException {
        Customer customer = getCustomer(customerEmail);
        return reservationService.reserveARoom(customer, room, checkInDate, checkOutDate);
    }

    public Collection<Reservation> getCustomerReservations(String customerEmail) throws CustomerNotFoundException {
        Customer customer = getCustomer(customerEmail);
        return reservationService.getCustomersReservation(customer);
    }

    public Collection<IRoom> findARoom(Date checkIn, Date checkOut) {
        return reservationService.findRooms(checkIn, checkOut);
    }

    public Collection<IRoom> getRecommendedRooms(Date checkInDate, Date checkOutDate) {
        Calendar checkInCal = getCalendar(checkInDate);
        Calendar checkOutCal = getCalendar(checkOutDate);
        return findARoom(checkInCal.getTime(), checkOutCal.getTime());
    }

    public Calendar getCalendar(Date checkInDate) {
        Calendar checkInCal = Calendar.getInstance();
        checkInCal.setTime(checkInDate);
        checkInCal.add(Calendar.DAY_OF_MONTH, 7);
        return checkInCal;
    }
}
