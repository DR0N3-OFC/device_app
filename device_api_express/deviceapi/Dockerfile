FROM node:18-alpine AS base

EXPOSE 3000

WORKDIR /app

COPY package*.json ./

RUN rm -rf node_modules && npm install

COPY . .

CMD ["npm", "start"]
