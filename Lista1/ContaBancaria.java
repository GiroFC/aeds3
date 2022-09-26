import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class ContaBancaria {
    protected boolean lapide;
    protected int idConta;
    protected String nomePessoa;
    protected short numEmails;
    protected String[] emails;
    protected String nomeUsusario;
    protected String senha;
    protected String cpf;
    protected String cidade;
    protected int transferenciasRealizadas;
    protected float saldoConta;

    //instâncias
    public ContaBancaria(boolean lapide, int idConta, String nomePessoa, short numEmails, String[] emails, String nomeUsusario,
            String senha, String cpf, String cidade, int transferenciasRealizadas, float saldoConta) {
                    this.lapide = lapide;    
                    this.idConta = idConta;
                    this.nomePessoa = nomePessoa;
                    this.numEmails = numEmails;
                    for(int i = 0; i < numEmails; i++){
                        this.emails[i] = emails[i];
                    }
                    this.nomeUsusario = nomeUsusario;
                    this.senha = senha;
                    this.cpf = cpf;
                    this.cidade = cidade;
                    this.transferenciasRealizadas = transferenciasRealizadas;
                    this.saldoConta = saldoConta;
    }

    public ContaBancaria(){
        lapide = false;
        idConta = -1;
        nomePessoa = "";
        numEmails = 0;
        emails = null;
        nomeUsusario = "";
        senha = "";
        cpf = "";
        cidade = "";
        transferenciasRealizadas = 0;
        saldoConta = 0F;
    }


    //Getters e Setters
    public int getIdConta(){return idConta;}
    public void setIdConta(int idConta) {this.idConta = idConta;}

    public String getNomePessoa() {return nomePessoa;}
    public void setNomePessoa(String nomePessoa) {this.nomePessoa = nomePessoa;}

    public short getNumEmails() {return numEmails;}
    public void setNumEmails(short numEmails) {this.numEmails = numEmails;}

    public String[] getEmails() {return emails;}
    public void setEmails(String[] emails) {this.emails = emails;}

    public String getNomeUsusario() {return nomeUsusario;}
    public void setNomeUsusario(String nomeUsusario) {this.nomeUsusario = nomeUsusario;}

    public String getSenha() {return senha;}
    public void setSenha(String senha) {this.senha = senha;}

    public String getCpf() {return cpf;}
    public void setCpf(String cpf) {this.cpf = cpf;}

    public String getCidade() {return cidade;}
    public void setCidade(String cidade) {this.cidade = cidade;}

    public int getTransferenciasRealizadas() {return transferenciasRealizadas;}
    public void setTransferenciasRealizadas(int transferenciasRealizadas) {this.transferenciasRealizadas = transferenciasRealizadas;}

    public float getSaldoConta() {return saldoConta;}
    public void setSaldoConta(float saldoConta) {this.saldoConta = saldoConta;}

    public boolean isLapide() {return lapide;}
    public void setLapide(boolean lapide) {this.lapide = lapide;}


    //metodo para acrecentar valor ao saldo
    public void saldoAdd(float x){
        saldoConta+=x;
    }

    //metodo para subtrair valor ao saldo
    public void saldoSub(float x){
        if((saldoConta - x) < 0){
            System.out.println("Erro!, saldo insuficiente para a retirada");
        }else{
            saldoConta-=x;
        }
    }

    //metodopara acrecentar numero de tranferencias realizada
    public void tranferenciaAdd(){
        transferenciasRealizadas++;
    }



    /**
    * Método para transformar um objeto em um byte array para ser armazenado no arquivo .db
    * @return
    * @throws IOException
    */
    public byte[] TBA() throws IOException {

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream dos = new DataOutputStream(baos);

    dos.writeBoolean(lapide);
    dos.write(idConta);
    dos.writeUTF(nomePessoa);
    dos.write(numEmails);
    for(int i = 0; i < numEmails; i++){
        dos.writeUTF(emails[i]);
    }
    dos.writeUTF(nomeUsusario);
    dos.writeUTF(senha);
    dos.writeUTF(cpf);
    dos.writeUTF(cidade);
    dos.write(transferenciasRealizadas);
    dos.writeFloat(saldoConta);
    return baos.toByteArray();
    }



    /**
    * método para pegar um byte array e transformar ele um um objeto
    * @param ba parametro que recebe byte array
    * @throws IOException
    */
    public void FBA(byte[] ba) throws IOException {
    ByteArrayInputStream bais = new ByteArrayInputStream(ba);
    DataInputStream dis = new DataInputStream(bais);
    lapide = dis.readBoolean();
    idConta = dis.readByte();
    nomePessoa = dis.readUTF();
    numEmails = dis.readByte();
    for(int i = 0; i < numEmails; i++){
        emails[i] = dis.readUTF();
    }
    nomeUsusario = dis.readUTF();
    senha = dis.readUTF();
    cpf = dis.readUTF();
    cidade = dis.readUTF();
    transferenciasRealizadas = dis.readByte();
    saldoConta = dis.readByte();
    }

    private String emailsString(){
        String resp = "";
        for(int i = 0; i < numEmails; i++){
            resp += emails[i] + " ";
        }
        return resp;
    }

    public String toString() {
        return 
          "\nID: " + idConta +
          "\nNome Completo: " + nomePessoa +
          "\nCidade: " + cidade +
          "\nEmails: " + emailsString() +
          "\nNome Usuario: " + nomeUsusario + 
          "\nCPF: " + cpf + 
          "\nSaldo: " + saldoConta +
          "\nTranferencias Realizadas: " + transferenciasRealizadas;
      }
}
