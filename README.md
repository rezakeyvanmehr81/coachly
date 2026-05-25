# Coachly

## Project Description

Coachly is a coaching session booking platform where coaches can publish training, wellness, nutrition, and personal development services, while clients can browse, filter, and book sessions by date. The platform keeps the original marketplace flow: secure authentication, service listings with images, date-based bookings, client and coach dashboards, and an admin panel for managing users, services, and bookings.

## Core User Stories

### 1. The Coach
- As a personal trainer, I want to publish a strength coaching service with a session rate so that clients can book time with me.
- As a wellness coach, I want to describe my specialization and location so clients can choose the right session format.

### 2. The Client
- As a client, I want to search coaching services by category and price so that I can find a coach that matches my goals.
- As a client, I want to book a coaching session for selected dates and track it from my dashboard.

### 3. The Admin
- As an administrator, I want to manage users, coaching services, and bookings so the platform stays clean and trustworthy.

## Running With Docker

This is the easiest way to run the full application with PostgreSQL.

### Prerequisites

- Docker Desktop installed and running

### Steps

bash
cd Coachly-main/Coachly
docker-compose up --build


Once the app starts, open:

text
http://localhost:8080


### Default Accounts

text
admin@coachly.com / admin123

maya@example.com / password123

daniel@example.com / password123

elena@example.com / password123


### Services

| Service  | Container     | Port          |
|----------|---------------|---------------|
| App      | coachly-app | 8080:8080   |
| Database | coachly-db  | 5433:5432   |

The PostgreSQL database uses:

text
database: coachly
username: coachly
password: coachly123
