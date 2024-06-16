# Rental-software

Write software that allows a rental company to manage a fleet of vehicles such as ICE/hybrid/BEV cars, motorcycles, pickups, and campers. The software should allow you to search for available vehicles in a given category, make a reservation, and issue an invoice


## What in use:
                
* Maven
* Java and javaFx
* SQLite
				

## Feature
- A rental vehicle's software allow customers to see and reserve for vehicles (MainApp)
- A manangement application so the manager can add, update or delete vehicles; see and delete the reservations. (ManagementApp)

### 13.06
#### Main application
+ Able to search for vehicle`s name and brand
+ Categories by vehicle`s type and brand
+ Able to see the vehicle`s details 
+ Able to make reservations from the vehicle`s details window

#### Management application
+ Able to see, update or delete the vehicles (CRUD Vehicle table)
+ Can also search for the vehicle`s by name, type or brand
+ Able to see the reservations (Reservation table)

#### Database (SQLite)
+ Tables
	+ Vehicles (id, brand, model, type, isAvailable, pricePerDay)
	+ Reservations (reservationId, vehicleId, customerName, customerEmail, customerPhone, reservationDate, returnDate)
		+ vehicleId <--> Vehicles(id)
+ Sample data of vehicles
  
### 14.06
+ Update pom.xml

  
#### In progress
+ Issue invoice
+ Validate the customer email and phone, date (reservation date and return date)
+ Sort function in main application
