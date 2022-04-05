create table articles
(
ID SERIAL NOT NULL PRIMARY KEY,
Name varchar(50) NOT NULL
);

create table balance
(
ID SERIAL NOT NULL PRIMARY KEY,
create_date timestamp(3) NOT NULL,
debit numeric(18, 2) NOT NULL
CONSTRAINT positive_debit_balance CHECK (debit >= 0),
credit numeric(18, 2) NOT NULL
CONSTRAINT positive_credit_balance CHECK (credit >= 0),
amount numeric(18, 2) NOT NULL
);

create table operations
(
ID SERIAL NOT NULL PRIMARY KEY,
article_id integer UNIQUE NOT NULL,
FOREIGN KEY (article_id) references articles (id),
debit numeric(18, 2) NOT NULL
CONSTRAINT positive_debit_operations CHECK (debit >= 0),
credit numeric(18, 2) NOT NULL
CONSTRAINT positive_credit_operations CHECK (credit >= 0),
create_date timestamp(3) NOT NULL,
balance_id integer UNIQUE NOT NULL,
FOREIGN KEY (balance_id) references balance (id)
);


