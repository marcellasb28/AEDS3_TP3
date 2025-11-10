package src.presenteFacil.model;

import java.util.ArrayList;
import src.presenteFacil.aeds3.*;

public class ArquivoLista extends Arquivo<Lista> {

    HashExtensivel<ParIDEndereco> indiceDiretoID;
    HashExtensivel<ParCodigoID> indiceDiretoCodigo;
    ArvoreBMais<ParIntInt> usuarioLista;

    public ArquivoLista() throws Exception {
        super("listas", Lista.class.getConstructor());

        indiceDiretoID = new HashExtensivel<>(
            ParIDEndereco.class.getConstructor(),
            4,
            "./data/listas/lista.id.d.db",
            "./data/listas/lista.id.c.db"
        );

        indiceDiretoCodigo = new HashExtensivel<>(
            ParCodigoID.class.getConstructor(),
            4,
            "./data/listas/lista.codigo.d.db",
            "./data/listas/lista.codigo.c.db"
        );

        usuarioLista = new ArvoreBMais<>(
            ParIntInt.class.getConstructor(),
            5,
            "./data/listas/lista.usuario.db"
        );
    }
    
    @Override
    public int create(Lista l) throws Exception {
        int id = super.create(l);
        indiceDiretoID.create(new ParIDEndereco(id, id));
        indiceDiretoCodigo.create(new ParCodigoID(l.getCodigo(), id));
        usuarioLista.create(new ParIntInt(l.getIdUsuario(), id));
        return id;
    }

    public Lista readByCodigo(String codigo) throws Exception {
        ParCodigoID pci = this.indiceDiretoCodigo.read(ParCodigoID.hash(codigo));
        if (pci == null) {
            return null;
        }
        return super.read(pci.getIDLista());
    }

    public Lista[] readByUsuario(int idUsuario) throws Exception {
        ArrayList<ParIntInt> pares = usuarioLista.read(new ParIntInt(idUsuario, -1));
        ArrayList<Lista> ativas = new ArrayList<>();

        for (ParIntInt p : pares) {
            Lista l = super.read(p.getId2());
            if (l != null && l.isAtiva()) {
                ativas.add(l);
            }
        }

        return ativas.toArray(new Lista[0]);
    }

    public Lista[] readByUsuarioDisableLists(int idUsuario) throws Exception {
        ArrayList<ParIntInt> pares = usuarioLista.read(new ParIntInt(idUsuario, -1));
        ArrayList<Lista> ativas = new ArrayList<>();

        for (ParIntInt p : pares) {
            Lista l = super.read(p.getId2());
            if (l != null && !l.isAtiva()) {
                ativas.add(l);
            }
        }

        return ativas.toArray(new Lista[0]);
    }

    public boolean update(Lista novaLista) throws Exception {
        Lista listaAntiga = super.read(novaLista.getId());
        if(listaAntiga == null){
            return false;
        }

        if(super.update(novaLista)) {
            if(!listaAntiga.getCodigo().equals(novaLista.getCodigo())) {
                indiceDiretoCodigo.delete(ParCodigoID.hash(listaAntiga.getCodigo()));
                indiceDiretoCodigo.create(new ParCodigoID(novaLista.getCodigo(), novaLista.getId()));
            }
            return true;
        }
        return false;
    }

    public boolean disableList(int idLista) throws Exception {

        Lista lista = super.read(idLista);
        
        if(lista == null) return false;

        lista.setAtiva(false);
        return super.update(lista);
    }

    public boolean activeList(int idLista) throws Exception {
        Lista lista = super.read(idLista);
        
        if(lista == null) return false;

        lista.setAtiva(true);
        return super.update(lista);
    }

    @Override
    public boolean delete(int idLista) throws Exception {
        
        Lista lista = super.read(idLista);
        
        if(lista == null) return false;

        if(super.delete(idLista)) {
            return indiceDiretoID.delete(lista.getId())
                && indiceDiretoCodigo.delete(ParCodigoID.hash(lista.getCodigo()))
                && usuarioLista.delete(new ParIntInt(lista.getIdUsuario(), idLista));
        }
        return false;
    }
}