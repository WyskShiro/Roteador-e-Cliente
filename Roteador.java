/*
    Recebe uma entrada no formato numero_porta rede-destino/máscara/gateway/interface 
que definirá seu funcionamento. 
Após criado, ele ficará escutando na porta definida na entrada até receber um pacote. 
    Caso venha a receber um pacote, ele lerá o campo de mensagem do pacote, 
o qual carrega o IP destino (da rede que se quer chegar), 
o IP do roteador ( para o qual a mensagem está sendo enviada), 
o IP origem (de quem criou o pacote) e a mensagem em si. 
    Após lê-lo, ele verificará na sua tabela de roteamento o que fazer com o pacote 
(se descarta-o, se encaminha-o diretamente ou repassa para um próximo roteador, 
alterando e recriando corretamente a parte de mensagem do pacote).

*/


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Roteador {

    private int portaExecucao = 12345;
    private byte[] msg;
    private TabelaRoteamento tabela;
    private DatagramSocket socketReceptor = null;

    /*
    * Recebe os parâmetros da linha de comando
    * Quebra os parâmetros em Strings
    * O primeiro parâmetro é a porta que ficará "aguardando entrarem"
    * Os outros são para criar sua tabela de roteamento
     */
    public Roteador(String linhaDeComando) {

        String[] partes = linhaDeComando.split(" ");
        portaExecucao = Integer.parseInt(partes[0]);
        tabela = new TabelaRoteamento(partes.length - 1);

        for (int a = 1; a < partes.length; a++) {
            tabela.adicionarLinhaDeComando(partes[a], a - 1);
        }
        
        executar();
    }

    public static void main(String[]args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String linhaDeComando;
        
        // Lê uma linha de comando e vai criando threads de roteadores
        linhaDeComando = br.readLine();
        linhaDeComando = linhaDeComando.replaceAll("   ", " ");
        linhaDeComando = linhaDeComando.replaceAll("  ", " ");

        Roteador novoRoteador = new Roteador(linhaDeComando);
    }

    /*
    * Cria o Socket na porta informada para ficar escutando
    * Caso receba um pacote, é perguntado para sua tabela de roteamento para onde reencaminhar
    * Caso não retorne nada, significa que chegou onde deveria
    * Caso retorne um endereço, reencaminhá-lo
    *
    * Mensagem -> ipOrigem, ipDestino, mensagem
     */
    public void executar() {
        byte[] mensagemByteficada = new byte[256];
        DatagramPacket pacote = new DatagramPacket(mensagemByteficada, mensagemByteficada.length);

        criarSocketReceptor();
        esperarReceber(pacote);

        String[] informacoesMensagem = decodificarPacote(pacote);
        String ipDestinoFinal = informacoesMensagem[0];
        String ipProxRoteador;
        String ipOrigem = informacoesMensagem[2];
        String mensagem = informacoesMensagem[3];


        ProximoRoteador proximoRoteador = tabela.verificarIP(informacoesMensagem[0]);

        if (proximoRoteador == null) { //Significa que nao encontrou candidato
            Mensagem.destinoInexistente(ipDestinoFinal);
            
        } else if (proximoRoteador.getIpProxRoteador().equals("0.0.0.0")) {
            Mensagem.encaminharDiretamente(informacoesMensagem);
            
        } else {
            ipProxRoteador = proximoRoteador.getIpProxRoteador();
            int portaProxRoteador = proximoRoteador.getPortaProxRoteador();

            mensagem = ipDestinoFinal + " " + ipProxRoteador + " " + ipOrigem + " " + mensagem;

            enviarPacote(mensagem, ipProxRoteador, portaProxRoteador);
            Mensagem.encaminharPacote(ipDestinoFinal, ipProxRoteador, portaProxRoteador);

        }
        socketReceptor.close();
    }

    private void enviarPacote(String mensagem, String ipRoteador, int portaProxRoteador) {

        InetAddress enderecoRoteador = null;
        byte[] mensagemByteficada;
        DatagramPacket pacote;
        DatagramSocket socketEnviador = null;

        try {
            enderecoRoteador = InetAddress.getByName(ipRoteador);
        } catch (UnknownHostException ex) {
            Logger.getLogger(Roteador.class.getName()).log(Level.SEVERE, null, ex);
        }

        mensagemByteficada = mensagem.getBytes();

        pacote = new DatagramPacket(mensagemByteficada, mensagemByteficada.length, enderecoRoteador, portaProxRoteador);
        try {
            socketEnviador = new DatagramSocket();
        } catch (SocketException ex) {
            Logger.getLogger(Roteador.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            socketEnviador.send(pacote);
        } catch (IOException ex) {
            Logger.getLogger(Roteador.class.getName()).log(Level.SEVERE, null, ex);
        }

        socketEnviador.close();
    }

    private void criarSocketReceptor() {
        try {
            socketReceptor = new DatagramSocket(portaExecucao);
        } catch (SocketException ex) {

            Logger.getLogger(Roteador.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void esperarReceber(DatagramPacket pacote) {

        try {
            socketReceptor.receive(pacote);
        } catch (IOException ex) {
            socketReceptor.close();
            Logger.getLogger(Roteador.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private String[] decodificarPacote(DatagramPacket pacote) {

        String informacoesPacote = new String(pacote.getData()).trim();
        String[] informacoesQuebradas = informacoesPacote.split(" ", 4);

        return informacoesQuebradas;
    }
    

}
