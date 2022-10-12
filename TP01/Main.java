import java.io.*;
import java.util.Scanner;

public class Main {
    /**
     * Imprime o menu para o usuário
     */
    public static void menu() {
        System.out.println("\n1 - Criar conta");
        System.out.println("2 - Realizar Transferencia");
        System.out.println("3 - Procurar conta");
        System.out.println("4 - Deletar conta");
        System.out.println("5 - Atualizar dados da conta");
        System.out.println("6 - Listar todas as contas");
        System.out.println("7 - Buscar na lista Invertida");
        System.out.println("8 - Rodar simulação");
        System.out.println("0 - Sair");
    }

    /**
     * Faz a leitura do último ID utilizado, se for a primeira vez de uso, o ID está inicializado com -1
     * e contruirá o primeiro objeto com ID = 0
     * @param id
     */
    public static int getLastId(int id) {
        RandomAccessFile arq;
        int idReturn;

        try {
            arq = new RandomAccessFile("dados/conta.db", "rw");
            idReturn = arq.readInt();
            arq.close();

            return idReturn;
        } catch(IOException e) {
            System.out.println("Erro ao ler o último id a ser inserido!");
            return id;
        }
    }

    public static void main(String[] args) {
        CRUD crud = new CRUD();
        IndexDAO index = new IndexDAO();
        ListaInvertida listaInvertida = new ListaInvertida();
        OrdenacaoExterna ordenacao = new OrdenacaoExterna();

        Scanner sc = new Scanner(System.in);

        try {
            byte opcao;
            int id = -1;
            id = getLastId(id);
            
            do {
                menu();
    
                System.out.print("\nDigite sua opção: ");
                opcao = sc.nextByte();
    
                if (opcao != 0) {
                    if (opcao == 1) {
                        String nome;
                        String nomeUsuario;
                        String senha;
                        String cpf;
                        String cidade;
    
                        id++;
    
                        sc.nextLine(); // fazendo a captura do "enter" para não ter problema na leitura dos dados de criação
                        System.out.print("\nNome: ");
                        nome = sc.nextLine();
                        System.out.print("Nome de Usuario: ");
                        nomeUsuario = sc.nextLine();
                        System.out.print("Senha: ");
                        senha = sc.nextLine();
                        System.out.print("CPF: ");
                        cpf = sc.nextLine();
                        System.out.print("Cidade: ");
                        cidade = sc.nextLine();
    
                        Conta c = new Conta((byte) id, nome, nomeUsuario, senha, cpf, cidade);
                        crud.create(c, id);
                        listaInvertida.createArqLista(nome, (byte) id, "dados/listaInvertida/listaInvertidaNome.db");       // Criar lista para o nome
                        listaInvertida.createArqLista(cidade, (byte) id, "dados/listaInvertida/listaInvertidaCidade.db");   // Criar lista para a cidade
                    } else if (opcao == 2) {
                        // transferencia
                    } else if(opcao == 3) {
                        byte idSearch;
                        Conta conta;
    
                        System.out.println("\nInsira o ID que deseja pesquisar: ");
                        idSearch = sc.nextByte();
                        
                        conta = crud.readId(idSearch);
                        if(crud.idExists(idSearch)) {
                            System.out.println(conta);
                        } else {
                            System.out.println("O id inserido não existe.");
                        }
                    } else if(opcao == 4) {
                        System.out.println("\nInsira o ID do usuário que deseja deletar: ");
                        byte idDel = sc.nextByte();
                        
                        if (crud.delete(idDel) == true) {
                            index.deleteValue((byte) idDel);
                            System.out.println("Conta deletada com sucesso!");
                        } else {
                            System.out.println("Conta não foi encontrada!");
                        }
                    } else if (opcao == 5) {
                        byte idUpd;
                        String nomeUpd;
                        String nomeUsuUpd;
                        String senhaUpd;
                        String cpfUpd;
                        String cidadeUpd;

                        Conta contaPrint;
                        Conta contaVazia = new Conta((byte) 0, "", "", "", "", "");
                        
                        System.out.print("\nInsira o ID da conta que deseja alterar: ");
                        idUpd = sc.nextByte();

                        contaPrint = crud.readId(idUpd);
                        System.out.print(contaPrint);
    
                        if (crud.readId(idUpd) != contaVazia) {
                            System.out.println("\n\n***Insira a seguir os novos dados***\n");
                            sc.nextLine();  // Pegar enter
                            System.out.print("Nome: ");
                            nomeUpd = sc.nextLine();
                            System.out.println("Nome Usuario: ");
                            nomeUsuUpd = sc.nextLine();
                            System.out.println("Senha");
                            senhaUpd = sc.nextLine();
                            System.out.print("CPF: ");
                            cpfUpd = sc.nextLine();
                            System.out.print("Cidade: ");
                            cidadeUpd = sc.nextLine();
        
                            Conta cUpd = new Conta(idUpd, nomeUpd, nomeUsuUpd, senhaUpd, cpfUpd, cidadeUpd);
        
                            if (crud.update(cUpd)) {
                                listaInvertida.updateLista(nomeUpd, idUpd, "dados/listaInvertida/listaInvertidaNome.db", false);
                                listaInvertida.updateLista(cidadeUpd, idUpd, "dados/listaInvertida/listaInvertidaCidade.db", false);
                                System.out.println("Registro atualizado com sucesso!");
                            } else {
                                System.out.println("Não foi possível atualizar o registro!");
                            }
                        } else {
                            System.out.println("\nID inserido inexistente!");
                        }
                        
    
                    } else if (opcao == 6) {
                        crud.readAll();
                    } else if (opcao == 7) {
                        System.out.println("Deseja busca por nome(1) ou cidade(2)");
                        int buscaOption = sc.nextInt();

                        sc.nextLine();
                        if(buscaOption == 1) {
                            // busca por nome
                            String nome;
                            System.out.println("Digite o nome que deseja: ");
                            nome = sc.nextLine();
                            listaInvertida.searchList(nome, "dados/listaInvertida/listaInvertidaNome.db");
                        } else if(buscaOption == 2) {
                            // busca por cidade
                            String cidade;
                            System.out.println("Digite a cidade que deseja: ");
                            cidade = sc.nextLine();
                            listaInvertida.searchList(cidade, "dados/listaInvertida/listaInvertidaCidade.db");
                        } else {
                            System.out.println("A opcao escolhida nao foi valida");
                        }
                    } else if (opcao == 8) {
                        ordenacao.intercalacaoBalanceada(10, 2);
                    }
                }
            } while (opcao != 0);
            sc.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}