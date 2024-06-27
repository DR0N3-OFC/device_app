# Device API (CC58D-1-2024)

> Desenvolvido pelos alunos: Bruno Raphael Facundo e Matheus Brustolin

---

API desenvolvida como requisito de nota na disciplina de Tópicos Avançados em Computação da Universidade Tecnológica Federal do Paraná.
Consiste em uma API para registro de dispositivos, gateways, sensores, atuadores, etc. Possuindo relacionamento entre si.

Para isso foi utilizado o Java Springboot e banco de dados Postgres para persistência de dados.

Para documentação da API foi utilizado o Swagger, facilitando todo o processo de definição de requisições e retorno de dados da API. 

## Preparando o ambiente

Crie um banco de dados Postgres ou inicialize um container Docker com o Postgres rodando na porta `5432`.

## Rodando

Para executar o projeto, no arquivo `DeviceapiApplication.java` clique "Ctrl + F5" ou clique em "Run -> Run Without Debbuging" na barra de tarefas.

- Vá para o navegador e digite `http://localhost:8080/swagger-ui/index.html#/`, isso abrirá a documentação Swagger para teste da API.

- A aplicação está rodando. Aproveite!!!