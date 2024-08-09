CREATE TABLE transactions (
                              id BIGSERIAL PRIMARY KEY,
                              account_from BIGINT NOT NULL,
                              account_to BIGINT NOT NULL,
                              currency_shortname VARCHAR(3) NOT NULL,
                              sum NUMERIC(15, 2) NOT NULL,
                              expense_category VARCHAR(255) NOT NULL DEFAULT 'PRODUCT',
                              datetime TIMESTAMP WITH TIME ZONE NOT NULL,
                              limit_exceeded BOOLEAN DEFAULT FALSE
);

CREATE TABLE limits (
                        id BIGSERIAL PRIMARY KEY,
                        limit_sum NUMERIC(15, 2) NOT NULL,
                        limit_datetime TIMESTAMP WITH TIME ZONE NOT NULL,
                        limit_currency_shortname VARCHAR(3) NOT NULL,
                        expense_category VARCHAR(255) NOT NULL DEFAULT 'PRODUCT'
);

CREATE TABLE exchange_rate (
                               id BIGSERIAL PRIMARY KEY,
                               currency_pair VARCHAR(7) NOT NULL,
                               date DATE NOT NULL,
                               close_rate NUMERIC(15, 6) NOT NULL
);
