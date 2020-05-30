insert into user (username, password, firstName, lastName, birthDate) values ('andrex','123', 'Andre', 'Baiao', TO_DATE('04/02/2010', 'DD/MM/YYYY'));

insert into user (username, password, firstName, lastName, birthDate) values ('lius', '123', 'Luis', 'Fonseca', TO_DATE('17/12/2015', 'DD/MM/YYYY'));
insert into user (username, password, firstName, lastName, birthDate) values ('Silva', '123', 'Joao', '', TO_DATE('17/02/1999', 'DD/MM/YYYY'));
insert into user (username, password, firstName, lastName, birthDate) values ('MandM', '123', 'Miguel', 'Mota', TO_DATE('13/06/1930', 'DD/MM/YYYY'));

insert into house (city, description, houseName, kmFromCityCenter, pricePerNight, numberOfBeds, maxNumberOfUsers, user_id) values ('viseu', 'nice house', 'house by the beach', 4.0, 34.5, 3, 4, 1);
insert into house (city, description, houseName, kmFromCityCenter, pricePerNight, numberOfBeds, maxNumberOfUsers, user_id) values ('aveiro', 'hot house', 'house by the desert', 32.0, 45.5, 2, 4, 1);
insert into house (city, description, houseName, kmFromCityCenter, pricePerNight, numberOfBeds, maxNumberOfUsers, user_id) values ('coimbra', 'cool house', 'house by the cloud', 32.0, 45.5, 2, 4, 2);
insert into house (city, description, houseName, kmFromCityCenter, pricePerNight, numberOfBeds, maxNumberOfUsers, user_id) values ('faro', 'chrome house', 'house by the chrome', 32.0, 45.5, 2, 4, 3);
insert into house (city, description, houseName, kmFromCityCenter, pricePerNight, numberOfBeds, maxNumberOfUsers, user_id) values ('minho', 'smart house', 'house by the brain', 32.0, 45.5, 2, 4, 2);

insert into rent (house_id, user_id, rentStart, rentEnd, pending) values (2, 3, TO_DATE('02/02/2010', 'DD/MM/YYYY'), TO_DATE('02/02/2010', 'DD/MM/YYYY'), FALSE);
insert into rent (house_id, user_id, rentStart, rentEnd, pending) values (2, 2, TO_DATE('04/02/2010', 'DD/MM/YYYY'), TO_DATE('04/02/2010', 'DD/MM/YYYY'), TRUE);
insert into rent (house_id, user_id, rentStart, rentEnd, pending) values (1, 2, TO_DATE('10/02/2010', 'DD/MM/YYYY'), TO_DATE('11/02/2010', 'DD/MM/YYYY'), TRUE);
insert into rent (house_id, user_id, rentStart, rentEnd, pending) values (3, 1, TO_DATE('03/02/2010', 'DD/MM/YYYY'), TO_DATE('03/02/2010', 'DD/MM/YYYY'), FALSE);

insert into userReviews (from_user_id, to_user_id, rating, description) values (1 , 2, 4, 'muito boa pessoa');
insert into userReviews (from_user_id, to_user_id, rating, description) values (3 , 2, 2, 'otimo dono de casa');

insert into userReviews (from_user_id, to_user_id, rating, description) values (2 , 1, 5, 'este baiao é de confiança. LIKE');

insert into Bookmarked_Houses(user_id, house_id) values (1, 4);
insert into Bookmarked_Houses(user_id, house_id) values (1, 3);
