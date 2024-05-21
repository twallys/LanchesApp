$(document).ready(function() {
    loadLanches();
    loadIngredientes();
    fetchOrders();

    function loadLanches() {
        $.get('http://localhost:8080/api/v1/lanches', function(data) {
            $('#lancheSelect').empty();

            if (data.data.length > 0) {
                data.data.forEach(function(lanche) {
                    $('#lancheSelect').append(new Option(lanche.nome, lanche.id));
                });
            } else {
                $('#lancheSelect').append(new Option('Nenhum lanche disponível', ''));
            }
        }).fail(function() {
            $('#lancheSelect').empty();
            $('#lancheSelect').append(new Option('Falha ao carregar lanches', ''));
        });
    }

    function loadIngredientes() {
        $.get('http://localhost:8080/api/v1/ingredientes', function(data) {
            $('#ingredientesContainer').empty();
            data.data.forEach(ing => {
                $('#ingredientesContainer').append(`
                    <div class="ingrediente">
                        <label>
                            <input type="checkbox" class="ingrediente-checkbox" value="${ing.id}">
                            ${ing.nome} ($${ing.valor.toFixed(2)})
                        </label>
                        <input type="number" min="0" value="0" class="ingrediente-quantidade" id="qty-${ing.id}" disabled>
                    </div>
                `);
            });

            $('.ingrediente-checkbox').change(function() {
                const checked = $(this).is(':checked');
                $(`#qty-${$(this).val()}`).prop('disabled', !checked);
                if (!checked) {
                    $(`#qty-${$(this).val()}`).val(0);
                }
            });
        });
    }

    $('#enviarPedidoBtn').on('click', function(e) {
        e.preventDefault();
        enviarPedido();
    });

    window.enviarPedido = function() {
        const cliente = $('#clienteNome').val();
        const lancheId = $('#lancheSelect').val();
        let adicionais = [];

        $('.ingrediente-checkbox:checked').each(function() {
            const id = $(this).val();
            const quantidade = parseInt($(`#qty-${id}`).val());
            if (quantidade > 0) {
                adicionais.push({ id: id, quantidade: quantidade });
            }
        });

        const pedidoData = {
            cliente: cliente,
            itens: [
                {
                    quantidade: 1,
                    lanche: {
                        id: lancheId
                    },
                    adicionais: adicionais
                }
            ]
        };

        $.ajax({
            url: 'http://localhost:8080/api/v1/pedidos',
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(pedidoData),
            success: function(response) {
                alert('Pedido enviado com sucesso!');
                fetchOrders();
            },
            error: function() {
                alert('Erro ao enviar pedido.');
            }
        });
    };

    $('#atualizarPedidosBtn').on('click', function(e) {
        e.preventDefault();
        fetchOrders();
    });

    function fetchOrders() {
        try {
            $.get('http://localhost:8080/api/v1/pedidos', function(data) {
                const table = document.getElementById('ordersTable');
                while (table.rows.length > 1) {
                    table.deleteRow(1);
                }

                data.data.forEach(pedido => {
                    const row = table.insertRow(-1);
                    row.insertCell(0).textContent = pedido.id;
                    row.insertCell(1).textContent = pedido.cliente;
                    row.insertCell(2).textContent = pedido.total.toFixed(2);

                    const itemList = pedido.itens.map(item => `${item.lanche.nome} (Quantidade: ${item.quantidade})`).join('<br>');
                    row.insertCell(3).innerHTML = itemList;

                    const ingredientesLancheList = pedido.itens
                        .map(item => item.lanche.lancheIngredientes
                            .map(ing => `${ing.ingrediente.nome} (Quantidade: ${ing.quantidade}) (Valor: R$ ${ing.ingrediente.valor.toFixed(2)})`)
                            .join('<br>'))
                        .join('<br>');
                    row.insertCell(4).innerHTML = ingredientesLancheList;

                    const adicionaisList = pedido.itens
                        .map(item => item.itemPedidoIngredientes
                            .map(ing => `${ing.ingrediente.nome} (Quantidade: ${ing.quantidade}) (Valor: R$ ${ing.ingrediente.valor.toFixed(2)})`)
                            .join('<br>'))
                        .join('<br>');

                    row.insertCell(5).innerHTML = adicionaisList;

                    const promocao = pedido.itens.map(item => `${item.promocao ? item.promocao.nome: ''}`);
                    row.insertCell(6).textContent = promocao;
                });

            });
        } catch (error) {
            console.error('Erro ao buscar pedidos:', error);
            alert('Erro ao atualizar a tabela de pedidos.');
        }
    }

});
