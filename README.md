# Roteador, Cliente

Serão explicadas brevemente as funções de cada classe.

Roteador → Recebe uma entrada no formato “ numero_porta rede-destino/máscara/gateway/interface “ que definirá seu funcionamento. Após criado, ele ficará escutando na porta definida na entrada até receber um pacote.

Caso venha a receber um pacote, ele lerá o campo de mensagem do pacote, o qual carrega o IP destino (da rede que se quer chegar), o IP do roteador ( para o qual a mensagem está sendo enviada), o IP origem (de quem criou o pacote) e a mensagem em si.

Após lê-lo, ele verificará na sua tabela de roteamento o que fazer com o pacote (se descarta-o, se encaminha-o diretamente ou repassa para um próximo roteador, alterando e recriando corretamente a parte de mensagem do pacote).

Endereco → Possui um vetor de 4bytes que armazena um endereço na forma de bytes e uma String que armazena o endereço na forma de String. Esta classe realiza operações binárias de comparação, como a lógica de criar e armazenar a versão binária de um endereço na forma String.

Mascara → Além de fazer tudo que a classe Endereco faz (por extendê-la), guarda o tamanho da máscara (se é /24, /28, /20 etc) e implementa lógicas adicionais de criar o vetor de 4 bytes que guarda o valor da máscara (por ex, 255.255.255.255 na forma binária).

Um método que vale destacar é o “adicionarTamanhoMascara”. Este método recebe os bytes que vão sendo criados e adicionados ao vetor de 4 bytes e, por meio de um switch, vai incrementando a variável que guarda o tamanho da máscara.

Tal método foi criado para facilitar as comparações na tabela de roteamento, caso mais de duas linhas fossem compatíveis como endereço destino (a linha que “casar” com o IP destino do pacote e que tiver a máscara com o maior tamanho é para onde deve ser encaminhado o pacote).

LinhaTabela → Possui um Endereco que é o IP do roteador destino, uma Mascara, um Endereco que é para onde deve ser encaminhado o pacote, caso ele case com a operação de “E Binário” e um inteiro que diz a porta para onde deve ser encaminhado.

TabelaRoteamento → Possui diversas LinhaTabela’s. Cada LinhaTabela é um “rede-destino/máscara/gateway/interface” que foi passado como parâmetro quando o Roteador foi criado.

Responsável por pegar os parâmetros passados pelo Roteador e criar a LinhaRoteamento. Seu método “verificarIP” recebe como parâmetro o IPdestino do pacote e realiza comparações para verificar se há “casamento” com uma de suas linhas. Caso haja casamento de uma ou mais linhas, chama-se o método “escolherRoteador” que recebe um ArrayList de possíveis endereços, ou seja, possíveis linhas da tabelas para onde pode ser encaminhado o pacote. Aqui é utilizado o método “adicionarTamanhoMascara” da classe Mascara.

ProximoRoteador → Classe auxiliar que é utilizada apenas para guardar o IP e a porta do roteador para o qual encaminhar o pacote.

Mensagem → Possui métodos que recebem parâmetros informações do pacote, como IP destino, e formata e exibe as mensagens solicitadas no trabalho.

Emissor → Recebe uma entrada no formato IP_RoteadorDestino/ Porta_RoteadorDestino/IP_Origem/IP_Destino/Mensagem, cria o pacote com o campo de mensagem contendo o IP_Destino, IP_RoteadorDestino, IP_Origem e a mensagem, e a envia para o Roteador que está no IP_RoteadorDestino escutando na porta Porta_RoteadorDestino.

Funcionalidades não implementadas

- Enviar pacotes por interfaces que não são numéricas (como eth0 e eth1).

- Ler entradas por parâmetros de argumento (primeiro executa-se o arquivo.class e depois informa-se os parâmetros para a execução).

- Ler parâmetros separados por mais de um espaço “ ” (caso execute, por exemplo, “1111 10.0.0.0/255.0.0.0/0.0.0.0/0

150.1.0.0/255.255.0.0/0.0.0.0/0

30.1.2.0/255.255.255.0/127.0.0.1/2222

0.0.0.0/0.0.0.0/127.0.0.1/2222”, o programa executará incorretamente a lógica implementada e lançará uma exceção, pois entre “1111” e “10.0.0.0” existem dois espaços).



