/*
    Possui um Endereco que é o IP do roteador destino, uma Mascara, 
um Endereco que é para onde deve ser encaminhado o pacote, 
caso ele case com a operação de E Binário e 
um inteiro que diz a porta para onde deve ser encaminhado.


*/


public class LinhaTabela {
    
    private Endereco ipRoteadorDestino;
    private Mascara mascara;
    private Endereco proximoSalto;
    private int portaSaidaInterface;
    
    
    public LinhaTabela() {
        ipRoteadorDestino = new Endereco();
        mascara = new Mascara();
        proximoSalto = new Endereco();
        portaSaidaInterface = 0;
    }

    public void setIpRoteadorDestino(String ipRoteadorDestino) {
        this.ipRoteadorDestino.adicionarEndereco(ipRoteadorDestino);;
    }

    public void setMascara(String mascara) {
        this.mascara.adicionarEndereco(mascara);
    }

    public void setProximoSalto(String proximoSalto) {
        this.proximoSalto.adicionarEndereco(proximoSalto);
    }

    public void setPortaSaidaInterface(String portaSaidaInterface) {
        this.portaSaidaInterface = Integer.parseInt(portaSaidaInterface);
    }
    
    
    public Endereco getRoteadorDestino() {
        return ipRoteadorDestino;
    }
    
    public byte getByteRoteadorDestino(int posicao) {
        return ipRoteadorDestino.getBytePosicao(posicao);
    }
    
    public byte getByteMascaraDestino(int posicao) {
        return mascara.getBytePosicao(posicao);
    }
    
    public int getTamanhoMascara() {
        return mascara.getTamanhoMascara();
    }
    
    public Endereco getProximoSalto() {
        return proximoSalto;
    }
    
    public int getPortaSaida() {
        return portaSaidaInterface;
    }
    
}
