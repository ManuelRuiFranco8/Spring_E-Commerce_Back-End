# Backend - E-commerce Spring Boot Application

This project is a Java-based e-commerce back-end application built with Spring Boot and integrated with a PostgreSQL database. It provides a RESTful API for managing product data, orders, 
and user registration and authentication, intended to support a front-end client via HTTP endpoints.

---

## 🔧 Key Features
- Java 11
- Spring Boot 2.2.6 with embedded Tomcat web server (it runs on `localhost:8080`)
- PostgreSQL database integrated via Hibernate ORM (database URL is `localhost:5432`)
- Spring Security 5.4.1
- JWT (**JSON Web Token**) 0.9.1 for token-based authentication (when a registered user logs in, it receives a token to include in the Authorization header of future HTTP requests)
RESTful API for frontend-backend communication

Application is packaged as Executable JAR (via Maven).

Two roles are distinguished for registered users, allowing different operations:
1. `**USER:**` browse product catalogue, add products to chart, place orders;
2. `**ADMIN:**` browse products, add products to the catalogue, remove products from the catalogue, update product details;

---

## 🎓 Credits
This project has been realized as assignment for the course of "Piattaforme Software per Applicazioni sul Web", A.A. 2022-2023

Master Degree in Telecommunication Engineering: Computing, Smart Sensing, and Networking

DIMES (Dipartimento di Ingegneria Informatica, Modellistica, Elettronica e Sistemistica)

UNICAL (Università della Calabria)
