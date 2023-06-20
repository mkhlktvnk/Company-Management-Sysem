INSERT INTO companies (name, description, date_of_creation)
VALUES ('ABC Corporation', 'A multinational technology company', '2005-10-15'),
       ('XYZ Industries', 'A manufacturing company specializing in automotive parts', '1998-06-28'),
       ('Global Investments', 'An investment firm providing financial services', '2010-03-12'),
       ('Tech Solutions Ltd.', 'An IT consulting company', '2015-09-02'),
       ('Innovate Inc.', 'A software development startup', '2020-07-18'),
       ('Globe Logistics', 'A global logistics and shipping company', '1992-12-05'),
       ('EcoTech Energy', 'A renewable energy company', '2008-04-30'),
       ('Healthcare Innovations', 'A healthcare technology company', '2003-11-22'),
       ('MediaWorks', 'A media and entertainment company', '1995-08-10'),
       ('FoodMaster', 'A chain of gourmet restaurants', '2012-02-17'),
       ('Alpha Pharma', 'A pharmaceutical company', '2006-09-08'),
       ('FashionForward', 'A fashion retail brand', '2018-05-25'),
       ('Greenfield Realty', 'A real estate development company', '2001-07-07'),
       ('Education Plus', 'An education technology platform', '2017-03-14'),
       ('SecureBank', 'A leading banking institution', '1990-01-01'),
       ('Travel Adventures', 'A travel agency specializing in adventure tours', '2004-06-12'),
       ('SportsGear', 'A sports equipment manufacturer', '2011-09-20'),
       ('Creative Solutions', 'An advertising and marketing agency', '1997-03-08'),
       ('Wellness First', 'A wellness and fitness center', '2009-11-04'),
       ('DreamBuilders', 'A construction company', '2016-08-30');

INSERT INTO departments (name, description, annual_budget, company_id)
VALUES ('Research and Development', 'Responsible for product innovation and research', 1500000.00, 1),
       ('Sales and Marketing', 'Handles sales strategies and marketing campaigns', 2000000.00, 1),
       ('Production', 'Manages manufacturing operations and quality control', 1000000.00, 2),
       ('Finance', 'Oversees financial planning and budgeting', 500000.00, 3),
       ('Human Resources', 'Handles recruitment, employee relations, and HR policies', 300000.00, 4),
       ('Information Technology', 'Manages IT infrastructure and software development', 1200000.00, 4),
       ('Supply Chain Management', 'Coordinates procurement and logistics', 800000.00, 6),
       ('Customer Service', 'Handles customer inquiries and support', 400000.00, 7),
       ('Legal and Compliance', 'Provides legal guidance and ensures compliance', 600000.00, 9),
       ('Operations', 'Oversees day-to-day operations and process improvement', 900000.00, 11),
       ('Design and Creatives', 'Responsible for graphic design and creative content', 700000.00, 12),
       ('Facilities Management', 'Manages maintenance and facility operations', 350000.00, 13),
       ('Training and Development', 'Provides training programs and professional development', 250000.00, 15),
       ('Risk Management', 'Identifies

 and mitigates potential risks', 450000.00, 16),
       ('Quality Assurance', 'Ensures product and service quality', 550000.00, 17),
       ('Business Development', 'Identifies growth opportunities and strategic partnerships', 950000.00, 18),
       ('Project Management', 'Manages project planning and execution', 650000.00, 19),
       ('Event Planning', 'Organizes corporate events and conferences', 400000.00, 20),
       ('Research and Analytics', 'Conducts market research and data analysis', 300000.00, 20),
       ('Customer Experience', 'Enhances customer satisfaction and loyalty', 500000.00, 20);

INSERT INTO employees (firstname, lastname, position, age, email, date_of_employment, salary_per_month, department_id)
VALUES ('John', 'Doe', 'Senior Software Engineer', 35, 'john.doe@example.com', '2010-05-18', 5000.00, 1),
       ('Jane', 'Smith', 'Sales Manager', 42, 'jane.smith@example.com', '2007-09-02', 6000.00, 2),
       ('Michael', 'Johnson', 'Production Supervisor', 38, 'michael.johnson@example.com', '2015-03-10', 4500.00, 3),
       ('Emily', 'Williams', 'Financial Analyst', 29, 'emily.williams@example.com', '2019-01-25', 5500.00, 4),
       ('Daniel', 'Brown', 'HR Manager', 41, 'daniel.brown@example.com', '2004-11-12', 5500.00, 5),
       ('Olivia', 'Miller', 'IT Manager', 36, 'olivia.miller@example.com', '2012-07-19', 6000.00, 6),
       ('William', 'Jones', 'Supply Chain Coordinator', 33, 'william.jones@example.com', '2017-09-30', 4000.00, 7),
       ('Sophia', 'Davis', 'Customer Service Representative', 28, 'sophia.davis@example.com', '2020-03-15', 3500.00, 8),
       ('David', 'Wilson', 'Legal Counsel', 45, 'david.wilson@example.com', '2002-02-07', 6500.00, 9),
       ('Emma', 'Taylor', 'Operations Manager', 39, 'emma.taylor@example.com', '2008-08-22', 6000.00, 10),
       ('Christopher', 'Anderson', 'Graphic Designer', 31, 'christopher.anderson@example.com', '2013-05-05', 4000.00,
        11),
       ('Ava', 'Thomas', 'Facilities Coordinator', 27, 'ava.thomas@example.com', '2016-12-11', 3500.00, 12),
       ('James', 'Moore', 'Training Specialist', 34, 'james.moore@example.com', '2014-04-28', 5000.00, 13),
       ('Mia', 'Jackson', 'Risk Analyst', 30, 'mia.jackson@example.com', '2018-02-14', 4500.00, 14),
       ('Benjamin', 'White', 'Quality Assurance Engineer', 32, 'benjamin.white@example.com', '2011-10-01', 4500.00, 15),
       ('Abigail', 'Harris', 'Business Development Manager', 37, 'abigail.harris@example.com', '2006-06-26', 7000.00,
        16),
       ('Matthew', 'Clark', 'Project Manager', 40, 'matthew.clark@example.com', '2009-04-03', 6500.00, 17),
       ('Elizabeth', 'Lewis', 'Event Planner', 26, 'elizabeth.lewis@example.com', '2015-08-09', 3500.00, 18),
       ('Andrew', 'Lee', 'Data Analyst', 29, 'andrew.lee@example.com', '2021-02-20', 4000.00, 19),
       ('Sofia', 'Walker', 'Customer Experience Specialist', 31, 'sofia.walker@example.com', '2022-01-10', 4000.00, 20);
