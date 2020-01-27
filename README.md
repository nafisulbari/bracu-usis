# USIS's Advising System
Where teachers can easily find non-clasing theory/lab or both section for students.

This is a project for CSE310, Fall 2019, Brac University

Name: Ahmed Nafisul Bari

ID:   16101237

## Setup And Run

Tested in **IntelliJ IDEA** and **XAMPP**

1. Clone
2. Import project in your favourite IDE
3. Download the dependencies

4. Open **application.properties**
5. Under JDBC properties, use your MySQL server's username and password

6. Goto **Database Backup** folder
7. Import the **usisbk.sql** into your MySQL server

8. Run the UsisApplication
9. Open a web browser and goto http://localhost


## Build

1. In root folder open CMD
2. Type **mvn package** and hit Enter
3. Wait for the JAR build
4. To Run, type **java -jar target/usis-0.1.jar**


## Dummy Data For Testing

Email: reg@bracu.ac.bd

Pass:  123

Email: teach1@bracu.ac.bd

Pass:  111

Email: std1@gmail.com

Pass:  000

