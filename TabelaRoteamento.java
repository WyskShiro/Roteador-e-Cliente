/*
    Possui diversas LinhaTabela’s. 
Cada LinhaTabela é um rede-destino/máscara/gateway/interface 
que foi passado como parâmetro quando o Roteador foi criado. 
    Responsável por pegar os parâmetros passados pelo Roteador e 
criar a LinhaRoteamento. Seu método verificarIP recebe como parâmetro 
o IPdestino do pacote e 
realiza comparações para verificar se há casamento com uma de suas linhas. 
    Caso haja casamento de uma ou mais linhas, chama-se o método escolherRoteador 
que recebe um ArrayList de possíveis endereços, ou seja, 
possíveis linhas da tabelas para onde pode ser encaminhado o pacote. 
Aqui é utilizado o método adicionarTamanhoMascara da classe Mascara.



*/


import java.util.ArrayList;

public class TabelaRoteamento {

    private LinhaTabela[] linhasTabela;
    private int quantasEntradas;

    public TabelaRoteamento(int quantasEntradas) {
        linhasTabela = new LinhaTabela[quantasEntradas];
        
        for(int a = 0; a < quantasEntradas; a++) {
            
            linhasTabela[a] = new LinhaTabela();
        }

        this.quantasEntradas = quantasEntradas;
    }

    /*
    *Verifica se o IP é o final ou deve encaminhar para um prox Roteador
     */
    public ProximoRoteador verificarIP(String ipDestinoChegado) {
        
        Endereco enderecoDestino = new Endereco();
        ArrayList<LinhaTabela> possiveisEnderecos = new ArrayList();
        enderecoDestino.adicionarEndereco(ipDestinoChegado);

        ProximoRoteador proxRoteador = null;
        int posicaoByte = 0;
        for (int numeroRoteador = 0; numeroRoteador < quantasEntradas; numeroRoteador++) {
            for (posicaoByte = 0; posicaoByte < 4; posicaoByte++) {

                byte byteRoteadorDestino = linhasTabela[numeroRoteador].getByteRoteadorDestino(posicaoByte);
                byte byteMascara = linhasTabela[numeroRoteador].getByteMascaraDestino(posicaoByte);
                byte byteEnderecoDestino = enderecoDestino.getBytePosicao(posicaoByte);

                byte operacao1 = (byte) (byteRoteadorDestino & byteMascara);
                byte operacao2 = (byte) (byteEnderecoDestino & byteMascara);

                if (operacao1 != operacao2) {
                    break;
                }
            }
            
            if(posicaoByte == 4){
                //Significa que a mascara de certo
                possiveisEnderecos.add(linhasTabela[numeroRoteador]);
            }
        }
        
        return possiveisEnderecos.size() > 0 ? escolherRoteador(possiveisEnderecos) : null;
        
    }
    
    public ProximoRoteador escolherRoteador(ArrayList<LinhaTabela> possiveisEnderecos){
        int tamanhoMascara = 0;
        LinhaTabela linhaEscolhida = null;
        
        for(LinhaTabela linhaAtual : possiveisEnderecos) {
            if(linhaAtual.getTamanhoMascara() >= tamanhoMascara) {
                tamanhoMascara = linhaAtual.getTamanhoMascara();
                linhaEscolhida = linhaAtual;
            }
        }
        
        String enderecoDestino = linhaEscolhida.getProximoSalto().getEnderecoString();
        int numeroPorta = linhaEscolhida.getPortaSaida();

        return new ProximoRoteador(enderecoDestino, numeroPorta);
    }

    public void adicionarLinhaDeComando(String parte, int numeroLinhaTabela) {
        String[] informacoes = parte.split("/");

        linhasTabela[numeroLinhaTabela].setIpRoteadorDestino(informacoes[0]);
        linhasTabela[numeroLinhaTabela].setMascara(informacoes[1]);
        linhasTabela[numeroLinhaTabela].setProximoSalto(informacoes[2]);
        linhasTabela[numeroLinhaTabela].setPortaSaidaInterface(informacoes[3]);
        
    }

}
