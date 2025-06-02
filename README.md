Bus Reservation System


Table of Contents
1. Project Overview
2. Technology Stack
3. Features
4. Architecture Diagram
5. Database Design
6. Modules and Pages
7. Security
8. Dependencies
9. Screenshots
10. How to Run
11. Future Improvements

Project Overview
The Bus Reservation System is a web application that allows users to register, login, view available trips, book seats, and manage bookings. Admins can manage buses, trips, and users.

Technology Stack
- Backend: Spring Boot (Java)
- Frontend: Thymeleaf, HTML, CSS
- Database: MongoDB
- Security: Spring Security
- Build Tool: Maven
Features
User:
	- Register & Login
	- View trips
	- Book trips
	- View & cancel bookings

Admin:
	- Manage buses (add/update/list)
	- Manage trips
	- View all users & bookings

Database Design

Models and Relationships:

	- User: has fields: username, password, age, gender, address, roles, @DBRef: Bus
	- Bus: fields: number, type, capacity
	- Trip: @DBRef: user, list of users
	- Booking: @DBRef: Trip
Modules and Pages

- Home: / (Public)
	- Login: /login (Public)
	- Register: /users/register (Public)
	- Add Bus: /buses/add (Admin)
	- List Buses: /buses/list (Admin)
	- Add Trip: /trips/add (Admin)
	- List Trips: /trips/list (Admin/User)
	- Book Trip: /bookings/new (User)
	- View Bookings: /bookings/list (Admin)

Security
- Custom UserDetailsService integrated with MongoDB
	- Password encryption with BCrypt
	- Role-based access (ADMIN, USER)
	- Public access to login, register, CSS



Dependencies (pom.xml)

<dependencies>
                    <dependency>spring-boot-starter-thymeleaf</dependency>
    	     <dependency>spring-boot-starter-web</dependency>
   	      <dependency>spring-boot-starter-data-mongodb</dependency>
   	      <dependency>spring-boot-starter-security</dependency>
  	      <dependency>spring-boot-devtools</dependency>
    	      <dependency>spring-boot-starter-test</dependency>
	</dependencies>


How to Run

1. Clone the repo / open in IDE
	2. Set up MongoDB (local or Atlas)
	3. Run the application:
	   mvn spring-boot:run
	4. Visit http://localhost:8080

Future Improvements

- Email/SMS ticket confirmations
	- Payment integration
	- Seat selection
	- PDF ticket generation
	- Admin dashboard with charts


Conclusion

The Bus Reservation System project successfully streamlines the process of booking and managing bus trips using Spring Boot, MongoDB, and Thymeleaf. It ensures efficient handling of buses, trips, users, and bookings with a user-friendly interface and backend logic. The project meets its goal of creating a functional, scalable, and maintainable application for real-world bus reservation needs.

Developer Info
- Author: Aruneshwaran
- Tools: IntelliJ, MongoDB Compass, Postman (for testing APIs)
- Application Live link : https://bus-reservation-capstone.onrender.com
Note :
 Once you click the link it may take 1 or 2 mins to load the application so wait patiently.
	Login info:-
	admin:
		username = admin
		password = admin
  user:
    username = user
		password = user

