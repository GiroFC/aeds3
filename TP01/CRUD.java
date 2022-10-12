import java.io.*;
import java.io.IOException;

public class CRUD extends IndexDAO {
    private RandomAccessFile arq;
    private final String nomeArquivo = "dados/conta.db";

    /**
     * Cria a classe e o arquivo, caso ele não exista, adicionando o ID = -1, para o primeiro objeto ter o ID = 0
     */
    public CRUD() {
        try {
            boolean exists = (new File(nomeArquivo)).exists();

            if (exists) {
                // Arquivo já existe
            } else {
                try {
                    int id = -1;

                    arq = new RandomAccessFile(nomeArquivo, "rw");
                    arq.writeInt(id);
                    arq.close();
                } catch (Exception e) {
                    System.out.println("Erro para criar e inicializar o id no arquivo!");
                }
            }
        } catch (Exception e) {
            System.out.println("Erro! " + e.getMessage());
        }
    }

    /**
     * Criação de um Conta dentro do arquivo
     * @param c -> objeto criado pelo usuário
     * @param id -> id do objeto criado, para ser adicionado no inicio do arquivo
     */
    public void create(Conta c, int id) {
        byte[] ba;
        IndexDAO indexado;

        try {
            arq = new RandomAccessFile(nomeArquivo, "rw");

            ba = c.toByteArray();
            arq.seek(0); // Movendo o ponteiro para o início do arquivo para atualizar o valor do ID
            arq.writeInt(id);
            arq.seek(arq.length());
            long enderecoPointer = arq.getFilePointer();
            arq.writeChar(' '); // Escritura da Lápide do registro
            arq.writeInt(ba.length);
            arq.write(ba);
            // Adicionar os dados do objeto no arquivo de indices para busca
            indexado = new IndexDAO((byte) id, enderecoPointer);
            indexado.addValue(indexado);
            arq.close();
        } catch (IOException e) {
            System.out.println("Erro para inserir a conta no arquivo!");
        }
    }

    /**
     * 
     * @param c -> novo objeto Conta para ser substituido
     * @return boolean (true para atualizado, false para erro)
     */
    public boolean update(Conta c) {
        try {
            long pos;
            char lapide;
            byte[] b;
            byte[] novoB;
            int tam;
            Conta objeto;

            long endereco;
            IndexDAO index;

            arq = new RandomAccessFile(nomeArquivo, "rw");

            arq.seek(4);

            while(arq.getFilePointer() < arq.length()) {
                pos = arq.getFilePointer();
                lapide = arq.readChar();
                tam = arq.readInt();
                b = new byte[tam];
                arq.read(b);
                if (lapide != '*') {
                    objeto = new Conta();
                    objeto.fromByteArray(b);
                    if (objeto.getId() == c.getId()) {
                        novoB = c.toByteArray();

                        if (novoB.length == tam) {
                            arq.seek(pos + 6); // Utilizado pos + 6 para que não atualize em cima da lápide nem do tamanho
                            arq.write(novoB);
                            arq.close();
                            return true;
                        } else {
                            arq.seek(arq.length());
                            endereco = arq.getFilePointer();
                            arq.writeChar(' ');
                            arq.writeInt(novoB.length);
                            arq.write(novoB);
                            delete(objeto.getId());
                            index = new IndexDAO(c.idConta, endereco);
                            index.updateValue(index);
                            arq.close();
                            return true;
                        }
                    }
                }
            }
            arq.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Deleta cliente do arquivo
     * @param id Id do cliente a ser removido
     */
    public boolean delete(byte id) {
        try {
            long pos;
            char lapide;
            int tam;
            byte[] b;
            Conta conta;

            arq = new RandomAccessFile(nomeArquivo, "rw");
            arq.seek(4); // Pular cabeçalho

            while(arq.getFilePointer() < arq.length()) {
                pos = arq.getFilePointer();
                lapide = arq.readChar();
                tam = arq.readInt();
                b = new byte[tam];
                arq.read(b);
                if(lapide != '*') {
                    conta = new Conta();
                    conta.fromByteArray(b);
                    if(conta.getId() == id) {
                        arq.seek(pos);
                        arq.writeChar('*');
                        arq.close();
                        return true;
                    }
                }
            }
            arq.close();
        } catch(IOException e) {
            System.out.println("Erro ao atualizar lápide (deletar) conta!");
        }
        return false;
    }

    /**
     * Metódo utilizado para retornar o conta de futebol e suas caracteristicas desejadas, buscando pelo ID
     * @param id -> id passado pelo usuário para retornar o conta
     */
    public Conta readId(byte id) {
        Conta conta = new Conta((byte) 0, "", "", "", "", "");

        try {
            arq = new RandomAccessFile(nomeArquivo, "rw");

            char lapide;
            byte[] b;
            int tam;
            
            if(idExists(id)) {
                long posOriginal = buscaBinaria((int) id);
                arq.seek(posOriginal);
                lapide = arq.readChar();
                tam = arq.readInt();
                b = new byte[tam];
                arq.read(b);
                if (lapide != '*') {
                    conta = new Conta();
                    conta.fromByteArray(b);
                    return conta;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return conta;
    }

    /**
     * Confere se o ID existe no arquivo original e se não está deletado
     * @param id -> id a ser conferido
     * @return -> verdadeiro se o id existir e falso se nao existir
     */
    public boolean idExists(byte id) {
        if(buscaBinaria(id) != -1) {
            return true;
        }
        return false;
    }

    /**
     * Lê uma conta do arquivo pelo nome inserida pelo usuário
     * @param s
     * @return object (conta pesquisada), caso não ache a conta null
     */
    public Conta readByName(String s) {
        try {
            arq = new RandomAccessFile("conta.db", "rw");

            Conta c;
            byte[] ba;
            int tam;

            arq.seek(4);

            while (arq.getFilePointer() < arq.length()) {
                char lapide = arq.readChar();
                tam = arq.readInt();
                ba = new byte[tam];
                arq.read(ba);

                if (lapide != '*') {
                    c = new Conta();
                    c.fromByteArray(ba);
                    if (c.nomePessoa.equals(s)) {
                        return c;
                    }
                }
            }

            return null;
        } catch (IOException e) {
            System.out.println("Não foi possível encontrar a conta desejada!");
            return null;
        }
    }

    

    /**
     * Imprime todas as contas do arquivo para o usuário
     */
    public void readAll() {
        try {
            arq = new RandomAccessFile(nomeArquivo, "rw");
            arq.seek(4);

            char lapide;
            byte[] b;
            int tam;
            Conta objeto;

            while (arq.getFilePointer() < arq.length()) {
                lapide = arq.readChar();
                tam = arq.readInt();
                b = new byte[tam];
                arq.read(b);
                if (lapide != '*') {
                    objeto = new Conta();
                    objeto.fromByteArray(b);
                    System.out.println(objeto.toString());
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao imprimir todas as contas!");
        }
    }

}