import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import javax.swing.tree.TreeNode;

public class ContaBancaria {
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
    protected boolean lapide;

    public ContaBancaria(int idConta, String nomePessoa, short numEmails, String[] emails, String nomeUsusario,
            String senha, String cpf, String cidade, int transferenciasRealizadas, float saldoConta, boolean lapide) {
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
                    this.lapide = lapide;
    }

    public ContaBancaria(){
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
        lapide = false;
    }

}
