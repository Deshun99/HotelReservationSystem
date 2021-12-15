# IS2103 Enterprise Systems Server-Side Design and Development (Pair Project 2021)
This pair project is one of NUS School of Computing (Information Systems [IS] major) core IS modules.

## General Business Rules
1. Every entity class has a unique key identifier.
2. Not all entities must perform CRUD (Create, Remove, Update & Delete) operations, only RoomType, Room, RoomRate requires CRUD operation .
3. When a new reservation request is received, the system does not need to allocate a hotel room before confirming the reservation. The system only needs to ensure that the hotel has sufficient room inventory to fulfil the new reservation.
4. In the event of overbooking for a particular room type, the guests can be upgraded to the next higher available room type at no additional cost.
5. Allocation of hotel rooms to guest reservations is performed daily at 2 am.
6. Check-out time occurs at 12 noon on the departure date and the Check-in time occurs at 2 pm on the arrival date.


## General Rationales
1. By having a unique key identifier, which is a primary key, data objects can be retrieved easily and directly with its own ID via invoking the find method in EntityManager.
2. The use of multiple session beans is to streamline performance and avoid unnecessary performance overhead 
3. Once the guest is checked in, the room will not change throughout the duration of their stay. 
4. All price calculations are done with a BigDecimal object as it would help maintain accuracy.


## Assumption for Logical Data Model
1. We assume that each room is numbered using a combination of a two-digit floor number and a two-digit sequence number for that floor in ascending order.
2. We assume that there can be a maximum of 9999 rooms with the two digit floor number and two digit sequence number combination.
3. We assume that the Hotel Reservation System will also be exposed to external partners such as price comparison websites, online travel websites and travel agencies besides Holiday.com.
4. We assumed that there is a fixed 2hr cleaning period from 12 pm to 2pm whenever the guest checks-out.

# High-level architecture of Hotel Reservation System [FRS] 
This project also known as HoRS, consists of 3 parts:
1. HoRS Management Client
2. HoRS Reservation Client
3. Holiday Reservation System

![Alt Text](https://i.imgur.com/a/N7fKf3b)

## Singleton Session Bean 
### Data Initialisation [HoRS System]
- [X] Loading of Test Data

### System Helper
-	[X] Allocate Rooms Daily
-	[X] Get Available Room
-	[X] Get All Reservation

## Stateful Session Bean
### Room Reservation Controller
-	[X] Guest Login
-	[X] Retrieve All Reservation
-	[X] Check In Room
-	[X] Get Reservation List By Email
-	[X] Checkout Room
-	[X] Assign Walk in Room 

## Stateless Session Bean 
### Employee Entity Session Bean [HoRS Management Client]
- [X] Employee Login
- [X] Employee Logout
- [X] Create New System Admin
-	[X] Create New Operation Manager
-	[X] Create New Sales Manager
-	[X] Create New Guest Relation Officer

### Guest Entity Session Bean
-	[X] Register New Guest
-	[X] Login Guest

### Partner Entity Session Bean
-	[X] View Reservation Detail
-	[X] Retrieve All Partner Reservations
-	[X] Retrieve Partner By Id
-	[X] Partner Login
-	[X] Retrieve All Partners
-	[X] Create New Partner

### Reservation Entity Session Bean 
- [X] Retrieve Reservation Detail
-	[X] Search Room
-	[X] Guest Reserve Room 
-	[X] Front Office Reserve Room
-	[X] Partner Reserve Room
-	[X] Set Assign Room 

### Room Entity Session Bean
-	[X] Create New Room Type
-	[X] Return New Room Type Entity
-	[X] View Room Type Details
-	[X] Update Room Type
-	[X] Delete Room Type
-	[X] Get Room Types By Ranking
-	[X] Create New Room
-	[X] Update Room
-	[X] Delete Room
-	[X] Create New Published Rate
-	[X] Create New Normal Rate
-	[X] Create New Peak Rate
-	[X] Create New Promotion Rate
-	[X] Retrieve Room By Room Number
-	[X] Retrieve All Rooms
-	[X] View Room Details
-	[X] Get List Of Exception Reports By Date
-	[X] Retrieve All Room Rates
-	[X] Delete Room Rate
-	[X] Update Room Rate

### Flight Reservations Session Bean [Sales Management Module]
- [X] View Flight Reservations
- [X] View My Flight Reservations
- [X] View My Flight Reservation Details

## FRS Reservation Client (for Customers)
### Customer Session Bean 
- [X] Register As Customer
- [X] Customer Login
- [X] Customer Logout

![Alt Text](https://i.imgur.com/a/ritQPrg)

## Holiday Reservation System (External Party - Web Services)
- [X] Partner Login
- [X] Partner Search Room
- [ ] Partner Reserve Room
- [X] View Partner Room Reservations
- [X] View Partner Room Reservation Details
- [X] Partner Logout

