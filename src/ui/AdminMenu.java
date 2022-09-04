package ui;

import api.AdminResource;
import api.HotelResource;
import exception.RoomAlreadyExistsException;
import model.Customer;
import model.IRoom;
import model.Room;
import model.RoomType;

import java.util.*;

public class AdminMenu {
    private static final AdminResource adminResource = AdminResource.getInstance();
    private static final HotelResource hotelResource = HotelResource.getInstance();

    public static void main(String[] args) {
        String[] options = {
                "1. See all Customers",
                "2. See all Rooms",
                "3. See all Reservations",
                "4. Add a Room",
                "5. Back to Main Menu"
        };

        Scanner scanner = new Scanner(System.in);

        while (true) {
            printAdminMenu(options);
            int option = scanner.nextInt();
            try {
                switch (option) {
                    case 1: seeAllCustomers(); break;
                    case 2: seeAllRooms(); break;
                    case 3: seeAllReservations(); break;
                    case 4: addARoom(); break;
                    case 5: MainMenu.main(null); break;
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

    private static void addARoom() {
        Scanner scanner = new Scanner(System.in);
        List<IRoom> rooms = new ArrayList<>();

        String roomNumber;
        while (true) {
            System.out.println("Room Number:");
            roomNumber = scanner.nextLine();

            if (hotelResource.getRoom(roomNumber) != null) {
                System.out.println("Room with this ID already exists");
            } else
                break;
        }

        System.out.println("Price per night:");
        double price = scanner.nextDouble();
        scanner.nextLine();

        Map<String, RoomType> roomTypes = new HashMap<>();
        roomTypes.put("1", RoomType.SINGLE);
        roomTypes.put("2", RoomType.DOUBLE);

        RoomType roomType = null;
        while (roomType == null) {
            System.out.println("Room type (1 / 2):");
            String roomTypeSelection = scanner.nextLine();
            roomType = roomTypes.get(roomTypeSelection);
        }

        IRoom room = new Room(roomNumber, price, roomType);
        rooms.add(room);
        try {
            adminResource.addRoom(rooms);
        } catch (RoomAlreadyExistsException e) {
            System.out.println("Room with this ID already exists");
        }
    }

    private static void seeAllReservations() {
        adminResource.displayAllReservations();
    }

    private static void seeAllRooms() {
        Collection<IRoom> rooms = adminResource.getAllRooms();
        for (IRoom room : rooms)
            System.out.println(room);
    }

    private static void seeAllCustomers() {
        Collection<Customer> customers = adminResource.getAllCustomer();
        for (Customer customer : customers)
            System.out.println(customer);
    }

    private static void printAdminMenu(String[] options) {
        for (String option : options)
            System.out.println(option);
    }
}
