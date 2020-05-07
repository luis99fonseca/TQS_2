# JustLikeHome

## Transformacao Digital

## Arquitetura Candidata
- Backend : SpringBoot
- Frontend : ReactJs/React Nativa
- Database : SQL

## Roles
- *Product Owner* - Andre Baiao
- *Team Manager* - Miguel Mota
- *DevOps master* - Luis Fonseca e Joao Silva
- *Developers* - Todos

## High Level Features
- Pesquisar casas tendo como parametros o seguinte: 
    - Localizacao
    - Max Price
    - Rating
    - Datas da estadia
    - Numero de hospedes
    
- Criar uma casa para arrendar com os seguintes parametros:
    - Localizacao
    - Preco por noite
    - Numero de quartos
    - Numero de hospedes
    - Descricao
    - Imagens
    - Cozinha (boolean)
    - Numero de casas de banho
    - Comodidades Extra
    
- Alugar uma casa

- Bookmark como favorito

- Pedir review numa casa que ja tenha estado antes de alugar outra
    - Permite ao sistema ter as casos com um rating mais proximo da realidade
- Permite um owner dar review ao cliente
    - Evita que futuros clientes criem conflitos

## Database
### Entidades
#### User
- ID unique PK
- BirthDate date
- Username string
- Name string
#### House
- ID User - Fk
- ID House - PK
- City string 
- Km from City Center float
- Price per night float
- beds int
- numberOfClients int
#### Comodities
- ID House - PK
- type - string
- description - string

#### Owner Review
- ID Review
- ID Owner
#### House Review
- ID Review
- ID House Review
 
#### Reviews
- ID unique 
- Rating int 0-5
- Description
#### Rents
- ID House
- ID User
- from timestamp
- to timestamp
- rating
 
```mermaid
graph LR
A[Rents];B[User];E[Comodities]; F[House]
B -- Reviews --> F
B -- Reviews -->B
B -- Has --> F
F -- Has --> E
F -- Has/Had --> A
```

#### User Stories

#### Backlog
- Jira


#### TODO MODELO DO DOMINIO , ESQUEMA BD, ESQUEMA ARQUITETURA

