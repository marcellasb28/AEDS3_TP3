package src.presenteFacil.model;

import java.util.*;
import src.presenteFacil.aeds3.*;

public class ArquivoListaProduto extends Arquivo<ListaProduto>{
    ArvoreBMais<ParIntInt> listaListaProduto; // Par(idLista; idListaProduto)
    ArvoreBMais<ParIntInt> produtoListaProduto; // Par(idProduto; idListaProduto)

    private ArquivoProduto arqProduto;
    private ArquivoLista arqLista;

    public ArquivoListaProduto() throws Exception {
        super("listaProduto", ListaProduto.class.getConstructor());

        this.arqProduto = new ArquivoProduto();
        this.arqLista = new ArquivoLista();

        listaListaProduto = new ArvoreBMais<>(
            ParIntInt.class.getConstructor(),
            5,
            "./data/listaProduto/listaId.listaProdutoId.db"
        );

        produtoListaProduto = new ArvoreBMais<>(
            ParIntInt.class.getConstructor(),
            5,
            "./data/listaProduto/produtoId.listaProdutoId.db"
        );
    }

    @Override
    public int create(ListaProduto lp) throws Exception {
        int id = super.create(lp);

        listaListaProduto.create(new ParIntInt(lp.getIdLista(), id));
        produtoListaProduto.create(new ParIntInt(lp.getIdProduto(), id));

        return id;
    }

    @Override
    public boolean delete(int idListaProduto) throws Exception {
        ListaProduto listaProduto = super.read(idListaProduto);
        
        if(listaProduto == null) return false;

        if(super.delete(idListaProduto)) {
            return listaListaProduto.delete(new ParIntInt(listaProduto.getIdLista(), idListaProduto))
                && produtoListaProduto.delete(new ParIntInt(listaProduto.getIdProduto(), idListaProduto));

        }

        return false;
    }

    public Produto[] getProdutosByListaId(int listaId) throws Exception {
        ArrayList<ParIntInt> pares = listaListaProduto.read(new ParIntInt(listaId, -1));
        ArrayList<Produto> produtos = new ArrayList<>();

        for (ParIntInt x : pares) {
            ListaProduto lp = super.read(x.getId2());
            Produto p = arqProduto.read(lp.getIdProduto());
            if (p != null)
                produtos.add(p);
        }

        return produtos.toArray(new Produto[0]);
    }

    public Lista[] getListasByProdutoId(int produtoId) throws Exception {
        ArrayList<ParIntInt> pares = produtoListaProduto.read(new ParIntInt(produtoId, -1));
        ArrayList<Lista> listas = new ArrayList<>();

        for (ParIntInt x : pares) {
            ListaProduto lp = super.read(x.getId2());
            Lista l = arqLista.read(lp.getIdLista());
            if (l != null)
                listas.add(l);
        }
        return listas.toArray(new Lista[0]);
    }

    public Lista[] getListaByProdutoIdAndUsuario(int produtoId, int idUsuario) throws Exception {
        ArrayList<ParIntInt> pares = produtoListaProduto.read(new ParIntInt(produtoId, -1));
        ArrayList<Lista> listas = new ArrayList<>();

        for (ParIntInt par : pares) {
            ListaProduto lp = super.read(par.getId2());
            if (lp != null) {
                Lista lista = arqLista.read(lp.getIdLista());
                if (lista != null && lista.getIdUsuario() == idUsuario && lista.isAtiva()) {
                    listas.add(lista);
                }
            }
        }

        return listas.toArray(new Lista[0]);
    }


    public ListaProduto[] readByListaId(int listaId) throws Exception {
        ArrayList<ParIntInt> pares = listaListaProduto.read(new ParIntInt(listaId, -1));
        ArrayList<ListaProduto> listaProdutos = new ArrayList<>();

        for (ParIntInt x : pares) {
            ListaProduto lp = super.read(x.getId2());
            if (lp != null) {
                listaProdutos.add(lp);
            }
        }

        return listaProdutos.toArray(new ListaProduto[0]);
    }

    public ListaProduto[] readByProdutoId(int produtoId) throws Exception {
        ArrayList<ParIntInt> pares = produtoListaProduto.read(new ParIntInt(produtoId, -1));
        ArrayList<ListaProduto> listaProdutos = new ArrayList<>();

        for (ParIntInt x : pares) { 
            ListaProduto lp = super.read(x.getId2());
            if (lp != null) {
                listaProdutos.add(lp);
            }
        }

        return listaProdutos.toArray(new ListaProduto[0]);
    }

    public ListaProduto readByListaIdAndProdutoId(int listaId, int produtoId) throws Exception {
        ArrayList<ParIntInt> pares = listaListaProduto.read(new ParIntInt(listaId, -1));

        for (ParIntInt par : pares) {
            ListaProduto lp = super.read(par.getId2());
            if (lp != null && lp.getIdProduto() == produtoId) {
                return lp;
            }
        }

        return null;
    }

}
