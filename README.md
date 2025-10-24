## Energy Control API

Esta é uma API de back-end para um sistema de gerenciamento de consumo de energia, desenvolvida em Spring Boot. O projeto é totalmente containerizado com Docker e Docker Compose para facilitar a execução e o gerenciamento de dependências, incluindo um banco de dados Oracle.

### Pré-requisitos

Antes de começar, garanta que você tenha as seguintes ferramentas instaladas em sua máquina:

*   **Docker e Docker Compose** (Geralmente incluídos no Docker Desktop)
*   **Git** (para clonar o repositório)

### Como Executar

Siga os passos abaixo para clonar, construir e executar a aplicação e seu banco de dados.

**1. Clonar o Repositório**

Abra seu terminal e clone o repositório do projeto:

```bash
git clone https://github.com/leonardomartindev/testes-energy-contol.git
```

**2. Navegar até o Diretório**

Entre na pasta do projeto que você acabou de clonar:

```bash
cd testes-energy-controlapi
```
*(Este diretório é o que contém o arquivo `docker-compose.yml`)*

**3. Construir e Iniciar os Containers**

Execute o Docker Compose. Este comando irá baixar a imagem do banco de dados, construir a imagem da sua API (a partir do `Dockerfile`) e iniciar ambos os containers em segundo plano.

```bash
docker compose up -d
```

**4. Verificar a Execução**

Para garantir que tudo está rodando corretamente, liste os containers em execução:

```bash
docker ps
```

Você deverá ver dois containers com o status "Up" (ou "running"):

*   `energy-control-app` (ou o nome que você definiu)
*   `oracle-db`

### Ambiente de Desenvolvimento para Testes

Para executar os testes automatizados deste projeto, é crucial configurar seu ambiente de desenvolvimento corretamente. Siga as instruções abaixo:

**1. Plugins da IDE (IntelliJ IDEA)**

Certifique-se de que os seguintes plugins estão instalados e ativados na sua IDE:

*   **Lombok:** Para reduzir o código boilerplate (como getters, setters, construtores).
*   **Cucumber for Java:** Para dar suporte à execução de testes BDD escritos em Gherkin.
*   **Gherkin:** Para fornecer syntax highlighting e navegação para os arquivos `.feature`.
*   **Docker:** Para facilitar a integração e o gerenciamento de containers diretamente da IDE.

Você pode instalar esses plugins acessando `File > Settings > Plugins` e pesquisando por cada um deles na Marketplace.

**2. Dependências do Projeto (Maven)**

O projeto utiliza o Maven para gerenciar suas dependências. Para garantir que todas as bibliotecas necessárias para os testes sejam baixadas, você precisa que o Maven processe o arquivo `pom.xml`.

Normalmente, o IntelliJ IDEA faz isso automaticamente ao abrir o projeto. Caso contrário, você pode forçar a sincronização:
*   Abra a aba "Maven" no lado direito da IDE.
*   Clique no ícone de "Reload All Maven Projects" (duas setas em círculo).

Isso garantirá que todas as dependências, como Rest-Assured, Cucumber e JUnit, sejam baixadas e configuradas no seu classpath.

### Autenticação

A API utiliza autenticação baseada em token JWT. Para consumir a maioria dos endpoints, você deve primeiro se autenticar para obter um token de acesso.

**1. Obter o Token de Acesso**

Use uma ferramenta como Postman ou Insomnia para fazer uma requisição `POST` ao endpoint de login:

*   **Método:** `POST`
*   **URL:** `http://localhost:8080/auth/login`
*   **Body (raw/JSON):**
    ```json
    {
      "email": "admin@exemplo.com",
      "password": "admin"
    }
    ```

**2. Usar o Token**

A resposta desta requisição incluirá um token. Copie esse valor.

Para todas as requisições futuras nos endpoints protegidos, você deve incluir este token no cabeçalho (Header) de autorização:

*   **Key:** `Authorization`
*   **Value:** `Bearer SEU_TOKEN_COPIADO_AQUI`

### Endpoints Disponíveis

Todos os endpoints da aplicação principal estão disponíveis sob a URL base: `http://localhost:8080/api/`

*   `/users`
*   `/setores`
*   `/equipamentos`
*   `/limites`
*   `/consumo`
*   `/alertas`
*   `/logs`

**Exemplo de Requisição (GET):**

`GET http://localhost:8080/api/setores` (Lembre-se de incluir o Header `Authorization`!)
