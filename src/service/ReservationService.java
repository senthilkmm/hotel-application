package service;

import exception.RoomAlreadyExistsException;
import model.Customer;
import model.IRoom;
import model.Reservation;

import java.util.*;

public class ReservationService {
    private static ReservationService reservationServiceSingleton;
    private static final Collection<Reservation> reservations = new LinkedList<>();
    private static final Collection<IRoom> rooms = new HashSet<>();

    private ReservationService() {
    }

    public static ReservationService getInstance() {
        if (reservationServiceSingleton == null)
            reservationServiceSingleton = new ReservationService();
        return reservationServiceSingleton;
    }

    public void addRoom(IRoom room) throws RoomAlreadyExistsException {
        if (rooms.contains(room))
            throw new RoomAlreadyExistsException();
        rooms.add(room);
    }

    public IRoom getARoom(String roomId) {
        return rooms
                .stream()
                .filter(room -> room.getRoomNumber().equalsIgnoreCase(roomId))
                .findFirst()
                .orElse(null);
    }

    public Reservation reserveARoom(Customer customer, IRoom room, Date checkInDate, Date checkOutDate) {
        Reservation reservation = new Reservation(customer, room, checkInDate, checkOutDate);
        reservations.add(reservation);
        return reservation;
    }

    public Collection<IRoom> findRooms(Date checkInDate, Date checkOutDate) {
        Collection<IRoom> availableRooms = new ArrayList<>();
        if (reservations.isEmpty())
            availableRooms.addAll(rooms);
        else {
            processAllRooms(checkInDate, checkOutDate, availableRooms);
        }
        return availableRooms;
    }

    void processAllRooms(Date checkInDate, Date checkOutDate, Collection<IRoom> availableRooms) {
        Collection<IRoom> bookedRooms = new ArrayList<>();
        for (IRoom room : rooms) {
            for (Reservation reservation : reservations) {
                processBookedRooms(checkInDate, checkOutDate, availableRooms, bookedRooms, room, reservation);
            }
            if (!bookedRooms.contains(room) && !availableRooms.contains(room))
                availableRooms.add(room);
        }
    }

    private void processBookedRooms(Date checkInDate, Date checkOutDate, Collection<IRoom> availableRooms, Collection<IRoom> bookedRooms, IRoom room, Reservation reservation) {
        if (reservation.getRoom() == room
                && checkOutDate.before(reservation.getCheckInDate())
                || checkInDate.after(reservation.getCheckOutDate()))
                availableRooms.add(reservation.getRoom());
         else if (reservation.getRoom() == room)
            bookedRooms.add(room);
    }

    public Collection<Reservation> getCustomersReservation(Customer customer) {
        Collection<Reservation> customerReservations = new LinkedList<>();
        for (Reservation reservation : reservations)
            if (reservation.getCustomer() == customer)
                customerReservations.add(reservation);
        return customerReservations;
    }

    public void printAllReservation() {
        for (Reservation reservation : reservations)
            System.out.println(reservation);
    }

    public Collection<IRoom> getAllRooms() {
        return rooms;
    }
}
