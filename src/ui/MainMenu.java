package ui;

import api.HotelResource;
import exception.CustomerAlreadyExistsException;
import exception.CustomerNotFoundException;
import model.IRoom;
import model.Reservation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.lang.System.exit;

public class MainMenu {
    private static final HotelResource hotelResource = HotelResource.getInstance();

    private static void printMainMenu(String[] options) {
        for (String option : options)
            System.out.println(option);
    }
    public static void main(String[] args) {
        String[] options = {
                "1. Find and reserve a room",
                "2. See my reservations",
                "3. Create an account",
                "4. Admin",
                "5. Exit"
        };

        int option;
        Scanner scanner = new Scanner(System.in);

        while (true) {
            printMainMenu(options);
            try {
                option = scanner.nextInt();
                switch (option) {
                    case 1: findAndReserveARoom(); break;
                    case 2: seeCustomerReservations(); break;
                    case 3: createAnAccount(); break;
                    case 4: adminMenu(); break;
                    case 5: exit(0);
                    default:
                        System.out.println("Please select a valid menu (between 1 and " + options.length + ")");
                }
            } catch (InputMismatchException ex) {
                System.out.println("Exception: " + ex);
                System.out.println("Please select a valid menu (between 1 and " + options.length + ")");
                scanner.next();
            } catch (Exception ex) {
                System.out.println("An exception occurred: " + ex);
                System.out.println("Please retry...");
                scanner.nextLine();
            }
        }
    }

    private static void adminMenu() {
        AdminMenu.main(null);
    }

    private static void createAnAccount() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("First name:");
        String firstName = scanner.nextLine();
        System.out.println("Last name:");
        String lastName = scanner.nextLine();

        while (true) {
            System.out.println("Email address:");
            String email = scanner.nextLine();
            try {
                hotelResource.createACustomer(email, firstName, lastName);
                break;
            } catch (IllegalArgumentException exception) {
                System.out.println("Please enter valid email address");
            } catch (CustomerAlreadyExistsException e) {
                System.out.println("Customer with this email address exists already");
            }
        }
    }

    private static void seeCustomerReservations() {
        Scanner scanner = new Scanner(System.in);

        Collection<Reservation> customerReservations;
        while (true) {
            System.out.println("Enter email address:");
            try {
                customerReservations = hotelResource.getCustomerReservations(scanner.nextLine());
                break;
            } catch (CustomerNotFoundException e) {
                System.out.println("Customer with this email does not exist. Press 'R' to retry or 'B' to go back");
                String next = scanner.nextLine();
                if (next.equalsIgnoreCase("R"))
                    continue;
                if (next.equalsIgnoreCase("B"))
                    return;
            }
        }
        if (customerReservations.isEmpty())
            System.out.println("No reservations found!");
        else
            for (Reservation reservation : customerReservations)
                System.out.println(reservation);
    }

    private static void findAndReserveARoom() {
        Scanner scanner = new Scanner(System.in);

        String email;
        while (true) {
            System.out.println("Enter your email address:");
            email = scanner.nextLine();
            try {
                hotelResource.getCustomer(email);
                break;
            } catch (CustomerNotFoundException e) {
                System.out.println("Customer with this email does not exist");
            }
        }

        System.out.println("Check In Date (E.g.: 2022-09-15):");
        Date checkInDate = getDate();

        System.out.println("Check Out Date (E.g.: 2022-09-17):");
        Date checkOutDate = getDate();

        Collection<IRoom> availableRooms = hotelResource.findARoom(checkInDate, checkOutDate);
        if (availableRooms.isEmpty()) {
            availableRooms = hotelResource.getRecommendedRooms(checkInDate, checkOutDate);
            if (availableRooms.isEmpty()) {
                System.out.println("Sorry, no rooms available on selected days. Please try with some other dates.");
                return;
            } else {
                System.out.println("No room available on selected days. Please select a recommended room available a week later");
                checkInDate = hotelResource.getCalendar(checkInDate).getTime();
                checkOutDate = hotelResource.getCalendar(checkOutDate).getTime();
            }
        }
        for (IRoom room : availableRooms)
                System.out.println(room);

        IRoom room;
        while (true) {
            System.out.println("Select a room number from above list:");
            String roomNumber = scanner.next();
            room = hotelResource.getRoom(roomNumber);
            if (room == null || !availableRooms.contains(room)) {
                System.out.println("Enter a valid room number");
            } else
                break;
        }

        Reservation reservation;
        try {
            reservation = hotelResource.bookARoom(email, room, checkInDate, checkOutDate);
            System.out.println(reservation);
        } catch (CustomerNotFoundException e) {
            System.out.println("Customer with this email does not exist");
        }
    }

    private static Date getDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Scanner scanner = new Scanner(System.in);

        while (true) {
            String dateStr = scanner.next();
            try {
                return simpleDateFormat.parse(dateStr);
            } catch (ParseException e) {
                System.out.println("Please enter date in valid format");
            }
        }
    }
}
