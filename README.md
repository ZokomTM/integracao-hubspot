# Integração com HubSpot - API REST

## Descrição

Este projeto implementa uma API REST em Java usando o Spring Boot para integrar com a API do HubSpot.

1. **Geração da Authorization URL**: Gera e retorna a URL de autorização para iniciar o fluxo OAuth com o HubSpot.
2. **Processamento do Callback OAuth**: Recebe o código de autorização e realiza a troca pelo token de acesso.
3. **Criação de Contatos**: Endpoint para criar um contato no CRM do HubSpot.
4. **Listagem de Contatos**: Endpoint para listar todos os contatos no CRM do HubSpot.
5. **Recebimento de Webhook para Criação de Contatos**: Esse endpoint não consegui implementar.

## Requisitos

Antes de rodar a aplicação, certifique-se de ter os seguintes requisitos instalados:

- Java 17
- Maven
- Conta de desenvolvedor no HubSpot

## Como Executar

### Passo 1: Clonar o repositório

Clone este repositório em sua máquina local:

git clone https://github.com/ZokomTM/integracao-hubspot.git
cd integracao-hubspot

### Configurar variáveis de ambiente (application.properties)

## Configuração banco de dados
spring.datasource.url=<sua-string-jdbc-postgresql>
spring.datasource.username=<seu-username-banco-dados>
spring.datasource.password=<sua-senha-banco-dados>
spring.datasource.driver-class-name=org.postgresql.Driver

## Configuração HubSpot
hubspot.client.id=<seu-client-id>
hubspot.client.secret=<seu-client-secret>
hubspot.redirect.uri=<seu-redirect-uri> no meu caso http://localhost:8081/api/oauth/callback
hubspot.scopes=crm.objects.contacts.read crm.objects.contacts.write

### Executar aplicação no terminal

mvn spring-boot:run


### Endpoints criados

## Retornar URL de Authorização

GET /api/oauth/authorize

## Criar contato no CRM

POST /api/contact

Body:
{
    "properties": {
        "email": "fernan@email.com",
        "firstname": "Fernan",
        "lastname": "Ferras",
        "phone": "+5547996330692"
    }
}


## Retornar lista de contatos no CRM

GET /api/contact/list


### Explicação Geral

Optei por utilizar o Spring Boot junto ao PostgreSQL para o banco de dados, pois já tenho experiência com ambas as tecnologias e me sinto mais confortável com elas.

Inicialmente, pensei em utilizar bibliotecas como o Lombok para simplificar a criação de construtores, getters e setters. No entanto, como o projeto é pequeno, optei por não utilizá-la, pois não vi necessidade.

A estrutura do código foi organizada de forma simples, separando as classes em pacotes com funções semelhantes. Coloquei toda a lógica de negócios e chamadas para APIs dentro dos serviços (services), enquanto o controlador (controller) ficou responsável apenas pela comunicação com os serviços.

Quanto à implementação dos métodos, enfrentei dificuldades iniciais para encontrar as chaves necessárias para autenticação. Após algum tempo, percebi que o problema estava no fato de estar utilizando uma conta de desenvolvedor do HubSpot. Como o teste indicava o uso dessa conta, demorei a perceber que era necessário criar uma conta ativa para realizar a autenticação.

Após resolver isso, a criação de contatos e a listagem dos mesmos foi relativamente simples. No entanto, ao tentar implementar o webhook, encontrei dificuldades. Como estou desenvolvendo localmente, o HubSpot não conseguiria disparar um evento para o meu endpoint. Considerei usar uma ferramenta de tunelamento para permitir o acesso ao meu localhost ou até mesmo subir a aplicação para a nuvem, mas acabei optando por não finalizar essa tarefa, pois não compreendi totalmente o funcionamento dos webhooks do HubSpot.

Pensando no futuro, acredito que seria interessante explorar outras formas de armazenamento do token, ao invés de mantê-lo no banco de dados como fiz inicialmente. Além disso, pretendo implementar o suporte completo a webhooks, melhorar o monitoramento da aplicação com logs apropriados (como `logger.info`, `logger.debug` e `logger.error`), e aprimorar a solução com melhores práticas de segurança e performance.
