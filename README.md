# Smart Petrzalka

### Backend

#### How to run
```aiignore
./gradlew bootBuildImage
docker-compose  -f docker-compose.yml -p smart-pertzalka1 up -d
```

## Overview

This application is a backend for Smart Petrzalka project. It is a REST API that provides endpoints for managing users, reservations, and other entities. It is built using Spring Boot and uses PostgreSQL as a database.

### All endpoints are available at `/swagger-ui/index.html`

### Login and registration are implemented using JWT tokens

### All requests beside login, registration and api doc require a valid JWT token in the Authorization header

## Implemented features
- User authentication
- Reservation management
- Payment simulation
- Email sending
- Qr code generation
- Communication with the Raspberry Pi to turn on/off the lights automatically when a reservation is started/ended
- Admin functionality
  - Flexible managing of playground open hours and prices
  - Monitoring of reservations


## Technologies
- Kotlin
- Spring
- PostgreSQL
- Docker