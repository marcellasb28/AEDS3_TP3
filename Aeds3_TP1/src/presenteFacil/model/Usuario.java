package src.presenteFacil.model;

import java.io.*;

public class Usuario implements Registro {

    private int id;
    private String nome;
    private String email;
    private int HashSenha;
    private String PerguntaSecreta;
    private int HashRespostaSecreta;
    private boolean ativo;

    public Usuario() {
        this.id = -1;
        this.nome = "";
        this.email = "";
        this.HashSenha = -1;
        this.PerguntaSecreta = "";
        this.HashRespostaSecreta = -1;
        this.ativo = false;
    }

    public Usuario(String nome, String email, int senhaHash, String pergunta, String resposta) {
        this.id = -1;
        this.nome = nome;
        this.email = email;
        this.HashSenha = senhaHash;
        this.PerguntaSecreta = pergunta;
        this.HashRespostaSecreta = resposta.hashCode();
        this.ativo = true;
    }

    @Override
    public int getID() { return id; }

    @Override
    public void setID(int id) { this.id = id; }

    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(this.id);
        dos.writeUTF(this.nome);
        dos.writeUTF(this.email);
        dos.writeInt(this.HashSenha);
        dos.writeUTF(this.PerguntaSecreta);
        dos.writeInt(this.HashRespostaSecreta);
        dos.writeBoolean(this.ativo);
        return baos.toByteArray();
    }

    @Override
    public void fromByteArray(byte[] ba) throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        this.id = dis.readInt();
        this.nome = dis.readUTF();
        this.email = dis.readUTF();
        this.HashSenha = dis.readInt();
        this.PerguntaSecreta = dis.readUTF();
        this.HashRespostaSecreta = dis.readInt();
        this.ativo = dis.readBoolean();
    }

    public String toString() {
        return "Nome: " + this.nome + "\n" +
               "Email: " + this.email + "\n" +
               "PerguntaSecreta: " + this.PerguntaSecreta + "\n" +
               "Ativo: " + (this.ativo ? "Sim" : "Não") + "\n";
    }

    // Getters
    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public int getHashSenha() { return HashSenha; }
    public String getPerguntaSecreta() { return PerguntaSecreta; }
    public int getHashRespostaSecreta() { return HashRespostaSecreta; }
    public boolean isAtivo() { return ativo; }

    // Setters
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
    public void setNome(String nome) { this.nome = nome; }
    public void setEmail(String email) { this.email = email; }
    public void setHashSenha(int hashSenha) { this.HashSenha = hashSenha; }
    public void setPerguntaSecreta(String pergunta) { this.PerguntaSecreta = pergunta; }
    public void setHashRespostaSecreta(int hashRespostaSecreta) { this.HashRespostaSecreta = hashRespostaSecreta; }
}

