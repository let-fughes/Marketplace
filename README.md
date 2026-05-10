# BARAHOLISHE Marketplace 

This project is a lightweight marketplace platform consisting of a Spring Boot microservice for the backend and a modern, responsive frontend built with HTML, Vanilla JavaScript, and Tailwind CSS.

## Tech Stack

### **Backend**

* **Java / Spring Boot 3**: The core framework for the microservice architecture.


* **Spring Web & Validation**: Used for building RESTful endpoints and validating request data.


* **REST API**: Standardized communication between the frontend and the database.


* **Lombok**: Implemented to reduce boilerplate code and improve readability.



### **Frontend**

* **HTML5 & Vanilla JavaScript**: Core logic utilizing the Fetch API for real-time backend communication.


* **Tailwind CSS**: A utility-first CSS framework used via CDN for rapid styling.


* **Google Fonts**: Uses the "Inter" font family for a professional user interface.



---

## Key Features

* **Ad Feed**: Dynamic loading and display of product cards, including prices and category labels.


* **Category Filtering**: Integrated filtering system using the `/getByCategory/{category}` endpoint to sort items by type.


* **Live Search**: Instant client-side filtering that allows users to find items by name.


* **User Profile**: Supports JWT/Bearer authentication, displaying user roles, names, and dynamic avatars with MinIO integration.


* **Media Management**: Support for uploading and retrieving product images via `MultipartFile`.



---

## API Overview (`ItemController`)

| Method | Endpoint | Description |
| --- | --- | --- |
| **GET** | `/v1/api/items` | Retrieve all items.

 |
| **GET** | `/v1/api/items/id/{id}` | Get specific item details by ID.

 |
| **GET** | `/v1/api/items/getByCategory/{category}` | Filter marketplace items by a specific category.

 |
| **GET** | `/v1/api/items/getImage/{id}` | Get the direct URL for a product image.

 |
| **POST** | `/v1/api/items` | Create a new advertisement (JSON + Image file).

 |
| **PUT** | `/v1/api/items/{id}` | Update existing product information.

 |
| **DELETE** | `/v1/api/items/{id}` | Remove an advertisement from the platform.

 |

---

## Getting Started

### 1. Backend Setup

* Ensure the API Gateway is running on port **8080**.


* Run the microservice using Maven:


```bash
./mvnw spring-boot:run

```



### 2. Frontend Setup

* The frontend is standalone and does not require Node.js installation.


* Simply open `index.html` in your web browser.


* **Pro Tip**: Use a local server (like the **Live Server** VS Code extension) to handle CORS and authorization headers correctly.
