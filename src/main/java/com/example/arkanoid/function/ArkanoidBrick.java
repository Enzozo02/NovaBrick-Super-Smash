package com.example.arkanoid.function;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ArkanoidBrick {
    private Rectangle brick; // Représente le rectangle graphique de la brique
    private int durability;  // Durabilité de la brique (combien de fois elle peut être frappée)

    // Constructeur qui crée une brique à une position donnée avec une largeur, une hauteur et une durabilité
    public ArkanoidBrick(double x, double y, double width, double height, int durability) {
        this.durability = durability;

        brick = new Rectangle(width, height); // Crée un rectangle pour la brique
        brick.setX(x); // Positionner la brique sur l'axe X
        brick.setY(y); // Positionner la brique sur l'axe Y

        updateColor(); // Met à jour la couleur en fonction de la durabilité
    }

    // Retourne le rectangle représentant la brique
    public Rectangle getBrick() {
        return brick;
    }

    // Méthode appelée lorsqu'une brique est frappée
    public void hit() {
        if (durability > 0) {
            durability--; // Réduire la durabilité de la brique
            updateColor(); // Mettre à jour la couleur de la brique en fonction de sa nouvelle durabilité
        }
    }

    // Vérifie si la brique est cassée (si sa durabilité est inférieure ou égale à 0)
    public boolean isBroken() {
        return durability <= 0;
    }

    // Met à jour la couleur de la brique en fonction de sa durabilité
    private void updateColor() {
        switch (durability) {
            case 3:
                brick.setFill(Color.RED); // Durabilité 3, couleur rouge
                break;
            case 2:
                brick.setFill(Color.ORANGE); // Durabilité 2, couleur orange
                break;
            case 1:
                brick.setFill(Color.YELLOW); // Durabilité 1, couleur jaune
                break;
            default:
                brick.setFill(Color.TRANSPARENT); // Durabilité 0 ou moins, la brique devient transparente
                break;
        }
    }

    // Méthode statique pour générer une grille de briques
    public static List<ArkanoidBrick> generateBricks(Pane gameLayout, int rows, int cols, double brickWidth, double brickHeight) {
        List<ArkanoidBrick> bricks = new ArrayList<>(); // Liste pour stocker toutes les briques
        Random random = new Random(); // Générateur de nombres aléatoires pour définir la durabilité

        // Création des briques en parcourant les lignes et les colonnes
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                double x = col * (brickWidth + 5) + 50; // Position X de la brique
                double y = row * (brickHeight + 5) + 50; // Position Y de la brique
                int randomDurability = random.nextInt(3) + 1; // Durabilité aléatoire entre 1 et 3
                ArkanoidBrick brick = new ArkanoidBrick(x, y, brickWidth, brickHeight, randomDurability); // Créer la brique
                bricks.add(brick); // Ajouter la brique à la liste
                gameLayout.getChildren().add(brick.getBrick()); // Ajouter la brique à la scène de jeu
            }
        }
        return bricks; // Retourner la liste de toutes les briques créées
    }
}
