package org.oscm.app.resource;

import junit.framework.TestCase;
import org.junit.Test;
import org.oscm.app.resource.Customer;

public class CustomerTest extends TestCase {

    @Test
    public void testGetCustomerName() {
        Customer customer1 = new Customer();
        customer1.setFirstName("John");
        customer1.setLastName("Doe");
        assertEquals("John Doe", customer1.getCustomerName());

        Customer customer2 = new Customer();
        customer2.setFirstName(null);
        customer2.setLastName(null);
        assertEquals("N/A N/A", customer2.getCustomerName());

        Customer customer3 = new Customer();
        customer3.setFirstName("John");
        customer3.setLastName(null);
        assertEquals("John N/A", customer3.getCustomerName());

        Customer customer4 = new Customer();
        customer4.setFirstName(null);
        customer4.setLastName("Doe");
        assertEquals("N/A Doe", customer4.getCustomerName());
    }
}
