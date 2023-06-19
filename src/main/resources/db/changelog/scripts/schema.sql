CREATE TABLE companies
(
    id                  SERIAL PRIMARY KEY,
    name                VARCHAR UNIQUE NOT NULL,
    description         VARCHAR        NOT NULL,
    date_of_creation    DATE           NOT NULL,
    number_of_employees BIGINT         NOT NULL
);

CREATE TABLE departments
(
    id            SERIAL PRIMARY KEY,
    name          VARCHAR        NOT NULL,
    description   VARCHAR        NOT NULL,
    annual_budget NUMERIC(19, 2) NOT NULL,
    company_id    BIGINT REFERENCES companies (id)
);

CREATE TABLE employees
(
    id                 SERIAL PRIMARY KEY,
    firstname          VARCHAR        NOT NULL,
    lastname           VARCHAR        NOT NULL,
    position           VARCHAR        NOT NULL,
    age                INTEGER        NOT NULL,
    email              VARCHAR        NOT NULL UNIQUE,
    date_of_employment DATE           NOT NULL,
    salary_per_month   NUMERIC(19, 2) NOT NULL,
    department_id      BIGINT REFERENCES departments (id)
);
