# Use a imagem base oficial do Node.js
FROM node:18-alpine AS base

# Defina o diretório de trabalho no contêiner
WORKDIR /app

# Copie o package.json e package-lock.json para o diretório de trabalho
COPY package*.json ./

# Instale as dependências do projeto
RUN npm install

# Copie o restante do código da aplicação para o diretório de trabalho
COPY . .

# Exponha a porta em que o servidor backend estará escutando
EXPOSE 3002

# Comando para executar o consumidor
CMD ["node", "app.js"]
