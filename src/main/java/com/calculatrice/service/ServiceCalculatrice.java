
package com.calculatrice.service;

import org.springframework.stereotype.Service;

@Service
public class ServiceCalculatrice {

    public double calculer(String expression) throws Exception {
        if (expression == null) {
            throw new IllegalArgumentException("L'expression ne peut pas être null");
        }

        expression = expression.trim().replaceAll("\\s+", "");

        if (expression.isEmpty()) {
            throw new IllegalArgumentException("L'expression ne peut pas être vide");
        }

        if (expression.length() > 512) {
            throw new IllegalArgumentException("Expression trop longue (512 caractères maximum)");
        }

        Parser parser = new Parser(expression);
        double resultat = parser.parseExpression();

        if (!parser.estTermine()) {
            throw new IllegalArgumentException("Expression invalide");
        }

        if (!Double.isFinite(resultat)) {
            throw new IllegalArgumentException("Résultat non défini ou trop grand");
        }
        return resultat;
    }

    private static class Parser {

        private final String expression;
        private int position = 0;

        public Parser(String expression) {
            this.expression = expression;
        }

        public double parseExpression() {
            double resultat = parseTerme();

            while (position < expression.length()) {
                char operateur = expression.charAt(position);

                if (operateur == '+') {
                    position++;
                    resultat += parseTerme();

                } else if (operateur == '-') {
                    position++;
                    resultat -= parseTerme();

                } else {
                    break;
                }
            }

            return resultat;
        }

        private double parseTerme() {
            double resultat = parseSigne();

            while (position < expression.length()) {
                char operateur = expression.charAt(position);

                if (operateur == '*') {
                    position++;
                    resultat *= parseSigne();

                } else if (operateur == '/') {
                    position++;

                    double droite = parseSigne();

                    if (droite == 0) {
                        throw new ArithmeticException(
                                "Division par zéro impossible"
                        );
                    }

                    resultat /= droite;

                } else {
                    break;
                }
            }

            return resultat;
        }

        private double parsePuissance() {
            double resultat = parseFacteur();

            if (position < expression.length()
                    && expression.charAt(position) == '^') {

                position++;

                double exposant = parseSigne();

                resultat = Math.pow(resultat, exposant);
            }

            return resultat;
        }

        private double parseSigne() {
            if (position < expression.length() && expression.charAt(position) == '-') {
                position++;
                return -parseSigne();
            }
            if (position < expression.length() && expression.charAt(position) == '+') {
                position++;
                return parseSigne();
            }
            return parsePuissance();
        }

        private double parseFacteur() {

            if (position >= expression.length()) {
                throw new IllegalArgumentException("Expression invalide");
            }
            if (expression.charAt(position) == '(') {

                position++;

                double resultat = parseExpression();

                if (position >= expression.length()
                        || expression.charAt(position) != ')') {

                    throw new IllegalArgumentException(
                            "Parenthèse fermante manquante"
                    );
                }

                position++;

                return resultat;
            }
            if (expression.startsWith("sqrt", position)) {
                position += 4;

                double valeur = parseFonction();

                if (valeur < 0) {
                    throw new IllegalArgumentException(
                            "Racine carrée d'un nombre négatif impossible"
                    );
                }

                return Math.sqrt(valeur);
            }

            if (expression.startsWith("sin", position)) {
                position += 3;

                double valeur = parseFonction();

                return Math.sin(Math.toRadians(valeur));
            }

            if (expression.startsWith("cos", position)) {
                position += 3;

                double valeur = parseFonction();

                return Math.cos(Math.toRadians(valeur));
            }

            if (expression.startsWith("tan", position)) {
                position += 3;

                double valeur = parseFonction();

                if (Math.abs(valeur % 180) == 90) {
                    throw new IllegalArgumentException("Tangente non définie pour cet angle");
                }
                return Math.tan(Math.toRadians(valeur));
            }

            if (expression.startsWith("log", position)) {
                position += 3;

                double valeur = parseFonction();

                return Math.log10(valeur);
            }

            if (expression.startsWith("ln", position)) {
                position += 2;

                double valeur = parseFonction();

                return Math.log(valeur);
            }

            return parseNombre();
        }

        private double parseFonction() {

            if (position >= expression.length()
                    || expression.charAt(position) != '(') {

                throw new IllegalArgumentException(
                        "Parenthèses attendues après la fonction"
                );
            }

            position++;

            double resultat = parseExpression();

            if (position >= expression.length()
                    || expression.charAt(position) != ')') {

                throw new IllegalArgumentException(
                        "Parenthèse fermante manquante"
                );
            }

            position++;

            return resultat;
        }

        private double parseNombre() {

            int debut = position;
            boolean point = false;

            while (position < expression.length()) {

                char caractere = expression.charAt(position);

                if (Character.isDigit(caractere)) {
                    position++;

                } else if (caractere == '.') {

                    if (point) {
                        throw new IllegalArgumentException(
                                "Nombre décimal invalide"
                        );
                    }

                    point = true;
                    position++;

                } else {
                    break;
                }
            }

            if (debut == position) {
                throw new IllegalArgumentException(
                        "Expression invalide à la position " + position
                );
            }

            if (position < expression.length()
                    && (expression.charAt(position) == 'e' || expression.charAt(position) == 'E')) {
                position++;
                if (position < expression.length()
                        && (expression.charAt(position) == '+' || expression.charAt(position) == '-')) {
                    position++;
                }
                int debutExposant = position;
                while (position < expression.length() && Character.isDigit(expression.charAt(position))) {
                    position++;
                }
                if (debutExposant == position) {
                    throw new IllegalArgumentException("Exposant invalide");
                }
            }

            return Double.parseDouble(
                    expression.substring(debut, position)
            );
        }

        public boolean estTermine() {
            return position == expression.length();
        }
    }
}

