-- INSERT INGREDIENTES
INSERT INTO ingrediente (nome, valor) VALUES ('Alface', 0.40);
INSERT INTO ingrediente (nome, valor) VALUES ('Bacon', 2.00);
INSERT INTO ingrediente (nome, valor) VALUES ('Hambúrguer de carne', 3.00);
INSERT INTO ingrediente (nome, valor) VALUES ('Ovo', 0.80);
INSERT INTO ingrediente (nome, valor) VALUES ('Queijo', 1.50);

-- INSERT LANCHES
INSERT INTO lanche (nome) VALUES ('X-Bacon');
INSERT INTO lanche (nome) VALUES ('X-Burger');
INSERT INTO lanche (nome) VALUES ('X-Egg');
INSERT INTO lanche (nome) VALUES ('X-Egg Bacon');

-- INSERT LANCHES_INGREDIENTES
-- X-Bacon: Bacon, Hambúrguer de carne, Queijo
INSERT INTO lanche_ingrediente (lanche_id, ingrediente_id, quantidade) VALUES (1, 2, 1);
INSERT INTO lanche_ingrediente (lanche_id, ingrediente_id, quantidade) VALUES (1, 3, 1);
INSERT INTO lanche_ingrediente (lanche_id, ingrediente_id, quantidade) VALUES (1, 5, 1);

-- X-Burger: Hambúrguer de carne, Queijo
INSERT INTO lanche_ingrediente (lanche_id, ingrediente_id, quantidade) VALUES (2, 3, 1);
INSERT INTO lanche_ingrediente (lanche_id, ingrediente_id, quantidade) VALUES (2, 5, 1);

-- X-Egg: Ovo, Hambúrguer de carne, Queijo
INSERT INTO lanche_ingrediente (lanche_id, ingrediente_id, quantidade) VALUES (3, 4, 1);
INSERT INTO lanche_ingrediente (lanche_id, ingrediente_id, quantidade) VALUES (3, 3, 1);
INSERT INTO lanche_ingrediente (lanche_id, ingrediente_id, quantidade) VALUES (3, 5, 1);

-- X-Egg Bacon: Ovo, Bacon, Hambúrguer de carne, Queijo
INSERT INTO lanche_ingrediente (lanche_id, ingrediente_id, quantidade) VALUES (4, 4, 1);
INSERT INTO lanche_ingrediente (lanche_id, ingrediente_id, quantidade) VALUES (4, 2, 1);
INSERT INTO lanche_ingrediente (lanche_id, ingrediente_id, quantidade) VALUES (4, 3, 1);
INSERT INTO lanche_ingrediente (lanche_id, ingrediente_id, quantidade) VALUES (4, 5, 1);

-- INSERT PROMOCOES
INSERT INTO promocao (nome, descricao)
    VALUES ('LIGHT', 'Se o lanche tem alface e não tem bacon, ganha 10% de desconto.');

INSERT INTO promocao (nome, descricao)
    VALUES ('MUITACARNE', 'A cada 3 porções de carne o cliente só paga 2. Se o lanche tiver 6 porções, o cliente pagará 4. Assim por diante...');

INSERT INTO promocao (nome, descricao)
    VALUES ('MUITOQUEIJO', 'A cada 3 porções de queijo o cliente só paga 2. Se o lanche tiver 6 porções, o cliente pagará 4. Assim por diante...');
