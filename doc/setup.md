git init
git add -A
git commit -am "initial add"

## go set up your repo in git

git remote add github git@github.com:anonymoose/clog.git
git push -u github master

heroku create

       Creating arcane-castle-3222... done, stack is cedar
       http://arcane-castle-3222.herokuapp.com/ | git@heroku.com:arcane-castle-3222.git

       Git remote heroku added

heroku addons:add heroku-postgresql:dev
       Adding heroku-postgresql:dev on arcane-castle-3222... 
       done, v3 (free)
       Attached as HEROKU_POSTGRESQL_CYAN_URL
       Database has been created and is available
       ! This database is empty. If upgrading, you can transfer
       ! data from another database with pgbackups:restore.
       Use `heroku addons:docs heroku-postgresql` to view documentation.


heroku pg:psql HEROKU_POSTGRESQL_CYAN_URL

    psql (9.1.1, server 9.2.4)
    WARNING: psql version 9.1, server version 9.2.
             Some psql features might not work.
    SSL connection (cipher: DHE-RSA-AES256-SHA, bits: 256)
    Type "help" for help.
    
    db3fsmopgk3gqg=> \i doc/schema.sql
    
    psql:doc/schema.sql:10: NOTICE:  drop cascades to constraint blog_users_id_fkey on table blog
    DROP TABLE
    DROP TABLE
    DROP TABLE
    psql:doc/schema.sql:19: NOTICE:  CREATE TABLE / PRIMARY KEY will create implicit index "ring_session_pkey" for table "ring_session"
    CREATE TABLE
    psql:doc/schema.sql:35: NOTICE:  CREATE TABLE / PRIMARY KEY will create implicit index "users_pkey" for table "users"
    CREATE TABLE
    psql:doc/schema.sql:36: NOTICE:  ALTER TABLE / ADD UNIQUE will create implicit index "user_email" for table "users"
    ALTER TABLE
    INSERT 0 1
    psql:doc/schema.sql:50: NOTICE:  CREATE TABLE / PRIMARY KEY will create implicit index "blog_pkey" for table "blog"
    CREATE TABLE
    ALTER TABLE
    db3fsmopgk3gqg=> \q
    

git push -u heroku master     <-- Do this forever more.
