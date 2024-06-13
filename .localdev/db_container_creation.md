## Создание контейнера с базой данных PostgreSQL в Docker  

После установки приложения Docker в командной строке ввести команду для запуска контейнера с PostgreSQL (образ при необходимости загрузится автоматически) :  

    docker run -d --name bankdb -p 5432:5432 -e POSTGRES_USER=admin -e POSTGRES_DB=postgres -e POSTGRES_PASSWORD=11111111 -e PGDATA=/var/lib/postgresql/data/pgdata postgres:16.0-alpine3.18

где

   - `db` - имя контейнера;
   - `data` - имя volume.

Для проверки работы контейнера можно выполнить следующие команды:

   - переход к выполнению команд в запущенном контейнере:

    docker exec -it bankdb bash

   - переход в консоль PostgreSQL:
  
    psql -h localhost -U admin postgres
