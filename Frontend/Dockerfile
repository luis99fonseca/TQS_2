FROM node:latest

WORKDIR /usr/src/app

COPY ./* ./JustLikeHome/

WORKDIR ./JustLikeHome/example

RUN npm ci

COPY . .

RUN echo "export const url ='http://192.168.160.52:8080'" > ./src/Rest/Const.js 

EXPOSE 3000

CMD ["npm","start"]
