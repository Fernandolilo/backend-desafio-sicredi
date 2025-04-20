# backend-desafio-sicredi


Desafio proposto por Sicredi.

Sistema de eleição, deve ser aberto a pauta ou agenda, ficar aberta por tempo determinado, após seu fechamento, será envidao ao banco de dados todos os votos. 

verificar se um ususario já executou seu voto, se votou nãopode votar novamente.

fechar a pauta automaticamente.

https://app.codecov.io/gh/Fernandolilo/backend-desafio-sicredi

para acesso da documentação da api: http://localhost:8000/api/v1/swagger-ui/index.html#

para acesso do kafka-ui  http://localhost:8081/

para acesso do adminer, um client de bd: http://localhost:8082

motor de base de dados: selecione o BD Postgres
o servidor: postgres
nome de utilizador: root
Senha: root
base de dados: votospopule 

para acesso ao Banco de dados via client Adminer, que é um client muito eficiente para acesso de bancos de dados como: 
Oracle (beta)
SqLite
MS SQL
Postgres
MySqs/ MariaDB

neste caso subim uma stack, com:  docker-compose up --build

facilita muito o desenvolvimento.


foi desenvolvida uma API spring boot, Java versão 17.
Banco de dados relacional: Postgres
Cluster de mensages: Kafka confluent
para acesso do UI do Kafka, ja deixei o link acima mas segue novamente: http://localhost:8081/

a responsabilidade do Kafka é apenas salvar na base de dados após o fechamento da sessão, que é gerenciado por tarefas dentro do spring com o @EnableSchedulinga

os votos ficam travados em uma mensagem dentro do conflutente do kafka, salvos em um volume, para se de repente ouver um problema com o container, e ele cair, não perder os votos, que estará dentro do volume. 

após finalizado a sessão, que por default de de 1 minuto, gerenciado pelo serviço dentro do spring, com um cron, finaliza a sessão, finalizando é salvo todos os votos.... 


temos endpoint de contagem de votos por sessão.

para que haja uma votação, 

1 criar um pauta.

http://localhost:8000/api/v1/agendas

```curl -X 'POST' \
  'http://localhost:8000/api/v1/agendas' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "description": "causa verde"
}'
{
  "id": "152aec6b-4d88-4eb9-8768-f34e16088c0c",
  "description": "causa verde"
}

2 salvar um assossiado para que possa votar:

http://localhost:8000/api/v1/associates

curl -X 'POST' \
  'http://localhost:8000/api/v1/associates' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "cpf": "12312312311",
  "nome": "fernando"
}'
{
  "id": null,
  "cpf": "12312312311",
  "nome": "fernando"
}

http://localhost:8000/api/sessions

curl -X 'POST' \
  'http://localhost:8000/api/v1/sessions' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "agenda": "causa verde"
}'

"17b59243-e118-45dc-a259-3222c1b4f6a7"


caso o associado tente votar novamente

curl -X 'POST' \
  'http://localhost:8000/api/v1/sessions/votedto' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "vote": "SIM",
  "id_session": "17b59243-e118-45dc-a259-3222c1b4f6a7",
  "id_associate": "6a791e80-4a36-4ce6-82bd-6953cf98eb02"
}'
Request URL
http://localhost:8000/api/v1/sessions/votedto
Server response
Code	Details
400	
Error: response status is 400

Response body
Download
{
  "errors": [
    "Associado já efetuou seu voto."
  ]
}


aqui esta o fluxo da api.  


2025-04-19T01:00:43.268Z  INFO 1 --- [api] [           main] o.a.k.c.t.i.KafkaMetricsCollector        : initializing Kafka metrics collector
2025-04-19T01:00:49.810Z  INFO 1 --- [api] [           main] o.a.kafka.common.utils.AppInfoParser     : Kafka version: 3.7.0
2025-04-19T01:00:49.811Z  INFO 1 --- [api] [           main] o.a.kafka.common.utils.AppInfoParser     : Kafka commitId: 2ae524ed625438c5
2025-04-19T01:00:49.813Z  INFO 1 --- [api] [           main] o.a.kafka.common.utils.AppInfoParser     : Kafka startTimeMs: 1745024449797
2025-04-19T01:00:49.863Z  INFO 1 --- [api] [           main] o.a.k.c.c.internals.LegacyKafkaConsumer  : [Consumer clientId=consumer-api-consumer-group-1, groupId=api-consumer-group] Subscribed to topic(s): order-vote-popule
2025-04-19T01:00:50.083Z  INFO 1 --- [api] [           main] com.systempro.sessao.SessaoApplication   : Started SessaoApplication in 90.339 seconds (process running for 94.008)
2025-04-19T01:00:52.608Z  WARN 1 --- [api] [ntainer#0-0-C-1] org.apache.kafka.clients.NetworkClient   : [Consumer clientId=consumer-api-consumer-group-1, groupId=api-consumer-group] Error while fetching metadata with correlation id 2 : {order-vote-popule=LEADER_NOT_AVAILABLE}
2025-04-19T01:00:52.615Z  INFO 1 --- [api] [ntainer#0-0-C-1] org.apache.kafka.clients.Metadata        : [Consumer clientId=consumer-api-consumer-group-1, groupId=api-consumer-group] Cluster ID: l_Yb7PmvQw2M3Uv8lnoTmg
2025-04-19T01:00:52.876Z  WARN 1 --- [api] [ntainer#0-0-C-1] org.apache.kafka.clients.NetworkClient   : [Consumer clientId=consumer-api-consumer-group-1, groupId=api-consumer-group] Error while fetching metadata with correlation id 5 : {order-vote-popule=LEADER_NOT_AVAILABLE}
2025-04-19T01:00:53.233Z  WARN 1 --- [api] [ntainer#0-0-C-1] org.apache.kafka.clients.NetworkClient   : [Consumer clientId=consumer-api-consumer-group-1, groupId=api-consumer-group] Error while fetching metadata with correlation id 7 : {order-vote-popule=LEADER_NOT_AVAILABLE}
Hibernate:
    select
        s1_0.id,
        s1_0.id_agenda,
        s1_0.fim,
        s1_0.inicio,
        s1_0.status
    from
        session s1_0
    where
        s1_0.status=?
Hibernate:
    select
        a1_0.id,
        a1_0.description
    from
        agenda a1_0
    where
        a1_0.id=?
Nenhum voto pendente encontrado.
Hibernate:
    select
        s1_0.id,
        s1_0.id_agenda,
        s1_0.fim,
        s1_0.inicio,
        s1_0.status
    from
        session s1_0
    where
        s1_0.status=?
AGENDAMENTO DE TAREFAS FUNCIONANDO COM SUCESSO
2025-04-19T01:01:02.274Z  INFO 1 --- [api] [ntainer#0-0-C-1] o.a.k.c.c.internals.ConsumerCoordinator  : [Consumer clientId=consumer-api-consumer-group-1, groupId=api-consumer-group] Discovered group coordinator kafka:9092 (id: 2147483646 rack: null)
2025-04-19T01:01:02.303Z  INFO 1 --- [api] [ntainer#0-0-C-1] o.a.k.c.c.internals.ConsumerCoordinator  : [Consumer clientId=consumer-api-consumer-group-1, groupId=api-consumer-group] (Re-)joining group
2025-04-19T01:01:02.412Z  INFO 1 --- [api] [ntainer#0-0-C-1] o.a.k.c.c.internals.ConsumerCoordinator  : [Consumer clientId=consumer-api-consumer-group-1, groupId=api-consumer-group] Request joining group due to: need to re-join with the given member-id: consumer-api-consumer-group-1-888fe7b5-b27c-48f6-a34a-cef54e46fd2e
2025-04-19T01:01:02.413Z  INFO 1 --- [api] [ntainer#0-0-C-1] o.a.k.c.c.internals.ConsumerCoordinator  : [Consumer clientId=consumer-api-consumer-group-1, groupId=api-consumer-group] (Re-)joining group
2025-04-19T01:01:05.551Z  INFO 1 --- [api] [ntainer#0-0-C-1] o.a.k.c.c.internals.ConsumerCoordinator  : [Consumer clientId=consumer-api-consumer-group-1, groupId=api-consumer-group] Successfully joined group with generation Generation{generationId=1, memberId='consumer-api-consumer-group-1-888fe7b5-b27c-48f6-a34a-cef54e46fd2e', protocol='range'}
2025-04-19T01:01:05.648Z  INFO 1 --- [api] [ntainer#0-0-C-1] o.a.k.c.c.internals.ConsumerCoordinator  : [Consumer clientId=consumer-api-consumer-group-1, groupId=api-consumer-group] Finished assignment for group at generation 1: {consumer-api-consumer-group-1-888fe7b5-b27c-48f6-a34a-cef54e46fd2e=Assignment(partitions=[order-vote-popule-0])}
2025-04-19T01:01:05.981Z  INFO 1 --- [api] [ntainer#0-0-C-1] o.a.k.c.c.internals.ConsumerCoordinator  : [Consumer clientId=consumer-api-consumer-group-1, groupId=api-consumer-group] Successfully synced group in generation Generation{generationId=1, memberId='consumer-api-consumer-group-1-888fe7b5-b27c-48f6-a34a-cef54e46fd2e', protocol='range'}
2025-04-19T01:01:05.986Z  INFO 1 --- [api] [ntainer#0-0-C-1] o.a.k.c.c.internals.ConsumerCoordinator  : [Consumer clientId=consumer-api-consumer-group-1, groupId=api-consumer-group] Notifying assignor about the new Assignment(partitions=[order-vote-popule-0])
2025-04-19T01:01:06.025Z  INFO 1 --- [api] [ntainer#0-0-C-1] k.c.c.i.ConsumerRebalanceListenerInvoker : [Consumer clientId=consumer-api-consumer-group-1, groupId=api-consumer-group] Adding newly assigned partitions: order-vote-popule-0
2025-04-19T01:01:06.127Z  INFO 1 --- [api] [ntainer#0-0-C-1] o.a.k.c.c.internals.ConsumerCoordinator  : [Consumer clientId=consumer-api-consumer-group-1, groupId=api-consumer-group] Found no committed offset for partition order-vote-popule-0
2025-04-19T01:01:06.219Z  INFO 1 --- [api] [ntainer#0-0-C-1] o.a.k.c.c.internals.SubscriptionState    : [Consumer clientId=consumer-api-consumer-group-1, groupId=api-consumer-group] Resetting offset for partition order-vote-popule-0 to position FetchPosition{offset=0, offsetEpoch=Optional.empty, currentLeader=LeaderAndEpoch{leader=Optional[kafka:9092 (id: 1 rack: null)], epoch=0}}.
2025-04-19T01:01:06.228Z  INFO 1 --- [api] [ntainer#0-0-C-1] o.s.k.l.KafkaMessageListenerContainer    : api-consumer-group: partitions assigned: [order-vote-popule-0]
Hibernate:
    select
        s1_0.id,
        s1_0.id_agenda,
        s1_0.fim,
        s1_0.inicio,
        s1_0.status
    from
        session s1_0
    where
        s1_0.status=?
Hibernate:
    select
        s1_0.id,
        s1_0.id_agenda,
        s1_0.fim,
        s1_0.inicio,
        s1_0.status
    from
        session s1_0
    where
        s1_0.status=?
Hibernate:
    select
        a1_0.id,
        a1_0.description
    from
        agenda a1_0
    where
        a1_0.id=?
Nenhum voto pendente encontrado.
AGENDAMENTO DE TAREFAS FUNCIONANDO COM SUCESSO
AGENDAMENTO DE TAREFAS FUNCIONANDO COM SUCESSO
Hibernate:
    select
        s1_0.id,
        s1_0.id_agenda,
        s1_0.fim,
        s1_0.inicio,
        s1_0.status
    from
        session s1_0
    where
        s1_0.status=?
Hibernate:
    select
        a1_0.id,
        a1_0.description
    from
        agenda a1_0
    where
        a1_0.id=?
Nenhum voto pendente encontrado.
Hibernate:
    select
        s1_0.id,
        s1_0.id_agenda,
        s1_0.fim,
        s1_0.inicio,
        s1_0.status
    from
        session s1_0
    where
        s1_0.status=?
AGENDAMENTO DE TAREFAS FUNCIONANDO COM SUCESSO
Hibernate:
    select
        s1_0.id,
        s1_0.id_agenda,
        s1_0.fim,
        s1_0.inicio,
        s1_0.status
    from
        session s1_0
    where
        s1_0.status=?
Hibernate:
    select
        s1_0.id,
        s1_0.id_agenda,
        s1_0.fim,
        s1_0.inicio,
        s1_0.status
    from
        session s1_0
    where
        s1_0.status=?
Hibernate:
    select
        a1_0.id,
        a1_0.description
    from
        agenda a1_0
    where
        a1_0.id=?
Nenhum voto pendente encontrado.
Hibernate:
    select
        s1_0.id,
        s1_0.id_agenda,
        s1_0.fim,
        s1_0.inicio,
        s1_0.status
    from
        session s1_0
    where
        s1_0.status=?
AGENDAMENTO DE TAREFAS FUNCIONANDO COM SUCESSO
Hibernate:
    select
        s1_0.id,
        s1_0.id_agenda,
        s1_0.fim,
        s1_0.inicio,
        s1_0.status
    from
        session s1_0
    where
        s1_0.status=?
Hibernate:
    select
        a1_0.id,
        a1_0.description
    from
        agenda a1_0
    where
        a1_0.id=?
Nenhum voto pendente encontrado.
AGENDAMENTO DE TAREFAS FUNCIONANDO COM SUCESSO
Hibernate:
    select
        s1_0.id,
        s1_0.id_agenda,
        s1_0.fim,
        s1_0.inicio,
        s1_0.status
    from
        session s1_0
    where
        s1_0.status=?
Hibernate:
    select
        s1_0.id,
        s1_0.id_agenda,
        s1_0.fim,
        s1_0.inicio,
        s1_0.status
    from
        session s1_0
    where
        s1_0.status=?
Hibernate:
    select
        a1_0.id,
        a1_0.description
    from
        agenda a1_0
    where
        a1_0.id=?
Nenhum voto pendente encontrado.
Hibernate:
    select
        s1_0.id,
        s1_0.id_agenda,
        s1_0.fim,
        s1_0.inicio,
        s1_0.status
    from
        session s1_0
    where
        s1_0.status=?
AGENDAMENTO DE TAREFAS FUNCIONANDO COM SUCESSO
Hibernate:
    select
        s1_0.id,
        s1_0.id_agenda,
        s1_0.fim,
        s1_0.inicio,
        s1_0.status
    from
        session s1_0
    where
        s1_0.status=?
Hibernate:
    select
        a1_0.id,
        a1_0.description
    from
        agenda a1_0
    where
        a1_0.id=?
Nenhum voto pendente encontrado.
AGENDAMENTO DE TAREFAS FUNCIONANDO COM SUCESSO
Hibernate:
    select
        s1_0.id,
        s1_0.id_agenda,
        s1_0.fim,
        s1_0.inicio,
        s1_0.status
    from
        session s1_0
    where
        s1_0.status=?
Hibernate:
    select
        a1_0.id,
        a1_0.description
    from
        agenda a1_0
    where
        a1_0.id=?
Nenhum voto pendente encontrado.
Hibernate:
    select
        s1_0.id,
        s1_0.id_agenda,
        s1_0.fim,
        s1_0.inicio,
        s1_0.status
    from
        session s1_0
    where
        s1_0.status=?
Hibernate:
    select
        s1_0.id,
        s1_0.id_agenda,
        s1_0.fim,
        s1_0.inicio,
        s1_0.status
    from
        session s1_0
    where
        s1_0.status=?
Hibernate:
    select
        a1_0.id,
        a1_0.description
    from
        agenda a1_0
    where
        a1_0.id=?
Nenhum voto pendente encontrado.
AGENDAMENTO DE TAREFAS FUNCIONANDO COM SUCESSO
Hibernate:
    select
        s1_0.id,
        s1_0.id_agenda,
        s1_0.fim,
        s1_0.inicio,
        s1_0.status
    from
        session s1_0
    where
        s1_0.status=?
2025-04-19T01:09:52.873Z  INFO 1 --- [api] [ntainer#0-0-C-1] org.apache.kafka.clients.NetworkClient   : [Consumer clientId=consumer-api-consumer-group-1, groupId=api-consumer-group] Node -1 disconnected.
AGENDAMENTO DE TAREFAS FUNCIONANDO COM SUCESSO
Hibernate:
    select
        s1_0.id,
        s1_0.id_agenda,
        s1_0.fim,
        s1_0.inicio,
        s1_0.status
    from
        session s1_0
    where
        s1_0.status=?
Hibernate:
    select
        s1_0.id,
        s1_0.id_agenda,
        s1_0.fim,
        s1_0.inicio,
        s1_0.status
    from
        session s1_0
    where
        s1_0.status=?
Hibernate:
    select
        a1_0.id,
        a1_0.description
    from
        agenda a1_0
    where
        a1_0.id=?
Nenhum voto pendente encontrado.
AGENDAMENTO DE TAREFAS FUNCIONANDO COM SUCESSO
Hibernate:
    select
        s1_0.id,
        s1_0.id_agenda,
        s1_0.fim,
        s1_0.inicio,
        s1_0.status
    from
        session s1_0
    where
        s1_0.status=?
Hibernate:
    select
        s1_0.id,
        s1_0.id_agenda,
        s1_0.fim,
        s1_0.inicio,
        s1_0.status
    from
        session s1_0
    where
        s1_0.status=?
Hibernate:
    select
        a1_0.id,
        a1_0.description
    from
        agenda a1_0
    where
        a1_0.id=?
Nenhum voto pendente encontrado.
AGENDAMENTO DE TAREFAS FUNCIONANDO COM SUCESSO
Hibernate:
    select
        s1_0.id,
        s1_0.id_agenda,
        s1_0.fim,
        s1_0.inicio,
        s1_0.status
    from
        session s1_0
    where
        s1_0.status=?
Hibernate:
    select
        a1_0.id,
        a1_0.description
    from
        agenda a1_0
    where
        a1_0.id=?
Nenhum voto pendente encontrado.
Hibernate:
    select
        s1_0.id,
        s1_0.id_agenda,
        s1_0.fim,
        s1_0.inicio,
        s1_0.status
    from
        session s1_0
    where
        s1_0.status=?
AGENDAMENTO DE TAREFAS FUNCIONANDO COM SUCESSO
Hibernate:
    select
        s1_0.id,
        s1_0.id_agenda,
        s1_0.fim,
        s1_0.inicio,
        s1_0.status
    from
        session s1_0
    where
        s1_0.status=?
Hibernate:
    select
        s1_0.id,
        s1_0.id_agenda,
        s1_0.fim,
        s1_0.inicio,
        s1_0.status
    from
        session s1_0
    where
        s1_0.status=?
Hibernate:
    select
        a1_0.id,
        a1_0.description
    from
        agenda a1_0
    where
        a1_0.id=?
Nenhum voto pendente encontrado.
2025-04-19T01:13:26.534Z  INFO 1 --- [api] [nio-8000-exec-1] o.a.c.c.C.[Tomcat].[localhost].[/api]    : Initializing Spring DispatcherServlet 'dispatcherServlet'
2025-04-19T01:13:26.537Z  INFO 1 --- [api] [nio-8000-exec-1] o.s.web.servlet.DispatcherServlet        : Initializing Servlet 'dispatcherServlet'
2025-04-19T01:13:26.593Z  INFO 1 --- [api] [nio-8000-exec-1] o.s.web.servlet.DispatcherServlet        : Completed initialization in 55 ms
2025-04-19T01:13:29.922Z  INFO 1 --- [api] [nio-8000-exec-4] o.springdoc.api.AbstractOpenApiResource  : Init duration for springdoc-openapi is: 1953 ms
Hibernate:
    select
        s1_0.id,
        s1_0.id_agenda,
        s1_0.fim,
        s1_0.inicio,
        s1_0.status
    from
        session s1_0
    where
        s1_0.status=?
Hibernate:
    select
        a1_0.id,
        a1_0.description
    from
        agenda a1_0
    where
        a1_0.id=?
Nenhum voto pendente encontrado.
Hibernate:
    select
        s1_0.id,
        s1_0.id_agenda,
        s1_0.fim,
        s1_0.inicio,
        s1_0.status
    from
        session s1_0
    where
        s1_0.status=?
AGENDAMENTO DE TAREFAS FUNCIONANDO COM SUCESSO
Hibernate:
    insert
    into
        agenda
        (description, id)
    values
        (?, ?)
AGENDAMENTO DE TAREFAS FUNCIONANDO COM SUCESSO
Hibernate:
    select
        s1_0.id,
        s1_0.id_agenda,
        s1_0.fim,
        s1_0.inicio,
        s1_0.status
    from
        session s1_0
    where
        s1_0.status=?
Hibernate:
    select
        s1_0.id,
        s1_0.id_agenda,
        s1_0.fim,
        s1_0.inicio,
        s1_0.status
    from
        session s1_0
    where
        s1_0.status=?
Hibernate:
    select
        a1_0.id,
        a1_0.description
    from
        agenda a1_0
    where
        a1_0.id=?
Nenhum voto pendente encontrado.
Hibernate:
    select
        s1_0.id,
        s1_0.id_agenda,
        s1_0.fim,
        s1_0.inicio,
        s1_0.status
    from
        session s1_0
    where
        s1_0.status=?
Hibernate:
    select
        a1_0.id,
        a1_0.description
    from
        agenda a1_0
    where
        a1_0.id=?
Nenhum voto pendente encontrado.
Hibernate:
    select
        s1_0.id,
        s1_0.id_agenda,
        s1_0.fim,
        s1_0.inicio,
        s1_0.status
    from
        session s1_0
    where
        s1_0.status=?
AGENDAMENTO DE TAREFAS FUNCIONANDO COM SUCESSO
Hibernate:
    insert
    into
        associated
        (cpf, nome, id)
    values
        (?, ?, ?)
Hibernate:
    select
        s1_0.id,
        s1_0.id_agenda,
        s1_0.fim,
        s1_0.inicio,
        s1_0.status
    from
        session s1_0
    where
        s1_0.status=?
Hibernate:
    select
        a1_0.id,
        a1_0.description
    from
        agenda a1_0
    where
        a1_0.id=?
Nenhum voto pendente encontrado.
AGENDAMENTO DE TAREFAS FUNCIONANDO COM SUCESSO
Hibernate:
    select
        s1_0.id,
        s1_0.id_agenda,
        s1_0.fim,
        s1_0.inicio,
        s1_0.status
    from
        session s1_0
    where
        s1_0.status=?
AGENDAMENTO DE TAREFAS FUNCIONANDO COM SUCESSO
Hibernate:
    select
        s1_0.id,
        s1_0.id_agenda,
        s1_0.fim,
        s1_0.inicio,
        s1_0.status
    from
        session s1_0
    where
        s1_0.status=?
Hibernate:
    select
        a1_0.id,
        a1_0.description
    from
        agenda a1_0
    where
        a1_0.id=?
Nenhum voto pendente encontrado.
Hibernate:
    select
        s1_0.id,
        s1_0.id_agenda,
        s1_0.fim,
        s1_0.inicio,
        s1_0.status
    from
        session s1_0
    where
        s1_0.status=?
Hibernate:
    select
        s1_0.id,
        s1_0.id_agenda,
        s1_0.fim,
        s1_0.inicio,
        s1_0.status
    from
        session s1_0
    where
        s1_0.status=?
Hibernate:
    select
        a1_0.id,
        a1_0.description
    from
        agenda a1_0
    where
        a1_0.id=?
Nenhum voto pendente encontrado.
Hibernate:
    select
        s1_0.id,
        s1_0.id_agenda,
        s1_0.fim,
        s1_0.inicio,
        s1_0.status
    from
        session s1_0
    where
        s1_0.status=?
AGENDAMENTO DE TAREFAS FUNCIONANDO COM SUCESSO
Hibernate:
    select
        a1_0.id,
        a1_0.description
    from
        agenda a1_0
    where
        a1_0.description=?
2025-04-19T01:19:52.711Z  INFO 1 --- [api] [ntainer#0-0-C-1] org.apache.kafka.clients.NetworkClient   : [Consumer clientId=consumer-api-consumer-group-1, groupId=api-consumer-group] Node -1 disconnected.
AGENDAMENTO DE TAREFAS FUNCIONANDO COM SUCESSO
Hibernate:
    select
        s1_0.id,
        s1_0.id_agenda,
        s1_0.fim,
        s1_0.inicio,
        s1_0.status
    from
        session s1_0
    where
        s1_0.status=?
Hibernate:
    select
        a1_0.id,
        a1_0.description
    from
        agenda a1_0
    where
        a1_0.id=?
Nenhum voto pendente encontrado.
Hibernate:
    select
        s1_0.id,
        s1_0.id_agenda,
        s1_0.fim,
        s1_0.inicio,
        s1_0.status
    from
        session s1_0
    where
        s1_0.status=?
Hibernate:
    select
        a1_0.id,
        a1_0.description
    from
        agenda a1_0
    where
        a1_0.description=?
Hibernate:
    insert
    into
        session
        (id_agenda, fim, inicio, status, id)
    values
        (?, ?, ?, ?, ?)
Hibernate:
    select
        s1_0.id,
        a1_0.id,
        a1_0.description,
        s1_0.fim,
        s1_0.inicio,
        s1_0.status
    from
        session s1_0
    left join
        agenda a1_0
            on a1_0.id=s1_0.id_agenda
    where
        s1_0.id=?
Hibernate:
    select
        a1_0.id,
        a1_0.cpf,
        a1_0.nome
    from
        associated a1_0
    where
        a1_0.id=?
Hibernate:
    select
        s1_0.id,
        a1_0.id,
        a1_0.description,
        s1_0.fim,
        s1_0.inicio,
        s1_0.status
    from
        session s1_0
    left join
        agenda a1_0
            on a1_0.id=s1_0.id_agenda
    where
        s1_0.id=?
Hibernate:
    select
        a1_0.id,
        a1_0.cpf,
        a1_0.nome
    from
        associated a1_0
    where
        a1_0.id=?
AGENDAMENTO DE TAREFAS FUNCIONANDO COM SUCESSO
Hibernate:
    select
        s1_0.id,
        s1_0.id_agenda,
        s1_0.fim,
        s1_0.inicio,
        s1_0.status
    from
        session s1_0
    where
        s1_0.status=?
Hibernate:
    select
        a1_0.id,
        a1_0.description
    from
        agenda a1_0
    where
        a1_0.id=?
Nenhum voto pendente encontrado.
Hibernate:
    select
        s1_0.id,
        s1_0.id_agenda,
        s1_0.fim,
        s1_0.inicio,
        s1_0.status
    from
        session s1_0
    where
        s1_0.status=?
Hibernate:
    select
        a1_0.id,
        a1_0.description
    from
        agenda a1_0
    where
        a1_0.id=?
AGENDAMENTO DE TAREFAS FUNCIONANDO COM SUCESSO
Hibernate:
    select
        s1_0.id,
        s1_0.id_agenda,
        s1_0.fim,
        s1_0.inicio,
        s1_0.status
    from
        session s1_0
    where
        s1_0.status=?
Hibernate:
    select
        a1_0.id,
        a1_0.description
    from
        agenda a1_0
    where
        a1_0.id=?
Nenhum voto pendente encontrado.
Hibernate:
    select
        s1_0.id,
        s1_0.id_agenda,
        s1_0.fim,
        s1_0.inicio,
        s1_0.status
    from
        session s1_0
    where
        s1_0.status=?
Hibernate:
    select
        a1_0.id,
        a1_0.description
    from
        agenda a1_0
    where
        a1_0.id=?
Hibernate:
    select
        s1_0.id,
        a1_0.id,
        a1_0.description,
        s1_0.fim,
        s1_0.inicio,
        s1_0.status
    from
        session s1_0
    left join
        agenda a1_0
            on a1_0.id=s1_0.id_agenda
    where
        s1_0.id=?
Hibernate:
    update
        session
    set
        id_agenda=?,
        fim=?,
        inicio=?,
        status=?
    where
        id=?
Hibernate:
    select
        s1_0.id,
        s1_0.id_agenda,
        s1_0.fim,
        s1_0.inicio,
        s1_0.status
    from
        session s1_0
    where
        s1_0.status=?
AGENDAMENTO DE TAREFAS FUNCIONANDO COM SUCESSO
Hibernate:
    select
        s1_0.id,
        s1_0.id_agenda,
        s1_0.fim,
        s1_0.inicio,
        s1_0.status
    from
        session s1_0
    where
        s1_0.status=?
Hibernate:
    select
        a1_0.id,
        a1_0.description
    from
        agenda a1_0
    where
        a1_0.id=?
Hibernate:
    select
        a1_0.id,
        a1_0.description
    from
        agenda a1_0
    where
        a1_0.id=?
Nenhum voto pendente encontrado.
AGENDAMENTO DE TAREFAS FUNCIONANDO COM SUCESSO
Hibernate:
    select
        s1_0.id,
        s1_0.id_agenda,
        s1_0.fim,
        s1_0.inicio,
        s1_0.status
    from
        session s1_0
    where
        s1_0.status=?
Hibernate:
    select
        s1_0.id,
        s1_0.id_agenda,
        s1_0.fim,
        s1_0.inicio,
        s1_0.status
    from
        session s1_0
    where
        s1_0.status=?
Hibernate:
    select
        a1_0.id,
        a1_0.description
    from
        agenda a1_0
    where
        a1_0.id=?
Hibernate:
    select
        a1_0.id,
        a1_0.description
    from
        agenda a1_0
    where
        a1_0.id=?
Nenhum voto pendente encontrado.
Hibernate:
    select
        s1_0.id,
        s1_0.id_agenda,
        s1_0.fim,
        s1_0.inicio,
        s1_0.status
    from
        session s1_0
    where
        s1_0.status=?
AGENDAMENTO DE TAREFAS FUNCIONANDO COM SUCESSO
Hibernate:
    select
        s1_0.id,
        s1_0.id_agenda,
        s1_0.fim,
        s1_0.inicio,
        s1_0.status
    from
        session s1_0
    where
        s1_0.status=?
Hibernate:
    select
        a1_0.id,
        a1_0.description
    from
        agenda a1_0
    where
        a1_0.id=?
Hibernate:
    select
        a1_0.id,
        a1_0.description
    from
        agenda a1_0
    where
        a1_0.id=?
Nenhum voto pendente encontrado.
AGENDAMENTO DE TAREFAS FUNCIONANDO COM SUCESSO
Hibernate:
    select
        s1_0.id,
        s1_0.id_agenda,
        s1_0.fim,
        s1_0.inicio,
        s1_0.status
    from
        session s1_0
    where
        s1_0.status=?
Hibernate:
    select
        s1_0.id,
        s1_0.id_agenda,
        s1_0.fim,
        s1_0.inicio,
        s1_0.status
    from
        session s1_0
    where
        s1_0.status=?
Hibernate:
    select
        a1_0.id,
        a1_0.description
    from
        agenda a1_0
    where
        a1_0.id=?
Hibernate:
    select
        a1_0.id,
        a1_0.description
    from
        agenda a1_0
    where
        a1_0.id=?
Nenhum voto pendente encontrado. ```