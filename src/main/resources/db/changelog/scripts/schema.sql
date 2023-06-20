CREATE TABLE companies
(
    id                  SERIAL PRIMARY KEY,
    name                VARCHAR UNIQUE NOT NULL,
    description         VARCHAR        NOT NULL,
    date_of_creation    DATE           NOT NULL
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

CREATE INDEX idx_company_id ON companies (id);
CREATE INDEX idx_department_id ON departments (id);
CREATE INDEX idx_employee_id ON employees (id);

