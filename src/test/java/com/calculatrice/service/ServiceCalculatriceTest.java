package com.calculatrice.service;

import org.junit.jupiter.api.Test;



import static org.junit.jupiter.api.Assertions.*;


public class ServiceCalculatriceTest {


    private final ServiceCalculatrice serviceCalculatrice = new ServiceCalculatrice();

    @Test
    public void testAddition() throws Exception {
        assertEquals(7.0, serviceCalculatrice.calculer("2+5"));
    }

    @Test
    public void testSoustraction() throws Exception {
        assertEquals(7.0, serviceCalculatrice.calculer("10-3"));
    }

    @Test
    public void testMultiplication() throws Exception {
        assertEquals(20.0, serviceCalculatrice.calculer("5*4"));
    }

    @Test
    public void testDivision() throws Exception {
        assertEquals(4.0, serviceCalculatrice.calculer("20/5"));
    }

    @Test
    public void testPuissance() throws Exception {
        assertEquals(4.0, serviceCalculatrice.calculer("2^2"));
    }

    @Test
    public void testRacineCarre() throws Exception {
        assertEquals(4.0, serviceCalculatrice.calculer("sqrt(16)"));
    }

    @Test
    public void testParentheses() throws Exception {
        assertEquals(14.0, serviceCalculatrice.calculer("(2+5)*2"));
    }

    @Test
    public void testPrioriteOperateurs() throws Exception {
        assertEquals(17.0, serviceCalculatrice.calculer("2+5*3"));
    }

    @Test
    public void testDivisionParZero() {
        assertThrows(ArithmeticException.class, () -> {
            serviceCalculatrice.calculer("10/0");
        });
    }

    @Test
    public void testRacineNegative() {
        assertThrows(IllegalArgumentException.class, () -> {
            serviceCalculatrice.calculer("sqrt(-4)");
        });
    }

    @Test
    public void testExpressionVide() {
        assertThrows(IllegalArgumentException.class, () -> {
            serviceCalculatrice.calculer("");
        });
    }

    @Test
    public void testSin() throws Exception {
        double result = serviceCalculatrice.calculer("sin(0)");
        assertEquals(0.0, result, 0.001);
    }

    @Test
    public void testCos() throws Exception {
        double result = serviceCalculatrice.calculer("cos(0)");
        assertEquals(1.0, result, 0.001);
    }

    @Test
    public void testLog() throws Exception {
        double result = serviceCalculatrice.calculer("log(100)");
        assertEquals(2.0, result, 0.001);
    }

    @Test
    public void testLn() throws Exception {
        double result = serviceCalculatrice.calculer("ln(2.718281828)");
        assertEquals(1.0, result, 0.001);
    }

    @Test
    public void testNombresDecimaux() throws Exception {
        assertEquals(3.5, serviceCalculatrice.calculer("1.5+2.0"));
    }
    @Test
    public void testPrioriteSigneEtPuissance() throws Exception {
        assertEquals(-4.0, serviceCalculatrice.calculer("-2^2"));
        assertEquals(4.0, serviceCalculatrice.calculer("(-2)^2"));
        assertEquals(0.25, serviceCalculatrice.calculer("2^-2"));
        assertEquals(512.0, serviceCalculatrice.calculer("2^3^2"));
    }

    @Test
    public void testNotationScientifique() throws Exception {
        assertEquals(2001.0, serviceCalculatrice.calculer("2e3+1"));
        assertEquals(0.001, serviceCalculatrice.calculer("1.0E-3"));
        assertThrows(IllegalArgumentException.class, () -> serviceCalculatrice.calculer("2e+"));
    }

    @Test
    public void testResultatsNonFinis() {
        for (String expression : new String[]{"ln(0)", "log(-1)", "10^1000", "(-1)^0.5"}) {
            assertThrows(IllegalArgumentException.class, () -> serviceCalculatrice.calculer(expression));
        }
    }

    @Test
    public void testExpressionsInvalides() {
        for (String expression : new String[]{"2+", "(2+3", "2..3", "sin 30", "2e", "1".repeat(513)}) {
            assertThrows(IllegalArgumentException.class, () -> serviceCalculatrice.calculer(expression));
        }
    }
    @Test
    public void testTangenteNonDefinie() throws Exception {
        assertEquals(1.0, serviceCalculatrice.calculer("tan(45)"), 0.000001);
        for (String expression : new String[]{"tan(90)", "tan(-90)", "tan(270)"}) {
            assertThrows(IllegalArgumentException.class, () -> serviceCalculatrice.calculer(expression));
        }
    }}


