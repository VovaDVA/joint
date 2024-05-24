FROM node:20.11.1-alpine AS builder

WORKDIR /joint/client

COPY package*.json ./

RUN npm install

COPY . .

RUN npm run build

FROM nginx:alpine
COPY --from=builder /joint/client/dist /usr/share/nginx/html

# Копируем конфигурацию Nginx
COPY nginx.conf /etc/nginx/nginx.conf

# Порты
EXPOSE 80

# Команда запуска Nginx
CMD ["nginx", "-g", "daemon off;"]