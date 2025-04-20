package com.github.victoraaquino;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Pattern;

public class TermoCLI {

    public static void main(String[] args) throws IOException, InterruptedException {

        String conteudo = Files.readString(Paths.get("palavras_5_letras.txt"));
        String[] palavras = Arrays.stream(conteudo.split(","))
                .map(String::trim)
                .filter(p -> !p.isEmpty())
                .toArray(String[]::new);

        Random random = new Random();
        String word = palavras[random.nextInt(palavras.length)].toUpperCase();

        // System.out.println("DEBUG: " + word);

        String normalizer = Normalizer.normalize(word, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        word = pattern.matcher(normalizer).replaceAll("");

        // System.out.println("DEBUG: normalized " + word);

        int attempts = 6;
        System.out.println("Advinhe a palavra com " + word.length() + " letras");
        System.out.println("*********** REGRA ***********");
        System.out.println("Se a letra estiver MAIÚSCULA, quer dizer que você acertou a posição da letra!");
        System.out.println(
                "Se a letra estiver MINÚSCULA, quer dizer que você acertou a letra, porém está na posição errado!");
        System.out.println("Se aparecer um _, quer dizer que não tem aquela letra!");
        System.out.println("NÃO UTILIZE ACENTOS");
        System.out.println("Boa sorte!");
        System.out.println("Tentativas restantes: " + attempts);

        System.out.println("Escreva a resposta:");
        boolean inGame = true;
        boolean isWinner = false;
        Scanner scanner = new Scanner(System.in);

        List<String> attemptsList = new ArrayList<>();
        Map<String, String> lettersUsed = new HashMap<String, String>();

        while (inGame || attempts >= 0) {
            String response = scanner.nextLine().toUpperCase();

            if (response.length() != word.length()) {
                System.out.println("Palavra inválida!");
                System.out.println("A palavra deve conter " + word.length() + " caracteres");
                continue;
            }

            System.out.println("*************");

            String attempt = "";
            attempt += "* ";
            for (int i = 0; i < response.length(); i++) {
                String letter = String.valueOf(response.charAt(i));
                lettersUsed.put(letter.toLowerCase(), letter.toLowerCase());
                if (word.contains(letter)) {
                    // Possui a letra
                    if (word.charAt(i) == response.charAt(i)) {
                        // Possui a letra e está na mesma posição
                        attempt += (letter.toUpperCase() + " ");
                        continue;
                    }
                    attempt += (letter.toLowerCase() + " ");
                } else {
                    // Não possui a letra
                    attempt += ("_ ");
                }
            }
            attempt += "*";

            attemptsList.add(attempt);

            attemptsList.forEach(System.out::println);

            System.out.println("*************");

            if (word.equals(response)) {
                inGame = false;
                isWinner = true;
                System.out.println("Parabéns você acertou!");
                continue;
            }

            System.out.println("Tentativas restantes: " + attempts);
            attempts--;

            System.out.println("Letras usadas:");
            lettersUsed.forEach((key, value) -> System.out.print(key + " "));
            System.out.println();
        }

        if (!isWinner) {
            System.out.println("Poxa tente denovo :(");
            System.out.println("A palavra era: " + word);
        }
    }
}