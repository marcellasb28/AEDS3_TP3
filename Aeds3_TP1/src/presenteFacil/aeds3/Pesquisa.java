package src.presenteFacil.aeds3;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.Collections;

public class Pesquisa {
    public ListaInvertida listaInvertida;

    public static final Set<String> StopWords = new HashSet<>(Arrays.asList(
            // Símbolos de pontuação
            "!", "\"", "#", "$", "%", "&", "'", "(", ")", "*", "+", ",", "-", ".", "/", ":", ";", "?", 
            
            // Artigos, pronomes, preposições, advérbios, conjunções e palavras comuns
            "a","à","ao","aos","aquela","aquelas","aquele","aqueles","as","às","até",
            "agora","ainda","além","algmas","algum","alguma","alguns","algumas","alguém",
            "ali","ambos","antes","apenas","apoio","cada","cá","coisa","como","com","contra","contudo",
            "cuja","cujas","cujo","cujos","da","das","de","dela","dele","deles","demais","depois","desde",
            "dessa","desse","desta","deste","disto","do","dos","e","é","ela","elas","ele","eles",
            "em","enquanto","entre","era","eram","essa","essas","esse","esses","esta","está","estão",
            "estas","estava","estavam","este","estes","esteja","estejam","estive","estivemos","estiveram",
            "eu","faz","fazer","fez","fim","foi","for","fora","foram","há","isso","isto","já","lá",
            "lhe","lhes","mais","mas","me","mesma","mesmas","mesmo","mesmos","meu","meus","minha",
            "minhas","muito","muitos","na","nas","nem","no","nos","nós","nossa","nossas","nosso","nossos",
            "num","numa","nunca","o","os","ou","onde","para","pela","pelas","pelo","pelos","per",
            "perante","pode","podem","pois","por","porque","porém","pouco","primeiro","qual","quais",
            "quando","quanto","que","quem","se","sem","seu","seus","só","sob","sobre","sua","suas",
            "tal","também","te","tem","têm","tenho","ter","teu","teus","toda","todas","todo","todos",
            "tu","tua","tuas","tudo","um","uma","umas","uns","vai","vão","você","vocês"
        ));

        public Pesquisa(ListaInvertida li){
            this.listaInvertida = li;
        }

        public static String removerAcentos(String str) {
            // Normaliza para separar caracteres e acentos, e remove acentos
            return Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
        }

        public static List<String> tokenizarEFiltrar(String texto) {
            List<String> termos = new ArrayList<>();
            
            // 1. Minúsculas e Sem Acentos
            String processado = removerAcentos(texto.toLowerCase());
            
            // 2. Tokenização: Divide a string em palavras e remove caracteres não-letras/dígitos
            String[] palavras = processado.split("[^a-z0-9]+");

            // 3. Filtragem de Stop Words
            for (String palavra : palavras) {
                if (!palavra.trim().isEmpty() && !StopWords.contains(palavra)) {
                    termos.add(palavra);
                }
            }
            return termos;
        } 

        public List<ParIDScore> buscarProdutos(String textoBusca) throws Exception {
    
            // 1. Pré-processamento dos termos de busca (Corrigindo Case e Acentos)
            List<String> termosBusca = tokenizarEFiltrar(textoBusca);
            if (termosBusca.isEmpty()) {
                return new ArrayList<>(); // Não há o que buscar
            }

            // Estrutura para somar os scores por ID
            java.util.Map<Integer, Double> scoresFinais = new java.util.HashMap<>();
            int N = listaInvertida.numeroEntidades(); // Total de produtos (entidades)

            // 2. Calcular TFxIDF para cada termo
            for (String termo : termosBusca) {
                ElementoLista[] ocorrencias = listaInvertida.read(termo); 
                
                // Frequência do Documento (df): quantos produtos têm esse termo
                int df = ocorrencias.length;
                if (df == 0) continue; 
                
                // 3. Calcular o IDF (log(N/df) + 1)
                double idf = Math.log10((double) N / df) + 1; // Usando log10 como no exemplo

                // 4. Acumular o Score (TF * IDF) para cada produto
                for (ElementoLista el : ocorrencias) {
                    double scoreAtual = el.getFrequencia() * idf;
                    scoresFinais.put(el.getId(), scoresFinais.getOrDefault(el.getId(), 0.0) + scoreAtual);
                }
            }

            // 5. Converter e Ordenar
            List<ParIDScore> resultados = new ArrayList<>();
            for (java.util.Map.Entry<Integer, Double> entry : scoresFinais.entrySet()) {
                resultados.add(new ParIDScore(entry.getKey(), entry.getValue())); 
            }

            // Ordenar por Score em ordem DECRESCENTE (Usando getScore para acessar o valor)
            resultados.sort((a, b) -> Double.compare(b.getScore(), a.getScore())); 
            
            return resultados;
    }
}
