package com.example.arkanoid.function;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ArkanoidVaisseau {
    // Déclaration du vaisseau, vitesse, vies et autres attributs liés au vaisseau
    private Rectangle vaisseau;
    private double vitesse = 7; // Vitesse par défaut du vaisseau
    private int vies = 4; // Nombre de vies initiales
    private boolean superDashActive = false; // Indique si le super dash est actif
    private long superDashEndTime = 0; // Temps de fin du super dash
    private long superDashCooldownEndTime = 0; // Temps de fin du cooldown du super dash
    private boolean isExpanded = false; // Indique si le vaisseau est élargi
    private long expandEndTime = 0; // Temps de fin de l'expansion
    private static final long SUPER_DASH_DURATION = 5000000000L; // Durée du super dash (5 secondes)
    private static final long SUPER_DASH_COOLDOWN = 30000000000L; // Temps de recharge du super dash (30 secondes)
    private static final long EXPAND_DURATION = 10000000000L; // Durée de l'expansion (10 secondes)

    // Constructeur du vaisseau
    public ArkanoidVaisseau() {
        // Création du vaisseau (rectangle) de taille 100x20, de couleur blanche
        vaisseau = new Rectangle(100, 20, Color.WHITE);
        vaisseau.setX(350); // Position X initiale du vaisseau
        vaisseau.setY(920); // Position Y initiale du vaisseau
    }

    // Getter pour récupérer le vaisseau
    public Rectangle getVaisseau() {
        return vaisseau;
    }

    // Déplacer le vaisseau à gauche, si ce n'est pas en dehors de l'écran
    public void moveLeft() {
        if (vaisseau.getX() > 0) {
            vaisseau.setX(vaisseau.getX() - vitesse);
        }
    }

    // Déplacer le vaisseau à droite, si ce n'est pas en dehors de l'écran
    public void moveRight(double sceneWidth) {
        if (vaisseau.getX() + vaisseau.getWidth() < sceneWidth) {
            vaisseau.setX(vaisseau.getX() + vitesse);
        }
    }

    // Activer le super dash
    public void activateSuperDash() {
        long currentTime = System.nanoTime(); // Temps actuel
        if (!superDashActive && currentTime >= superDashCooldownEndTime) {
            superDashActive = true; // Activer le super dash
            vitesse = 14; // Doubler la vitesse du vaisseau
            superDashEndTime = currentTime + SUPER_DASH_DURATION; // Temps de fin du super dash
            superDashCooldownEndTime = superDashEndTime + SUPER_DASH_COOLDOWN; // Temps de fin du cooldown du super dash
        }
    }

    // Activer l'expansion du vaisseau
    public void activateExpand() {
        isExpanded = true; // Marquer le vaisseau comme agrandi
        expandEndTime = System.nanoTime() + EXPAND_DURATION; // Calculer le temps de fin de l'expansion
        vaisseau.setWidth(400); // Changer la largeur du vaisseau
    }

    // Mettre à jour l'état du vaisseau (vitesse, expansion, etc.)
    public void update() {
        long currentTime = System.nanoTime(); // Temps actuel

        // Désactiver le super dash si le temps est écoulé
        if (superDashActive && currentTime >= superDashEndTime) {
            superDashActive = false;
            vitesse = 7; // Rétablir la vitesse normale
        }

        // Réduire la taille du vaisseau si l'expansion est terminée
        if (isExpanded && currentTime >= expandEndTime) {
            isExpanded = false;
            vaisseau.setWidth(100); // Réduire la largeur du vaisseau
        }
    }

    // Récupérer le nombre de vies restantes
    public int getVies() {
        return vies;
    }

    // Diminuer une vie
    public void perdreVie() {
        if (vies > 0) {
            vies--; // Retirer une vie
            mettreAJourCouleur(); // Mettre à jour la couleur du vaisseau selon le nombre de vies
        }
    }

    // Ajouter une vie
    public void ajouterVie() {
        if (vies < 5) { // Limite à 5 vies
            vies++; // Ajouter une vie
            mettreAJourCouleur(); // Mettre à jour la couleur du vaisseau
        }
    }

    // Mettre à jour la couleur du vaisseau selon le nombre de vies
    private void mettreAJourCouleur() {
        switch (vies) {
            case 5 -> vaisseau.setFill(Color.BLUE); // Bleu pour 5 vies
            case 4 -> vaisseau.setFill(Color.WHITE); // Blanc pour 4 vies
            case 3 -> vaisseau.setFill(Color.GREEN); // Vert pour 3 vies
            case 2 -> vaisseau.setFill(Color.YELLOW); // Jaune pour 2 vies
            case 1 -> vaisseau.setFill(Color.RED); // Rouge pour 1 vie
        }
    }

    // Vérifier si le vaisseau est mort (pas de vies restantes)
    public boolean estMort() {
        return vies <= 0;
    }

    // Vérifier si le super dash est actuellement actif
    public boolean isSuperDashActive() {
        return superDashActive;
    }

    // Récupérer le temps de fin du cooldown du super dash
    public long getSuperDashCooldownEndTime() {
        return superDashCooldownEndTime;
    }

    // Récupérer le temps de fin du super dash
    public long getSuperDashEndTime() {
        return superDashEndTime;
    }
}
