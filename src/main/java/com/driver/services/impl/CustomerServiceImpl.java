package com.driver.services.impl;

import com.driver.model.*;
import com.driver.model.TripBooking;
import com.driver.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.driver.model.Customer;
import com.driver.model.Driver;
import com.driver.repository.CustomerRepository;
import com.driver.repository.DriverRepository;
import com.driver.repository.TripBookingRepository;
import com.driver.model.TripStatus;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	CustomerRepository customerRepository2;

	@Autowired
	DriverRepository driverRepository2;

	@Autowired
	TripBookingRepository tripBookingRepository2;

	@Override
	public void register(Customer customer) {
		//Save the customer in database
		customerRepository2.save(customer);
	}

	@Override
	public void deleteCustomer(Integer customerId) {
		// Delete customer without using deleteById function
		customerRepository2.deleteById(customerId);

	}

	@Override
	public TripBooking bookTrip(int customerId, String fromLocation, String toLocation, int distanceInKm) throws Exception{
		//Book the driver with lowest driverId who is free (cab available variable is Boolean.TRUE). If no driver is available, throw "No cab available!" exception
		//Avoid using SQL query
		List<Driver> driverList= driverRepository2.findAll();
		int firstDriverId = Integer.MAX_VALUE;

		for(Driver driver : driverList){
			if(driver!= null && driver.getDriverId() < firstDriverId){
				firstDriverId= driver.getDriverId();
			}
		}

		if(firstDriverId == Integer.MAX_VALUE) throw new Exception("No cab available!");

				//else Creating a new trip and setting its values
				Driver driver= driverRepository2.findById(firstDriverId).get();
				TripBooking newTrip= new TripBooking();
				newTrip.setCustomer(customerRepository2.findById(customerId).get());
				newTrip.setDriver(driver);
				newTrip.setFromLocation(fromLocation);
				newTrip.setToLocation(toLocation);
				newTrip.setDistanceInKm(distanceInKm);
				newTrip.setStatus(TripStatus.CONFIRMED);
				driver.getCab().setAvailable(false);

				// adding trip to the list of customers trip list
				Customer customer= customerRepository2.findById(customerId).get();
				List<TripBooking> bookingList= customer.getTripBookingList();
				bookingList.add(newTrip);
				customer.setTripBookingList(bookingList);

				//adding trip to driver's trip list
				List<TripBooking> driverTripList= driver.getTripBookingList();
				driverTripList.add(newTrip);
				driver.setTripBookingList(driverTripList);

				//saving details to DB
				driverRepository2.save(driver);
				customerRepository2.save(customer);
				
				return newTrip;

	}

	@Override
	public void cancelTrip(Integer tripId){
		//Cancel the trip having given trip Id and update TripBooking attributes accordingly
		TripBooking trip= tripBookingRepository2.findById(tripId).get();
		trip.setStatus(TripStatus.CANCELED);
		trip.getDriver().getCab().setAvailable(true);
		trip.setBill(0);
//		driverRepository2.save(trip.getDriver());
		tripBookingRepository2.save(trip);
	}

	@Override
	public void completeTrip(Integer tripId){
		//Complete the trip having given trip Id and update TripBooking attributes accordingly
		TripBooking trip= tripBookingRepository2.findById(tripId).get();
		trip.setStatus(TripStatus.COMPLETED);
		trip.getDriver().getCab().setAvailable(true);
//		driverRepository2.save(trip.getDriver());
		tripBookingRepository2.save(trip);

	}
}
