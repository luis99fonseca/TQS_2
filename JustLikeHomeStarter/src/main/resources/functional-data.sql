insert into user (username, password, firstName, lastName, birthDate) values ('andrex','123', 'Andre', 'Baiao', TO_DATE('04/02/2010', 'DD/MM/YYYY'));

insert into user (username, firstName, lastName, birthDate) values ('lius', 'Luis', 'Fonseca', TO_DATE('17/12/2015', 'DD/MM/YYYY'));
insert into user (username, firstName, lastName, birthDate) values ('Silva', 'Joao', '', TO_DATE('17/02/1999', 'DD/MM/YYYY'));
insert into user (username, firstName, lastName, birthDate) values ('MandM', 'Miguel', 'Mota', TO_DATE('13/06/1930', 'DD/MM/YYYY'));

insert into house (city, description, houseName, kmFromCityCenter, pricePerNight, numberOfBeds, maxNumberOfUsers, user_id) values ('viseu', 'nice house', 'house by the beach', 4.0, 34.5, 3, 4, 1);
insert into house (city, description, houseName, kmFromCityCenter, pricePerNight, numberOfBeds, maxNumberOfUsers, user_id) values ('aveiro', 'hot house', 'house by the desert', 32.0, 45.5, 2, 4, 1);
insert into house (city, description, houseName, kmFromCityCenter, pricePerNight, numberOfBeds, maxNumberOfUsers, user_id) values ('coimbra', 'hot house', 'house by the cloud', 32.0, 45.5, 2, 4, 2);
insert into house (city, description, houseName, kmFromCityCenter, pricePerNight, numberOfBeds, maxNumberOfUsers, user_id) values ('faro', 'hot house', 'house by the chrome', 32.0, 45.5, 2, 4, 3);

insert into rent (house_id, user_id, rentStart, rentEnd, pending) values (2, 3, TO_DATE('02/02/2010', 'DD/MM/YYYY'), TO_DATE('02/02/2010', 'DD/MM/YYYY'), FALSE);
insert into rent (house_id, user_id, rentStart, rentEnd, pending) values (2, 2, TO_DATE('04/02/2010', 'DD/MM/YYYY'), TO_DATE('04/02/2010', 'DD/MM/YYYY'), TRUE);
insert into rent (house_id, user_id, rentStart, rentEnd, pending) values (3, 1, TO_DATE('03/02/2010', 'DD/MM/YYYY'), TO_DATE('03/02/2010', 'DD/MM/YYYY'), FALSE);
