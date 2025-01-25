# Backend application for project management system

Table of Contents
=================
   * [Introduction](#introduction)
   * [Running the application](#running-the-application)
     * [Setup smtp mail for sending invitation to new users](#setup-smtp-mail-for-sending-invitation-to-new-users)
       * [Prerequisites](#prerequisites) 
       * [Configuring application.properties](#configuring-applicationproperties)
       * [Explanation of SMTP configuration properties](#explanation-of-smtp-configuration-properties)
     * [Environment variables](#environment-variables)
     * [Set up database](#set-up-database)
       * [Generate database schema](#generate-database-schema)
       * [Insert permissions data](#insert-permissions-data)
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

#### Explanation of SMTP configuration properties

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
| `FRONTEND_DOMAIN_URL`        | The base URL of the frontend application.                          |
| `PORT`                       | Web service port, defaults to 10000                                |


### Set up database

#### Generate database schema

When the project run for the first time, the database schema will be created automatically with Hibernate ORM.

- connect to your database
  ```sql
    psql -U your_username -d your_database_name
  ```

##### Insert permissions data

To insert the permissions data into the database, follow the steps below:

1. Download the `permissions_insert.sql` file from the project directory .
2. Run the script in your PostgreSQL database:
  ```sql
   \i /path/to/permissions_insert.sql
  ```

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

