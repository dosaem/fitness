FROM node:18-alpine

RUN apk add --no-cache curl git docker-cli bash

WORKDIR /app

COPY server.js .
COPY deploy.sh .
RUN chmod +x deploy.sh

CMD ["node", "server.js"]
