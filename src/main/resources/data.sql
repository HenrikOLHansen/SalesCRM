INSERT INTO "COMPANY" (ID, VERSION, NAME) VALUES
(6, 1, 'Region Hovedstaden'),
(7, 1, 'Merkel'),
(8, 1, 'GISP'),
(9, 1, 'E-Work'),
(10, 1, 'Alm. Brand'),
(200, 1, 'SAS'),
(201, 1, 'Nordea'),
(202, 1, 'Novo Nordisk'),
(203, 1, 'Ørsted'),
(204, 1, 'Coolshop'),
(205, 1, 'Ikea'),
(206, 1, 'Rigspolitiet');
INSERT INTO "CONTACT" (ID, VERSION, EMAIL,FIRST_NAME,LAST_NAME,COMPANY_ID, PHONE_NUMBER) VALUES
(11, 1, 'hans.hansen@rh.dk', 'Hans', 'Hansen', 6, '5353656'),
(12, 1, 'poul.jensen@merkel.dk', 'Poul', 'Jensen', 7, '3535353'),
(13, 1, 'per.iversen@rh.dk', 'Per', 'Iversen', 6, '2233445'),
(14, 1, 'pm@almbrand.dk', 'Pia', 'Mikkelsen', 10, '');
-- INSERT INTO "CONSID_CONTACT" (ID, VERSION, FIRST_NAME) VALUES
-- (40, 1, 'Laurids'),
-- (41, 1, 'Markus'),
-- (42, 1, 'Daniel');
INSERT INTO "LEAD" (ID, VERSION, CUSTOMER_CONTACT_ID, START_DATE, END_DATE, DESCRIPTION) VALUES
(61, 1, 11, '2023-12-01', '2024-04-01', 'C# Senior Engineer needed');
INSERT  INTO "SKILL_TYPE" (ID, VERSION, TYPE) VALUES
(700, 1, 'Programming Language'),
(701, 1, 'Technology'),
(702, 1, 'Database'),
(703, 1, 'Methodology'),
(704, 1, 'Cloud');
INSERT  INTO "SKILL" (ID, VERSION, NAME, SKILL_ID) VALUES
(62, 1, 'C#', 700),
(63, 1, 'Java', 700),
(64, 1, 'Salesforce', 701),
(65, 1, 'Kubernetes', 704),
(66, 1, 'Docker', 704),
(67, 1, 'Scrum Master', 703),
(68, 1, 'Frontend', 701),
(69, 1, 'Design/UX', 701),
(70, 1, 'Javascript', 700),
(71, 1, 'AWS', 704),
(72, 1, 'MySQL', 702),
(73, 1, 'PostgreSQL', 702),
(74, 1, 'GraphQL', 701),
(75, 1, 'REST API', 701),
(76, 1, 'MongoDB', 702),
(77, 1, 'Oracle DB', 702),
(78, 1, 'Frontend', 701),
(79, 1, 'Backend', 701),
(80, 1, 'FullStack', 701);
INSERT INTO "CONSULTANT" (ID, VERSION, FIRST_NAME, LAST_NAME) VALUES
(100, 1, 'Henrik', 'Hansen'),
(101, 1, 'Daniel', 'Jensen'),
(102, 1, 'Jesper', 'Dresler'),
(103, 1, 'Mikkel', 'Olsen'),
(104, 1, 'Claus', 'Reinhold');
INSERT INTO "COMPLETED_ASSIGNMENT" (ID, VERSION, CONSULTANT, CUSTOMER_CONTACT, ADDITIONAL_CONTACT, CONSID_CONTACT, START_DATE, END_DATE, DESCRIPTION) VALUES
(301, 1, 'Henrik Hansen', 'Hans Hansen @Region Hovedstaden', '', 'Laurids', '2019-02-10', '2019-10-12', 'Java, backend task for fetching something from a database...'),
(302, 1, 'Daniel Jensen', 'Hans Hansen @Region Hovedstaden', '', 'Daniel', '2020-05-10', '2021-02-28', ''),
(303, 1, 'Henrik Hansen', 'Poul Jensen @Merkel', '', 'Markus', '2017-01-01', '2018-04-30', ''),
(304, 1, 'Mikkel Olsen', 'Poul Jensen @Merkel', '', 'Laurids', '2022-08-17', '2022-10-12', ''),
(305, 1, 'Claus Reinhold', 'Pia Mikkelsen @Alm. Brand', '', 'Daniel', '2023-01-20', '2023-07-25', '');
INSERT INTO "TASK" (ID, VERSION, TASK_CONTACT_ID, LINK, DESCRIPTION, DUE_DATE, USERNAME) VALUES
(400, 1, 11, 'https://www.linkedin.com/jobs/collections/recommended/?currentJobId=3773696405', 'C# Project', '2023-12-01', 'daniel'),
(401, 1, 13, 'https://www.linkedin.com/jobs/view/3737405768/?alternateChannel=search&refId=kJ9G6Hld0CaMiRz3uur9jA%3D%3D&trackingId=FeyENqIJVtxTY1QL5UcF7g%3D%3D', 'Salesforce Opportunity with Nordea', '2024-01-05', 'laurids');
INSERT INTO "USER_TABLE" (ID, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, VERSION) VALUES
(500, 'Laruids', 'Lind', 'laurids.lind@consid.com', '$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW', 1);
INSERT INTO "ROLE" (ID, NAME, VERSION) VALUES
(600, 'ROLE_ADMIN', 1),
(601, 'ROLE_USER', 1);