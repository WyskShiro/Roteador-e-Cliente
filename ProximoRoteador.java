/*
    Classe auxiliar que é utilizada apenas para guardar o IP 
e a porta do roteador para o qual encaminhar o pacote.

*/


public class ProximoRoteador {
    private String ipProxRoteador;
    private int portaProxRoteador;

    
    public ProximoRoteador(String ipProxRoteador, int portaProxRoteador) {
        this.ipProxRoteador = ipProxRoteador;
        this.portaProxRoteador = portaProxRoteador;
    }

    public String getIpProxRoteador() {
        return ipProxRoteador;
    }

    public int getPortaProxRoteador() {
        return portaProxRoteador;
    }
    
    
    
    
}
