/*
    Possui um vetor de 4bytes que armazena um endereço na forma de bytes e 
uma String que armazena o endereço na forma de String. 
Esta classe realiza operações binárias de comparação, 
como a lógica de criar e armazenar a versão binária de um endereço na forma String.

*/


public class Endereco {

    protected byte[] bytes = new byte[4];
    protected String enderecoString = "";
    

    public void adicionarEndereco(String endereco) {

        if (endereco.contains(".")) {
            String[] enderecoQuebrado = endereco.split("\\.");

            for (int a = 0; a < bytes.length; a++) {
                int byteConvertido = Integer.valueOf(enderecoQuebrado[a]);
                bytes[a] = (byte) byteConvertido;
            }
            enderecoString = endereco;
        }
    }


    public String getEnderecoString() {
        return enderecoString;
    }

    public byte getBytePosicao(int posicao) {
        return bytes[posicao];
    }


    protected void adicionarEndereco(String[] mascaraBinariaRepartida) {

        for (int a = 0; a < 4; a++) {
            bytes[a] = Byte.parseByte(mascaraBinariaRepartida[a], 2);
        }
    }





}
