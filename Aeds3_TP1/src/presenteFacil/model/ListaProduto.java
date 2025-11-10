package src.presenteFacil.model;

import java.io.*;

public class ListaProduto implements Registro{
    private int id;
    private int idLista; //Chave Estrangeira
    private int idProduto; //Chave Estrangeira
    private int quantidade;
    private String observacoes;

    public ListaProduto() {
        this.id = -1;
        this.idLista = -1;
        this.idProduto = -1;
        this.quantidade = 0;
        this.observacoes = "";
    }

    public ListaProduto(int id, int idLista, int idProduto, int quantidade, String observacoes) {
        this.id = id;
        this.idLista = idLista;
        this.idProduto = idProduto;
        this.quantidade = quantidade;
        this.observacoes = observacoes;
    }

    public ListaProduto(int idLista, int idProduto, int quantidade, String observacoes) {
        this.id = -1;
        this.idLista = idLista;
        this.idProduto = idProduto;
        this.quantidade = quantidade;
        this.observacoes = observacoes;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getIdLista() {
        return idLista;
    }

    public void setIdLista(int idLista) {
        this.idLista = idLista;
    }

    public int getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(int idProduto) {
        this.idProduto = idProduto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getID() {
        return id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setID(int id) {
        this.id = id;
    }

    @Override
    public byte[] toByteArray() throws Exception{
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(this.id);
        dos.writeInt(this.idLista);
        dos.writeInt(this.idProduto);
        dos.writeInt(this.quantidade);
        dos.writeUTF(this.observacoes);
        return baos.toByteArray();
    }

    @Override
    public void fromByteArray(byte[] vb) throws Exception{
        ByteArrayInputStream bais = new ByteArrayInputStream(vb);
        DataInputStream dis = new DataInputStream(bais);
        this.id = dis.readInt();
        this.idLista = dis.readInt();
        this.idProduto = dis.readInt();
        this.quantidade = dis.readInt();
        this.observacoes = dis.readUTF();
    }
    
    /*@Override
    public String toString() {
        return "ListaProduto{" +
                "idLista=" + idLista +
                ", idProduto=" + idProduto +
                ", quantidade=" + quantidade +
                ", observacoes='" + observacoes + '\'' +
                '}';
    }*/

    @Override
    public String toString() {
        return "QUANTIDADE.....: " + this.quantidade +
               "\nOBESERVAÇÕES...: " + this.observacoes;
    }
}

