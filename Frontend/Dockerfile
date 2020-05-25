FROM node:latest

WORKDIR /usr/src/app

COPY ./* ./JustLikeHome/

WORKDIR ./JustLikeHome/example

RUN npm ci

COPY . .

EXPOSE 3000

CMD ["npm","start"]