# Roster Manager

Roster Manager is a full-stack application that I am building as a gift for my mother to help automate the process of creating volunteer rosters.

The goal of the project is to generate fair, flexible schedules while accounting for factors such as volunteer availability, role eligibility, pairing requirements, workload balancing, and scheduling preferences. Rather than manually assembling rosters each month, users will be able to manage volunteers, define roles, and generate schedules automatically.

This project is also an opportunity for me to explore system design, scheduling algorithms, backend architecture, and modern web application development. The application is being built with a Java backend and a React frontend.

## Status

🚧 The backend is feature-complete and fully functional. The remaining work is focused on building the React frontend and connecting it to the existing REST API.

✅ Completed backend functionality includes:

- Volunteer management
- Role management
- Event management
- Availability and unavailability tracking
- Role eligibility configuration
- Paired-role scheduling support
- Fair workload distribution
- Configurable scheduling constraints and preferences
- Automatic roster generation engine
- REST API endpoints
- Service and repository layers
- SQLite database schema and persistence layer

🎯 Planned frontend functionality includes:

- Volunteer, role, and event management UI
- Roster generation interface
- Schedule visualization and review
- Configuration of scheduling rules and preferences
- Exporting rosters to PDF, Word, and other formats

## Motivation
My mother has invested a great deal in my education and career, and this project is my way of using the skills I've developed to build something meaningful that solves a real problem she faces. I hope it becomes a useful tool for her while also serving as a challenging software engineering project for me. I also hope this serves as a good enough ROI for her.

## Technologies

Current technology stack:

- Java 21
- Spring Boot
- React (in progress)
- SQLite
- Maven
- Lombok
- REST APIs
- JUnit

## Future Enhancements

Potential future improvements include:

- __PDF and Word export support__: Allow users to export their roster to a PDF or Word document
- __Manual assignment overrides__: Allow users to lock specific people into particular roles and dates, with the roster generation engine automatically scheduling around those fixed assignments.
- __Generated roster persistence__: Have the app persist generated rosters to the database
