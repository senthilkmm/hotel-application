package service;

import exception.CustomerAlreadyExistsException;
import exception.CustomerNotFoundException;
import model.Customer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CustomerService {
    private static CustomerService customerServiceSingleton;
    private final Map<String,Customer> customers = new HashMap<>();

    private CustomerService() {
    }

    public static CustomerService getInstance() {
        if (customerServiceSingleton == null)
            customerServiceSingleton = new CustomerService();
        return customerServiceSingleton;
    }

    public void addCustomer(String email, String firstName, String lastName) throws CustomerAlreadyExistsException {
        Customer customer = new Customer(firstName, lastName, email);
        if (customers.containsKey(email))
            throw new CustomerAlreadyExistsException();
        customers.put(email, customer);
    }

    public Customer getCustomer(String customerEmail) throws CustomerNotFoundException {
        if (!customers.containsKey(customerEmail))
            throw new CustomerNotFoundException();
        return customers.get(customerEmail);
    }

    public Collection<Customer> getAllCustomers() {
        return customers.values();
    }
}
