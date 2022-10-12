/**
 * Classe CRUD para fazer as alterações no arquivo contass.db
 * @author Igor Franco
 * Matrícula: 750077
 */

import java.io.*;

public class CRUD {
    private final String nomeDoArquivo = "contas.db";
    private RandomAccessFile arq;

    /**
     * Mertodo para iniciar o crud e força o primeiro ID  a ser 0
     */
    public CRUD(){
        try {
        boolean verificaArquivo = (new File(nomeDoArquivo)).exists();
        if (!verificaArquivo){
            try {
            int id = -1;
            arq = new RandomAccessFile(nomeDoArquivo, "rw");
            arq.writeInt(id);
            arq.close();
            } catch (Exception e){System.out.println(e.getMessage() + "erro em criar o id");}
        }
        }catch(Exception e){System.out.println(e.getMessage());}
    }

    //CREATE
    /**
    * Método para criaçõo de uma conta utilizando o id passado pelo Menu.op1()
     */
    public void C (ContaBancaria contausuario, int id){
        byte[] data;
        try {
        arq = new RandomAccessFile(nomeDoArquivo, "rw");
        data = contausuario.TBA();
        arq.seek(0);
        arq.writeInt(id);
        arq.seek(arq.length());
        arq.writeChar(' '); // lapide
        arq.writeInt(data.length);
        arq.write(data);
        arq.close();
        }catch(IOException e){System.out.println(e.getMessage() + "erro de inserção");}
    }

    /**
    * Vai fazer toda a leitura do arquvio e vai pular o cabecalho
    */
    public void R() {
        try {
            arq = new RandomAccessFile(nomeDoArquivo, "rw");
            arq.seek(4);

            byte[] data;
            ContaBancaria conta;
            char lapide;
            int TAM;

            while (arq.getFilePointer() < arq.length()) {
                lapide = arq.readChar();
                TAM = arq.readInt();
                data = new byte[TAM];
                arq.read(data);
                if (lapide != '*') {
                    conta = new ContaBancaria();
                    conta.FBA(data);
                    System.out.println(conta.toString());
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    


    /**
     * Recebe uma conta e que vai substiruir a Conta ja  presente no arquivo
     * Além disso é preciso aumentar o seek para não sobrescrever os dados
     * @param contaUsuario
     * @return
     */
    public boolean U(ContaBancaria contaUsuario){
        try {
        byte[] data;
        ContaBancaria conta;
        byte[] newData;
        long posicao;
        char lapide;
        int TAM;

        arq = new RandomAccessFile(nomeDoArquivo, "rw");
        arq.seek(4);

        while(arq.getFilePointer() < arq.length()) {
            posicao = arq.getFilePointer();
            lapide = arq.readChar();
            TAM = arq.readInt();
            data = new byte[TAM];

            arq.read(data);

            if (lapide != '*') {
                conta = new ContaBancaria();
                conta.FBA(data);
                if (conta.getIdConta() == contaUsuario.getIdConta()) {
                    newData = contaUsuario.TBA();
                    if (newData.length <= TAM) {
                        arq.seek(posicao + 6);
                        arq.write(newData);
                        arq.close();
                    } else {
                        arq.seek(arq.length());
                        arq.writeChar(' ');
                        arq.writeInt(newData.length);
                        arq.write(newData);
                        D(conta.getIdConta());
                        arq.close();
                    }
                    return true;
                }
            }
        }
        arq.close();
        return false;
    } catch (Exception e) {
        System.out.println(e.getMessage());
        return false;
        }
    }

    /**
   * remove a conta do arquivo
   * @param id da conta removido
   * @return
   */

    public boolean D(int id) {
        try {
            byte[] data;
            ContaBancaria conta;
            long posicao;
            char lapide;
            int TAM;
    
            arq = new RandomAccessFile(nomeDoArquivo, "rw");
            arq.seek(5);
    
            while(arq.getFilePointer() < arq.length()) {
                posicao = arq.getFilePointer();
                lapide = arq.readChar();
                TAM = arq.readInt();
                data = new byte[TAM];
                arq.read(data);
                if(lapide != '*') {
                    conta = new ContaBancaria();
                    conta.FBA(data);
                    if(conta.getIdConta() == id) {
                        arq.seek(posicao);
                        arq.writeChar('*');
                        arq.close();
                        return true;
                    }
                }
            }
            arq.close();
            return false;
        } catch(IOException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }    


    /**
 * faz uma busca no arquivo comparando o id e retnora falso se o no e nao existir
 * @param id
 * @return
 */
  public boolean pesquisaId(int id) {
    try {
        arq = new RandomAccessFile(nomeDoArquivo, "rw");

        byte[] data;
        ContaBancaria objeto;
        char lapide;
        int TAM;

        arq.seek(4);

        while (arq.getFilePointer() < arq.length()) {
            lapide = arq.readChar();
            TAM = arq.readInt();
            data = new byte[TAM];
            arq.read(data);

            if (lapide != '*') {
                objeto = new ContaBancaria();
                objeto.FBA(data);

                if (objeto.getIdConta() == id) {
                    System.out.println(objeto.toString());
                    return true;
                }
            }
        }
        return false;

    } catch (IOException e) {
        System.out.println(e.getMessage());
        return false;
    }
}
/**
 * Mesma coisa do que o pesquisaId mas utiliza o nome como parametro
 * @param nomeConta
 * @return retorna o Conta desejado utilizado para fazer as partidas
 */
public ContaBancaria pesquisaNome(String nomeConta) {
  try {
      arq = new RandomAccessFile(nomeDoArquivo, "rw");

      byte[] data;
      ContaBancaria conta;
      int TAM;

      arq.seek(4);

      while (arq.getFilePointer() < arq.length()) {
          char lapide = arq.readChar();
          TAM = arq.readInt();
          data = new byte[TAM];
          arq.read(data);

          if (lapide != '*') {
              conta = new ContaBancaria();
              conta.FBA(data);
              if (conta.nomePessoa.equals(nomeConta)) {
                  return conta;
              }
          }
      }

      return null;
  } catch (IOException e) {
      System.out.println(e.getMessage());
      return null;
  }
}

}
