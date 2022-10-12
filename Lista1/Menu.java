/**
 * Classe menu que faz todas as chamadas de metodos e criaçõo de interface no terminal
 * @author Igor Franco
 * Matrícula: 750077
 */

import java.io.IOException;
import java.io.RandomAccessFile;
// import java.util.ArrayList;
import java.util.Scanner;

public class Menu extends ContaBancaria {
  public static int ultimoId(int id) {
    RandomAccessFile arquivo;
    int ultimoId;
    try {
      arquivo = new RandomAccessFile("contas.db", "rw");
      ultimoId = arquivo.readInt();
      arquivo.close();
      return ultimoId;
    } catch (IOException e) {
      System.out.println("criando novo arquivo");
      return id;
    }
  }

  // Metodo para limpar o terminal
  public static void limpatela() {
    System.out.println("\u000c");
    System.out.println("\u000c");
    System.out.println("\u000c");
    System.out.println("\u000c");
  }

  /* Metodo para imprimir o menu principal */
  public static void showMenu() {
    Menu.limpatela();
    System.out.println("+-------------------------------------------+");
    System.out.println("|        Menu de Opções                     |");
    System.out.println("+-------------------------------------------+");
    System.out.println("| Opção 1 - Criar uma nova Conta            |");
    System.out.println("| Opção 2 - Mostrar as Contas               |");
    System.out.println("| Opção 3 - Editar uma Conta                |");
    System.out.println("| Opção 4 - Remover uma Conta               |");
    System.out.println("| Opção 5 - Fazer Transferencia             |");
    System.out.println("| Opção 99 - Sair                           |");
    System.out.println("|-------------------------------------------|");
    System.out.println("Digite a opção desejada");
  }

  /**
   * Menu para interação do usuário para criar uma conta
   * @param id é passado nesse metodo para ser incrementado, se não existir ccontas salvas vai ser iniciado com 0
   */
  public static void op1(int id)throws Exception {
    CRUD crud = new CRUD();
    try (Scanner scannerOp1 = new Scanner(System.in)) {
      String nome;
      // short numEmails;
      // ArrayList<String> emails = new ArrayList<>();
      String nomeUsuario;
      String senha;
      String cpf;
      String cidade;

      Menu.limpatela();
      System.out.println("+-------------------------------------------+");
      System.out.println("|        Criar uma Conta                    |");
      System.out.println("+-------------------------------------------+");

      System.out.println("Aperte Enter para iniciar...");
      scannerOp1.nextLine();

      System.out.print("Digite o nome completo do usuario da Conta ->");
      nome = scannerOp1.nextLine();

      // System.out.print("Quantos emails sua conta terá? ");
      // numEmails = scannerOp1.nextShort();

      
      // scannerOp1.nextLine();

      // for(int i = 0; i < numEmails; i++){
      //   System.out.println("Digite o email de numero " + (i+1) + ":");
      //   emails.add(scannerOp1.nextLine());
      // }

      System.out.print("Digite o nome de usuario da Conta ->");
      nomeUsuario = scannerOp1.nextLine();

      System.out.print("Digite a senha da Conta ->");
      senha = scannerOp1.nextLine();

      System.out.print("Digite o CPF da Conta ->");
      cpf = scannerOp1.nextLine();

      System.out.print("Digite a cidade da Conta ->");
      cidade = scannerOp1.nextLine();

      id = ultimoId(id);
      id++;

      ContaBancaria novaConta = new ContaBancaria((byte) id, nome, nomeUsuario, senha, cpf, cidade, 0, 0);
      crud.C(novaConta, id);

      System.out.println("Conta criada com sucesso!!! O Id da sua conta é " + id);
      System.out.println("Aperte Enter para voltar para o menu...");
      scannerOp1.nextLine();
    }

  }

  /**
   * metodo para chamar a lista de contas
   * */
  public static void op2() {
    Menu.limpatela();
    try (Scanner scannerOp2 = new Scanner(System.in)) {
        System.out.println("+-------------------------------------------+");
        System.out.println("|        Lista de Contas                    |");
        System.out.println("+-------------------------------------------+");
        CRUD crud = new CRUD();
        crud.R();
        System.out.println("Aperte Enter para voltar para o menu...");
        scannerOp2.nextLine();
    }
  }

    /**
     * metodo para fazer o update de um clube usando o ID dele como referencia
     */
  public static void op3() {
    Menu.limpatela();
    System.out.println("+-------------------------------------------+");
    System.out.println("|        Editar uma Conta                   |");
    System.out.println("+-------------------------------------------+");

      String novoNome;
      // short novonumEmails = 1;
      // ArrayList<String> novosemails = new ArrayList<>();
      String novoNomeUsuario;
      String novaSenha;
      String novoCpf;
      String novaCidade;

    try (Scanner sc = new Scanner(System.in)) {
      CRUD crud = new CRUD();

      System.out.println("Insira o ID da Cconta que deseja modificar: ");
      System.out.print("ID escolhido: ");
      int id;
      id = sc.nextInt();

      //se o clube existir fazemos  upload
      if (crud.pesquisaId(id)) {
        Menu.limpatela();
        System.out.println("+-------------------------------------------+");
        System.out.println("|        Novos dados da Conta               |");
        System.out.println("+-------------------------------------------+");
        sc.nextLine();
        System.out.print("Digite o novo nome-> ");
        novoNome = sc.nextLine();
        
        // System.out.print("Quantos emails sua conta terá? ");
        // novonumEmails = sc.nextShort();

        // for(int i = 0; i < novonumEmails; i++){
        //   System.out.println("Digite o email de numero " + (i+1) + ":");
        //   novosemails.add(sc.nextLine());
        // }

        System.out.print("Digite o novo nome de usuario-> ");
        novoNomeUsuario = sc.nextLine();
        System.out.print("Digite a nova Senha-> ");
        novaSenha = sc.nextLine();
        System.out.print("Digite o novo CPF-> ");
        novoCpf = sc.nextLine();
        System.out.print("Digite a nova Cidade-> ");
        novaCidade = sc.nextLine();

        ContaBancaria novaConta = new ContaBancaria((byte) id, novoNome, novoNomeUsuario, novaSenha, novoCpf, novaCidade, 0, 0);

        if (crud.U(novaConta)) {
          System.out.println("Conta alterada");
        } else {
          System.out.println("erro");
        }
      } else {
        System.out.println("erro");
      }
      System.out.println("Aperte Enter para voltar para o menu...");
      sc.nextLine();
    }
  }

  /**
   * Metodo para remover um Clube utilizando o ID dele
   */
  public static void op4() {
    Menu.limpatela();
    System.out.println("+-------------------------------------------+");
    System.out.println("|        Remover uma Conta                  |");
    System.out.println("+-------------------------------------------+");
    CRUD crud = new CRUD();
    try (Scanner sc = new Scanner(System.in)) {
        System.out.println("Digite o ID da Conta que deseja remover");
        System.out.print("ID escolhido: ");
        int id = sc.nextByte();
        if (crud.D(id) != false) { //verificacao
          System.out.println("Conta deletado");
        } else {
          System.out.println("erro");
        }
        System.out.println("Aperte Enter para voltar para o menu...");
        sc.nextLine();
    }

  }


}
