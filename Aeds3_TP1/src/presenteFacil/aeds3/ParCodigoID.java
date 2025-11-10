package src.presenteFacil.aeds3;

import java.io.*;

public class ParCodigoID implements RegistroHashExtensivel<ParCodigoID> {

    private String codigo;
    private int idLista;

    private final short TAMANHO = 24;

    public ParCodigoID() {
        this("", -1);
    }

    public ParCodigoID(String codigo, int idLista) {
        this.codigo = codigo;
        this.idLista = idLista;
    }

    public String getCodigo() {
        return codigo; 
    }

    public int getIDLista() { 
        return idLista; 
    }

    public static int hash(String codigo) {
        return Math.abs(codigo.hashCode());
    }

    @Override
    public int hashCode() {
        return Math.abs(this.codigo.hashCode());
    }

    @Override
    public short size() {
        return this.TAMANHO;
    }

    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        dos.writeUTF(codigo);
        dos.writeInt(idLista);

        byte[] dados = baos.toByteArray();
        byte[] registro = new byte[TAMANHO];
        for (int i = 0; i < dados.length && i < TAMANHO; i++) {
            registro[i] = dados[i];
        }

        return registro;
    }

    @Override
    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);

        this.codigo = dis.readUTF();
        this.idLista = dis.readInt();
    }
}