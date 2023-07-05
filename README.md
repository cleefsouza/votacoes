# Votações API :heavy_check_mark:
> Solução back-end para gerenciar sessões de votação

## Visão Geral :speech_balloon:
No cooperativismo, cada associado possui um voto e as decisões são tomadas em assembleias, por votação. A partir disso, foi desenvolvido uma simples api para gerenciar essas sessões de votação. A solução é executada na nuvem e promover algumas funcionalidades, como cadastrar uma nova pauta, iniciar uma sessão de votação e consultar o resultado final da votação.

**URL de acesso**: [votacoes-api.up.railway.app](https://votacoes-api.up.railway.app)  
Obs: endpoints descritos mais abaixo no readme

## Preparação Ambiente :computer:
- JDK 11
- Maven
- Cliente REST API (Postman, Insomnia, etc)
- PgAdmin, Dbeaver (Postgres)
- Docker
- IDE (IntelliJ, Eclipse, etc)

## Instalação :minidisc:
- Clone o projeto
``` shell
git clone git@github.com:cleefsouza/votacoes.git
```
- Baixe as dependencias com o maven
``` shell
mvn install
```
- Configure o `application.properties` com suas configurações locais ou em nuvem
- Para subir container do rabbitmq local `src/main/resources/docker-compose.yml`
``` shell
docker-compose up -d
```
- Execute o projeto
``` shell
mvn spring-boot:run
```
- Execute os testes da aplicação
``` shell
mvn test
```

## Utilização :airplane:
Crie suas requests no cliente rest de sua preferencia. **Para fins de exercício**, a segurança dos recursos é abstraída e qualquer chamada para os mesmos é considerada como autorizada.

## Endpoints :pushpin:

### Cadastrar Pauta
> Endpoint para cadastrar uma nova pauta.
- **URL**: `/pautas/cadastrar`
- **Método**: POST
- **Corpo da Requisição**: JSON

Exemplo de Requisição:
``` bash
POST /pautas/cadastrar
```
Exemplo de corpo da requisição:
``` json
{
  "titulo": "Título da Pauta",
  "descricao": "Descrição da Pauta"
}
```
Respostas
|Código|Descrição|Corpo da Resposta|
|------|---------|-----------------|
|201|Pauta cadastrada com sucesso|Objeto JSON da pauta cadastrada|
|409|Requisição com conflito|Já existe uma pauta com esse título em uso|

### Buscar Pautas
> Endpoint para buscar as pautas cadastradas.
- **URL**: `/pautas`
- **Método**: GET

Parâmetros de Paginação (opcionais)
|Parâmetro|Valor Padrão|Descrição|
|---------|------------|---------|
|page|	0	|Número da página|
|size|	5	|Tamanho da página|
|sort|	id	|Ordenação dos resultados|
|direction|	DESC	|Direção da ordenação (ASC/DESC)|

Exemplo de Requisição:
``` bash
GET /pautas?page=0&size=10&sort=id&direction=DESC
```
Respostas
|Código|Descrição|Corpo da Resposta|
|------|---------|-----------------|
|200|Sucesso|Lista de objetos JSON das pautas|
|400|Requisição inválida|Mensagem de erro|

### Iniciar Sessão
> Endpoint para iniciar uma nova sessão de votação para uma determinada pauta.
- **URL**: `/pauta/{pautaId}/sessao/iniciar`
- **Método**: POST
- **Corpo da Requisição**: JSON

Exemplo de Requisição:
``` bash
POST /pauta/ad39f92a-1f6f-47bb-b96f-c9af252fac6c/sessao/iniciar
```
Exemplo de corpo da requisição:
``` json
{
  "duracao": 5, (em minutos)
  "encerrada": false 
}
```
Respostas
|Código|Descrição|Corpo da Resposta|
|------|---------|-----------------|
|201|Sessão cadastrada com sucesso|Objeto JSON da sessão cadastrada|
|400|Requisição inválida|Mensagem de erro|
|404|Recurso não encontrado|Pauta não encontrada|
|409|Requisição com conflito|Já existe uma sessão para essa pauta|

### Cadastrar Associado
> Endpoint para cadastrar um novo associado.
- **URL**: `/associados/cadastrar`
- **Método**: POST
- **Corpo da Requisição**: JSON

Exemplo de Requisição:
``` bash
POST /associado/cadastrar
```
Exemplo de corpo da requisição:
``` json
{
  "nome": "Nome do Associado",
  "cpf": "123.456.789-00"
}
```
Respostas
|Código|Descrição|Corpo da Resposta|
|------|---------|-----------------|
|201|Associado cadastrado com sucesso|Objeto JSON do associado cadastrado|
|400|Requisição inválida|Mensagem de erro|
|409|Requisição com conflito|Já existe um associado com esse CPF|

### Buscar Associados
> Endpoint para buscar uma lista paginada de associados cadastrados.
- **URL**: `/associado`
- **Método**: GET

Parâmetros de Paginação (opcionais)
|Parâmetro|Valor Padrão|Descrição|
|---------|------------|---------|
|page|	0	|Número da página|
|size|	5	|Tamanho da página|
|sort|	id	|Ordenação dos resultados|
|direction|	DESC	|Direção da ordenação (ASC/DESC)|

Exemplo de Requisição:
``` bash
GET /associado?page=0&size=10&sort=id&direction=DESC
```
Respostas
|Código|Descrição|Corpo da Resposta|
|------|---------|-----------------|
|200|Sucesso|Lista de objetos JSON dos associados|
|400|Requisição inválida|Mensagem de erro|

### Votar em uma Pauta
> Endpoint para registrar um voto em uma pauta.
- **URL**: `/pauta/{pautaId}/votar`
- **Método**: POST
- **Corpo da Requisição**: JSON

Exemplo de Requisição:
``` bash
POST /pauta/ad39f92a-1f6f-47bb-b96f-c9af252fac6c/votar
```
Exemplo de corpo da requisição:
``` json
{
  "voto": "Sim",
  "cpf": "123.456.789-00",
}
```
Respostas
|Código|Descrição|Corpo da Resposta|
|------|---------|-----------------|
|200|Voto registrado com sucesso|Objeto JSON do voto registrado|
|400|Requisição inválida|Sessão encerrada|
|403|Requisição negada|Associado já votou nessa pauta|
|404|Recurso não encontrado|Pauta não encontrada|
|404|Recurso não encontrado|Associado não encontrado|

### Buscar Resultado da Votação
> Endpoint para buscar o resultado da votação de uma pauta.
- **URL**: `/pauta/{pautaId}/resultado`
- **Método**: GET

Exemplo de Requisição:
``` bash
GET /pauta/ad39f92a-1f6f-47bb-b96f-c9af252fac6c/resultado
```
Respostas
|Código|Descrição|Corpo da Resposta|
|------|---------|-----------------|
|200|Sucesso|Objeto JSON com o resultado da votação|
|404|Recurso não encontrado|Pauta não encontrada|
|404|Recurso não encontrado|Não a sessão de votação para essa pauta|
|409|Requisição com conflito|Sessão em andamento|

### Exemplos :anger:
Cadastre seus associados, crie uma pauta e inicie uma sessão de votação. Após o tempo de duração da sessão encerrar, a votação sera finalizada e a API enviará uma mensagem com o resultado para uma fila RabbitMQ, configure um consumidor na sua aplicação para receber as mensagens e processar os resultados. 

### Autor :man_technologist:
Aryosvalldo Cleef ─ [linkedin](https://www.linkedin.com/in/aryosvalldo-cleef/) ─ [@cleefsouza](https://github.com/cleefsouza)

### Meta :dart:
Made with :heart: by **Cleef Souza**
