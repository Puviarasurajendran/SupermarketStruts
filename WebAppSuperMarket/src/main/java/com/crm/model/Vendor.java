package com.crm.model;

public class Vendor {
    private int vendorId;
    private String name;
    private String contactName;
    private String contactPhone;
    private String address;
    private String vendor_email;

    public Vendor() {}

    public int getVendorId() {
        return vendorId;
    }

    public void setVendorId(int vendorId) {
        this.vendorId = vendorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }



	public String getVendor_email() {
		return vendor_email;
	}

	public void setVendor_email(String vendor_email) {
		this.vendor_email = vendor_email;
	}

	@Override
	public String toString() {
		return "Vendor [vendorId=" + vendorId + ", name=" + name + ", contactName=" + contactName + ", contactPhone="
				+ contactPhone + ", address=" + address + ", vendor_email=" + vendor_email + "]";
	}

}

