package org.oscm.app.resource;

public class Customer {

    private String firstName = null;
    private String lastName = null;

    public String getCustomerName() {
        String returnValue = null;
        if ((this.firstName != null) && (this.lastName != null)) {
            returnValue = this.firstName + " " + this.lastName;
        } else if (this.firstName != null) returnValue = this.firstName + " N/A";
        else if (this.lastName != null) returnValue = "N/A " + this.lastName;
        else returnValue = "N/A " + "N/A";
        return returnValue;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
