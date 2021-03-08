
INSERT INTO usuario(email, senha, data_criacao) VALUES ('vitor@email.com', '$2a$10$eACCYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORUYzfMQCbVBIhZ8tG', TIMESTAMP WITH TIME ZONE '2021-01-10T20:30:08.12345Z');
INSERT INTO usuario(email, senha, data_criacao) VALUES ('teste@email.com', '$2a$10$eACCYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORUYzfMQCbVBIhZ8tG', TIMESTAMP WITH TIME ZONE '2021-02-15T20:30:08.12345Z');

INSERT INTO categoria(nome, categoria_mae_id) VALUES ('teste', null);

INSERT INTO produto(nome, valor, quantidade, descricao, categoria_id, dono_id, data_criacao)
VALUES ('Produto Teste', 100, 200, 'desc', 1, 1, TIMESTAMP WITH TIME ZONE '2021-02-18T20:30:08.12345Z');


