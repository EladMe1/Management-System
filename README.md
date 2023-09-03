# Management System
Welcome to the Project Management System! This web application is designed to streamline project management tasks, user registration, and administration. Below, you'll find useful information about installation, functionality, and a brief overview of the project.

## Table of Contents
1. [Useful Information](#Useful Information)
2. [Installation](#Installation)
3. [General Information](#General-Information)
4. [Functionality](#Functionality)
5. [Screenshots](#Screenshots)

## Useful Information
Admin User:
Email: admin@example.com
Password: password

## Installation
To get started with the Project Management System, follow these steps:

Clone the repository to your local machine.
Set up the 'ex5' database folder using MAMP or any other suitable database management tool.


## General Information
The project consists of a web application developed using the Spring Framework, specifically the Spring MVC (Model-View-Controller) architecture. It provides functionality for user registration, login, user management, and password reset. The application has different controllers responsible for handling various requests and interacting with the database using the UserRepository.

## Functionality
User Registration
The UserController handles the "/api/register" POST request to register a new user. The request body contains the user's first name, last name, email, job, and password. The UserController validates the email to ensure it is not already taken. If the email is available, a new User object is created and saved to the database using the UserRepository.

User Login
The UserController handles the "/api/login" POST request to authenticate a user. The request body contains the user's email and password. The UserController checks if the user exists in the database and verifies the password. If the login credentials are correct, a session is established using the UserSessionBean, and the user is considered logged in. The session stores the user's email, first name, last name, and job.

User Management (Admin)
The AdminController handles the "/admin" GET request to display the admin dashboard. Only the admin user can access this page. The admin user is identified based on the email stored in the UserSessionBean. The AdminController retrieves a list of all users from the UserRepository and passes it to the view. The view displays the user information in a table format.

Adding a User (Admin)
The AdminController handles the "/admin/add" POST request to add a new user. The request parameters include the email, first name, last name, job, and password. The AdminController creates a new User object with the provided information and saves it to the database using the UserRepository. After adding the user, the admin is redirected back to the admin dashboard.

Editing a User (Admin)
The AdminController handles the "/admin/edit/{id}" GET request to retrieve the user's information for editing. The {id} path variable represents the ID of the user to edit. If the user exists in the database, the AdminController retrieves the user's information and passes it to the "edit-user" view. The view displays the user's current information and allows the admin to update it.

Updating a User (Admin)
The AdminController handles the "/admin/edit/{id}" POST request to update a user's information. The {id} path variable represents the ID of the user to update. The request parameters include the updated first name, last name, and job. The AdminController retrieves the user from the UserRepository using the ID and updates the user's information. The updated user is saved back to the database, and the admin is redirected to the admin dashboard.

Deleting a User (Admin)
The AdminController handles the "/admin/delete/{id}" GET request to delete a user. The {id} path variable represents the ID of the user to delete. The AdminController deletes the user from the database using the UserRepository. After deleting the user, the admin is redirected back to the admin dashboard.

Password Reset
The UserController handles the "/api/forgot-password" POST request to reset a user's password. The request body contains the user's first name, last name, email, and job for verification. The UserController retrieves the user from the database and compares the provided user information. If the information matches, a new password is generated, and the user's password is updated in the database. An email is sent to the user with the new password or a password reset link.

## Screenshots
Here are some screenshots of the Project Management System:

Dashboard
![](img/1.jpg)

User Management (Admin)
User Management

User Profile
User Profile

Password Reset
Password Reset

