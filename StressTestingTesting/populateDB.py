import string
from datetime import date
import random
import time
import datetime

def insert(table_name, parameters, values):
    if table_name != "rent":
        return "insert into " + table_name + " (" + ','.join(parameters) + ") values " + str(values)
    else:
        # print("ELSE", parameters)
        temp_string = "insert into " + table_name + " (" + ','.join(parameters) + ") values " + str(values[:-1])
        return temp_string[:-1] + ", " + str(values[-1]).upper() + ")"

def str_time_prop(start, end, format, prop):
    """Get a time at a proportion of a range of two formatted times.

    start and end should be strings specifying times formated in the
    given format (strftime-style), giving an interval [start, end].
    prop specifies how a proportion of the interval to be taken after
    start.  The returned time will be in the specified format.
    """

    stime = time.mktime(time.strptime(start, format))
    etime = time.mktime(time.strptime(end, format))

    ptime = stime + prop * (etime - stime)

    return time.strftime(format, time.localtime(ptime))

def random_date(start="2018-1-1 1:00", end="2020-06-30 23:00"):
    return str_time_prop(start, end, '%Y-%m-%d %H:%M', random.random())

def compareDatas(d1, d2):
    sd1 = d1.split("-")
    sd2 = d2.split("-")
    # print(sd1 , " -- " , sd2)
    if (int(sd1[0]) > int(sd2[0])):
        # print("anp: ", int(sd1[0]) , int(sd2[0]))
        return True
    elif (int(sd1[1]) > int(sd2[1])):
        # print("mes: ", int(sd1[1]) , int(sd2[1]))
        return True
    elif (int(sd1[2]) > int(sd2[2])):
        # print("dia: ", int(sd1[2]) , int(sd2[2]))
        return True
    return False

## define number of each table entrance
user_no = 50
house_no = 70
rent_no = 80
reviews_no = 47  #not realli editable
bookmark_no = 90
# user_no = 4
# house_no = 4
# rent_no = 4
# reviews_no = 4  #not realli editable
# bookmark_no = 4

user_f_name = ["João", "José", "Maria", "Luís", "Miguel", "Rafael", "Rafaela", "Pedro", "Rita", "Inês", "Marta",
                  "Margarida", "Francisca", "Leonor", "Ana", "Lara", "Alice", "Mafalda", "Helena", "Teresa", "Carla", "Filipa",
                  "Soraia", "Rosa", "Vera", "Santiago", "Rodrigo", "Dinis", "Tiago", "Diogo", "Guilherme", "Vasco",
                  "Luís", "Isaac", "Kevin", "Bruno", "Manuel", "Micael", "Artur", "Igor", "Edgar"]

user_l_name = ["Silva", "Santos", "Ferreira", "Pereira", "Oliveira", "Costa", "Rodrigues", "Martins", "Jesus",
                  "Sousa", "Fernandes", "Gonçalves", "Gomes", "Marques", "Almeira", "Ribeiro", "Pinto", "Carvalho",
                  "Teixeira", "Moreira", "Rocha"]

index_user = 0
username = []
password = []
firstName = []
lastName = []
birthDate = []
while index_user < user_no:

    name01 = random.choice(user_f_name)
    firstName.append(name01)

    name02 = random.choice(user_l_name)
    lastName.append(name02)

    temp_birth = random_date('1975-04-18 00:04', '1999-09-11 00:04')
    birthDate.append(temp_birth.split(" ")[0])

    temp_username = name01[0 : random.randint(1, len(name01))] + name02[0 : random.randint(1, len(name02))]
    username.append(temp_username)

    password.append(''.join(random.choice(string.digits) for i in range(6)))

    index_user += 1

with open("SqlUser.sql", "w") as file01:
    for i in range(user_no):
        file01.write((insert("user", ("username", "password", "firstName", "lastName", "birthDate"), (username[i], password[i], firstName[i], lastName[i], birthDate[i]) )) + ";\n")
        row = (insert("user", ("username", "password", "firstName", "lastName", "birthDate"), (username[i], password[i], firstName[i], lastName[i], birthDate[i]) ))
        print(row)
print()

######## HOUSE
house_f_name = ["Casa de", "Mansão de", "Prédio de", "Casarão de", "Casota de", "Palácio de"]
house_l_name = ["Piedade", "Principal", "Grilo", "Serragem", "Creoulo", "Malta", "Social", "Santiago"]
descriptions = ["Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec consectetur gravida sapien nec imperdiet.",
                "In hac habitasse platea dictumst. Fusce porta venenatis congue. Fusce tempor tincidunt dictum.",
                "Mauris id pellentesque tortor. Aenean faucibus felis nec feugiat pretium. Mauris ac dolor sed purus tincidunt bibendum.",
                " Nulla dapibus, mauris quis egestas gravida, mauris justo scelerisque diam, sed ornare lacus est quis sem.",
                " Donec porttitor pretium ex, vel malesuada enim tincidunt vel. Suspendisse eu gravida velit. ",
                "Nunc blandit, mi id lobortis eleifend, justo lacus placerat elit, id iaculis mi lacus sit amet nisi."
                ]
cities = ["Viseu", "Aveiro", "Espinho", "Ponta Delgada", "Porto", "Coimbra", "lisboa", "faro", "Minho"]

index_house = 0
house_city = []
house_description = []
house_name = []
house_kmFC = []
house_pPN = []
house_nOB = []
house_mNOU = []
house_user = []

house_dict = {}

while index_house < house_no:

    house_city.append(random.choice(cities))

    house_description.append(random.choice(descriptions))

    house_name.append(random.choice(house_f_name) + " " + random.choice(house_l_name))

    house_kmFC.append(random.randint(1, 20))

    house_pPN.append(random.randint(15, 70))

    house_nOB.append(random.randint(1, 7))

    house_mNOU.append(random.randint( (house_nOB[index_house]), (house_nOB[index_house] * 2) ))

    temp_user01 = random.randint(1, user_no)
    house_user.append(temp_user01)

    if temp_user01 not in house_dict:
        house_dict[temp_user01] = [index_house + 1]
    else:
        house_dict[temp_user01].append(index_house + 1)

    index_house += 1

with open("SqlHouse.sql", "w") as file01:
    for i in range(index_house):
        file01.write( (insert("house", ("city", "description", "houseName", "kmFromCityCenter", "pricePerNight", "numberOfBeds", "maxNumberOfUsers", "user_id"), (house_city[i], house_description[i], house_name[i], house_kmFC[i], house_pPN[i], house_nOB[i], house_mNOU[i], house_user[i] ) )) + ";\n")
        row = (insert("house", ("city", "description", "houseName", "kmFromCityCenter", "pricePerNight", "numberOfBeds", "maxNumberOfUsers", "user_id"), (house_city[i], house_description[i], house_name[i], house_kmFC[i], house_pPN[i], house_nOB[i], house_mNOU[i], house_user[i] ) ))
        print(i + 1, "" , row)
print()

index_rent = 0
rent_hI = []
rent_uI = []
rent_Start = []
rent_End = []
rent_P = []

rent_user_house_dict = {}

while index_rent < rent_no:

    temp_user = random.randint(1, user_no)
    if temp_user not in house_dict:
        house_dict[temp_user] = []
    rent_uI.append(temp_user)

    # guarantees is not own house
    temp_house = random.randint(1, house_no)
    # print("BEFORE", house_dict)
    while temp_house in house_dict[temp_user]:
        # print("ESTA", temp_house, temp_user)
        temp_house = random.randint(1, house_no)
    rent_hI.append(temp_house)

    if temp_user not in rent_user_house_dict:
        rent_user_house_dict[temp_user] = []
    rent_user_house_dict[temp_user].append(temp_house)

    temp_start = random_date('2020-01-01 00:04')
    rent_Start.append(temp_start.split(" ")[0])

    temp_end = random_date(temp_start)
    rent_End.append(temp_end.split(" ")[0])

    nowTime = datetime.datetime.now()
    # print("data: ", nowTime.strftime("%Y-%m-%d %H:%M"), temp_start, not compareDatas(nowTime.strftime("%Y-%m-%d %H:%M").split(" ")[0], temp_start.split(" ")[0]))
    rent_P.append(bool(not compareDatas(nowTime.strftime("%Y-%m-%d %H:%M").split(" ")[0], temp_start.split(" ")[0])))
    index_rent += 1

with open("SqlRent.sql", "w") as file01:
    for i in range(index_rent):
        # print(compareDatas(rent_End[i].split(" ")[0], rent_Start[i].split(" ")[0] ))
        file01.write((insert("rent", ("house_id", "user_id", "rentStart", "rentEnd", "pending"), (rent_hI[i], rent_uI[i], rent_Start[i], rent_End[i], rent_P[i]) )) + ";\n")
        row = (insert("rent", ("house_id", "user_id", "rentStart", "rentEnd", "pending"), (rent_hI[i], rent_uI[i], rent_Start[i], rent_End[i], rent_P[i]) ))
        print(row)
print()

index_reviews = 0
reviews_userFrom = []
reviews_userTo = []
reviews_rate = []
reviews_description = []

descriptions = ["muito", "pessoa", "fixe", "boa", "inteligente", "smart", "simpatico", "cidadao", "compatriota", "camarada", "companheiro", "individuo", "lindo"]

if True:

    # print("ownsers: ", house_user)
    # print("dict: ", house_dict)
    # print("rents user:houses", rent_user_house_dict)
    for user in rent_user_house_dict:
        # print("-----USER: ", user)

        for h in range(random.randint(0, len(rent_user_house_dict[user]))):
            # print("H: ", h, " ;", rent_user_house_dict[user][h])
            # print("---HOUSE: ", rent_user_house_dict[user][h], " -> ", rent_user_house_dict[user], " -- ", house_user[rent_user_house_dict[user][h] - 1])
            reviews_userFrom.append(house_user[rent_user_house_dict[user][h] - 1])
            reviews_userTo.append(user)
            reviews_rate.append(random.randint(0, 5))
            reviews_description.append(random.choice(descriptions)+ " "+random.choice(descriptions)+ " " +random.choice(descriptions))
            index_reviews += 1

index_hreviews = 0
hreviews_userId = []
hreviews_houseId = []
hreviews_rate = []
hreviews_description = []

descriptions = ["muito", "casa", "fixe", "boa", "inteligente", "smart", "simpatico", "edificio", "construçao", "infrasestrutura", "local", "sitio", "lindo"]

if True:

    # print("ownsers: ", house_user)
    # print("dict: ", house_dict)
    # print("rents user:houses", rent_user_house_dict)
    for user in rent_user_house_dict:
        # print("-----USER: ", user)
        # print(">>len: ", len(rent_user_house_dict[user]))
        for h in range(len(rent_user_house_dict[user])):
            # print("H: ", h, " ;", rent_user_house_dict[user][h])
            # print("---HOUSE: ", rent_user_house_dict[user][h], " -> ", rent_user_house_dict[user], " -- ", house_user[rent_user_house_dict[user][h] - 1])
            hreviews_houseId.append(rent_user_house_dict[user][h])
            hreviews_userId.append(user)
            hreviews_rate.append(random.randint(0, 5))
            hreviews_description.append(random.choice(descriptions)+ " "+random.choice(descriptions)+ " " +random.choice(descriptions))
            index_hreviews += 1

with open("SqlHouseReview.sql", "w") as file01:
    for i in range(index_hreviews):
        # print(compareDatas(rent_End[i].split(" ")[0], rent_Start[i].split(" ")[0] ))
        file01.write((insert("houseReviews", ("user_id", "house_id", "rating", "description"), (hreviews_userId[i], hreviews_houseId[i], hreviews_rate[i], hreviews_description[i]))) + ";\n")
        row = (insert("houseReviews", ("user_id", "house_id", "rating", "description"), (hreviews_userId[i], hreviews_houseId[i], hreviews_rate[i], hreviews_description[i])))
        print(row)

index_bookmark = 0
bookmark_user = []
bookmark_house = []

while index_bookmark < bookmark_no:

    temp_user = random.randint(1, user_no)
    if temp_user not in house_dict:
        house_dict[temp_user] = [index_house]
    bookmark_user.append(temp_user)

    # guarantees is not own house
    temp_house = random.randint(1, house_no)
    while temp_house in house_dict[temp_user]:
        temp_house = random.randint(1, house_no)
    bookmark_house.append(temp_house)
    index_bookmark += 1

with open("SqlHousemark.sql", "w") as file01:
    for i in range(index_bookmark):
        file01.write((insert("Bookmarked_Houses", ("user_id", "house_id"), (bookmark_user[i], bookmark_house[i] ))) + ";\n")
        row = (insert("Bookmarked_Houses", ("user_id", "house_id"), (bookmark_user[i], bookmark_house[i] )))
        print(row)
