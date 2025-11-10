package src.presenteFacil.aeds3;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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


}
