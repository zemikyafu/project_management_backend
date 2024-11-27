# Backend application for project management system

Table of Contents
=================
   * [Introduction](#introduction)
   * [Running the application](#running-the-application)
     * [Setup smtp mail for sending invitation to new users](#setup-smtp-mail-for-sending-invitation-to-new-users)
       * [Prerequisites](#prerequisites) 
       * [Configuring application.properties](#configuring-applicationproperties)
       * [Explanation of SMTP Configuration Properties](#explanation-of-smtp-configuration-properties)
     * [Environment variables](#environment-variables)
   * [API Documentation](#api-documentation)
     * [Swagger UI](#swagger-ui)
     * [JSON and YAML files](#json-and-yaml-files)
   * [Deployment](#deployment)
     * [Setting up the project on Render](#setting-up-the-project-on-render)
     * [Render spin-up instructions](#render-spin-up-instructions)
   * [Integrify project details](#integrify-project-details)

## Introduction

This is a backend application for a project management system.
The application is built using Spring Boot and Hibernate.

## Running the application

### Setup SMTP mail for sending invitation to new users

#### Prerequisites

- Gmail Account: Ensure you have a Gmail account to be used for sending emails.
- App Password: Generate an App Password for your Gmail account:
    *  Log in to your Gmail account.
    *  Navigate to Google Account Settings > Security > App Passwords. (Note. Make sure you have 2-Step Verification enabled)
    *  Choose the app and device for which you want to generate the password, then click Generate.
    *  Copy the generated password for use in your application.

#### Configuring application.properties

```properties
# SMTP Configuration for Gmail
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=${SPRING_MAIL_USERNAME}
spring.mail.password=${SPRING_MAIL_PASSWORD}
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

SPRING_MAIL_USERNAME and SPRING_MAIL_PASSWORD are environment variables that should be set in the environment
where the application is running. See the [Environment variables](#environment-variables) section for more details.

#### Explanation of SMTP Configuration Properties

- spring.mail.host: Specifies Gmail's SMTP server (smtp.gmail.com).
- spring.mail.port: Uses port 587 for secure communication.
- spring.mail.username: The email address for the Gmail account used to send emails.
- spring.mail.password: The App Password for your Gmail account.
- spring.mail.properties.mail.smtp.auth: Enables authentication for the SMTP server.
- spring.mail.properties.mail.smtp.starttls.enable: Ensures secure communication by upgrading the connection to use encryption.

### Environment variables

To run the app, configure the required environment variables for this project.
The repository includes an example `.env` file in `src/main/resources/.env.example`.

You can copy the example file and rename it to `.env` with following command (from project root directory):

```bash
cp src/main/resources/.env.example src/main/resources/.env
```

Below are the environment variables required for the application:

| Environment Variable         | Description                                                        |
|------------------------------|--------------------------------------------------------------------|
| `SPRING_DATASOURCE_URL`      | The URL for the database connection.                               |
| `SPRING_DATASOURCE_USERNAME` | The username for the database.                                     |
| `SPRING_DATASOURCE_PASSWORD` | The password for the database.                                     |
| `JWT_SECRET`                 | Secret key for JWT authentication.                                 |
| `SPRING_MAIL_USERNAME`       | The Email address used  to authenticate with the SMTP mail server. |
| `SPRING_MAIL_PASSWORD`       | The Password  used  to authenticate with the SMTP mail server.     |
| `DOMAIN_URL`                 | The base URL of the application domain.                            |
| `PORT`                       | Web service port, defaults to 10000                                |

## API Documentation

### Swagger UI

After running the application, the API documentation can be accessed
from following url: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

### JSON and YAML files

The API documentation can also be accessed in JSON and YAML formats:
- [JSON](http://localhost:8080/v3/api-docs)
- [YAML](http://localhost:8080/v3/api-docs.yaml)

## Deployment

Application is deployed on Render.com. You can access live demo of the application at
[https://fs19-java-backend-cc5x.onrender.com/](https://fs19-java-backend-cc5x.onrender.com/).

API documentation available at [https://fs19-java-backend-cc5x.onrender.com/swagger-ui/index.html](https://fs19-java-backend-cc5x.onrender.com/swagger-ui/index.html).

Deployment is automated from the main branch using Render's GitHub integration.

### Setting up the project on Render

1. Fork the repository to your GitHub account.
2. Create a new web service on Render.
3. Select the repository you forked.
4. Deploy as docker container.
5. Configure the environment variables in the Render dashboard. You can use the `.env.example` file as a reference.
Make sure to keep the port value as `10000`. Read info [here](https://render.com/docs/web-services).

### Render spin-up instructions

Render [spins down](https://render.com/docs/free#spinning-down-on-idle)
a free web service that goes 15 minutes without receiving inbound traffic.
`spin-up.yaml` workflow sends a request to the backend every 14 minutes to prevent spin down.
Setup GitHub repository secret `BACKEND_URL` with the value of the backend health check point.
Backend has actuators enabled by default, so the health check point is available at `/actuator/health`.

e.g.:
```
BACKEND_URL: https://backend.example.com/actuator/health
```

---
### Set Up the Database
#### When the project run for the first time, the database schema will be created automatically with Hibernate ORM.
- connect to your database
  ```sql
    psql -U your_username -d your_database_name
    ```
#####  Run the Permissions Insert Script
 * Download the permissions_insert.sql file from the project directory .
 * Run the script in your PostgreSQL database:
   ```sql
    \i /path/to/permissions_insert.sql
    ```
# Integrify project details

## Teamwork

- Designing REST API endpoints
- Database schema
- Workable backend server with Spring Boot & Hibernate

---

## Menu

- [Vision](#vision)
- [Business Requirements](#business-requirements)
- [Requirements](#requirements)
- [Mandatory Features](#mandatory-features)
- [Optional Features](#optional-features)

---

## Vision

You are required to build a fullstack project management system similar to Trello, Jira, or Monday.

The project can be single- or multi-tenant.

The main requirements are as follows:

- **User Management**
- **Projects and Workspaces**
- **Tasks and Issues**

To take the project to the next level, consider these additional requirements:

- **Collaboration**
- **Real-time Collaboration**
- **Integration with Other Platforms**
- **Reporting and Analytics**

### Business Requirements

- Brainstorm the backend design in terms of entity structure and how they will interact. 
- Discuss the implementation of architecture: CLEAN, DDD, TDD, and any possible pattern your team want to apply (repository pattern, CQRS, etc.).
- **Deadline Management**: Use any project management tool of your choice to ensure timely delivery.

---

## Requirements

_For team assignment, only 1 member should fork the repo, then the admin can invite other members to contribute in the same repo. The rest of the team members, must fork from the common repo(admin repo), making PRs against the common repo when changes are needed. Remember to have a develop branch before merging to main. Each feature/schema/bug/issue should have its own branch, and only one member should work on one branch/file at a time. Before making any new branch, make sure to sync the fork and run `git pull` to avoid conflicts with the common team repo._

1. **Create ERD Diagram** with entities, attributes, and relationships. Include the ERD as an image in the project.

2. **Design the API Endpoints** to follow REST API architecture. Document the endpoints in `.md` files, providing detailed explanations of queries, parameters, request bodies, authentication requirements (if applicable), sample responses, and status codes.

3. **Basic Entities** (Additional entities may be included as needed):
	Possible entities:
   - Tenant
   - User
   - Project
   - Workspace
   - Task
   - Role
   - Permission
   - Comment (optional)
   - Notification (optional)

4. **Develop Backend Server** using CLEAN Architecture:

   - Use exception handler to provide meaningful responses to users.
   - Unit testing is required primarily for the Domain and Service layers. It would be ideal to test also the controllers and data access.
   - The README file should clearly describe the project with sufficient detail and a readable structure.

---

## Mandatory Features

- **User Management**
   - User registration and login functionality
   - User authentication using email/password or other methods (e.g., Google, GitHub)
   - Custom roles and permissions (e.g., HR, Dev, PM, Guest)

- **Projects and Workspaces**
   - Ability to create and manage multiple projects/workspaces
   - Project details: name, description, start/end dates, status

- **Tasks and Issues**
   - Task/issue creation with title, description, priority, and deadline
   - Task/issue tracking: status updates (e.g., To-Do, In Progress, Done)
   - Assign tasks/issues to team members or specific users

- **Boards and Kanban (UI-related)**
   - Customizable boards for different projects/workspaces
   - Card-based representation of tasks/issues on the board
   - Drag-and-drop reordering of cards
   - Board filters and custom views (e.g., due dates, priority)

---

## Optional Features

- **Collaboration and Communication**
   - Notification system: email/text updates on task/issue changes
   - Tagging team members in comments
   - File attachments and commenting on tasks/issues

- **Real-Time Collaboration**
   - Real-time commenting with instant updates for team members
   - Auto-updates for task statuses

- **Integrations and APIs**
   - Integration with Google Drive, Trello, Slack, GitHub issues, calendar, and email clients

- **Gantt Charts and Timelines**
   - Gantt chart visualization for project timelines

- **Reporting and Analytics**
   - Customizable dashboards for project leaders and stakeholders
   - Task/issue analytics: time spent, effort required, conversion rates, etc.

