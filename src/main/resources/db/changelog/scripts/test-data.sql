INSERT INTO companies (name, description, date_of_creation, number_of_employees)
VALUES
    ('Company A', 'Sample description for Company A', '2020-01-01', 100),
    ('Company B', 'Sample description for Company B', '2015-05-10', 50),
    ('Company C', 'Sample description for Company C', '2008-09-15', 200),
    ('Company D', 'Sample description for Company D', '2012-03-20', 75),
    ('Company E', 'Sample description for Company E', '2019-11-02', 150),
    ('Company F', 'Sample description for Company F', '2005-07-08', 300),
    ('Company G', 'Sample description for Company G', '2018-04-12', 90),
    ('Company H', 'Sample description for Company H', '2010-06-30', 120),
    ('Company I', 'Sample description for Company I', '2016-02-25', 80),
    ('Company J', 'Sample description for Company J', '2017-12-18', 250);

INSERT INTO departments (name, description, annual_budget, company_id)
VALUES
    ('Department A', 'Sample description for Department A', 100000.00, 1),
    ('Department B', 'Sample description for Department B', 50000.00, 1),
    ('Department C', 'Sample description for Department C', 75000.00, 2),
    ('Department D', 'Sample description for Department D', 250000.00, 3),
    ('Department E', 'Sample description for Department E', 200000.00, 3),
    ('Department F', 'Sample description for Department F', 150000.00, 4),
    ('Department G', 'Sample description for Department G', 80000.00, 5),
    ('Department H', 'Sample description for Department H', 120000.00, 6),
    ('Department I', 'Sample description for Department I', 90000.00, 7),
    ('Department J', 'Sample description for Department J', 180000.00, 8);

INSERT INTO employees (firstname, lastname, position, age, email, date_of_employment, salary_per_month, department_id)
VALUES
    ('John', 'Doe', 'Manager', 35, 'john.doe@example.com', '2019-01-15', 5000.00, 1),
    ('Jane', 'Smith', 'Engineer', 28, 'jane.smith@example.com', '2020-02-20', 4000.00, 1),
    ('Michael', 'Johnson', 'Analyst', 32, 'michael.johnson@example.com', '2018-05-10', 3500.00, 2),
    ('Emily', 'Brown', 'Developer', 30, 'emily.brown@example.com', '2021-03-12', 4500.00, 2),
    ('David', 'Wilson', 'Designer', 27, 'david.wilson@example.com', '2022-04-25', 3800.00, 3),
    ('Sarah', 'Anderson', 'Manager', 40, 'sarah.anderson@example.com', '2017-09-05', 5500.00, 4),
    ('Robert', 'Taylor', 'Engineer', 29, 'robert.taylor@example.com', '2016-06-15', 4200.00, 4),
    ('Olivia', 'Martin', 'Analyst', 31, 'olivia.martin@example.com', '2023-01-02', 3700.00, 5),
    ('James', 'Thompson', 'Developer', 33, 'james.thompson@example.com', '2015-11-08', 4800.00, 6),
    ('Emma', 'Davis', 'Designer', 26, 'emma.davis@example.com', '2014-08-18', 4000.00, 6);
