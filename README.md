# Arkanoid - Jeu Java avec javaFX

Arkanoid est un jeu classique de casse-briques développé en Java utilisant la bibliothèque JavaFX. Le but du jeu est de contrôler un vaisseau pour faire rebondir une balle et détruire des briques tout en évitant que la balle tombe en bas de l'écran. Le jeu propose des éléments de gameplay comme des bonus et des power-ups, et garde un score en fonction des briques détruites et du temps passé en partie.

## Fonctionnalités

- **Déplacement du vaisseau** : Utilisez les touches `Q` et `D` pour déplacer le vaisseau à gauche et à droite.
- **Super Dash** : Appuyez sur la touche `SPACE` pour activer un super dash qui accélère le vaisseau.
- **Multiball** : Des bonus permettent d'ajouter plusieurs balles au jeu.
- **Bonus** : Vous pouvez récupérer des bonus pour agrandir le vaisseau ou ajouter des vies.
- **Musique de fond et effets sonores** : La musique de fond est jouée en boucle pendant toute la partie.
- **Vies et Game Over** : Le jeu se termine quand le joueur perd toutes ses vies.
- **Score** : Le score augmente à chaque brique détruite et est affiché en temps réel.

## Prérequis

Avant de pouvoir exécuter le projet, vous devez avoir installé :

- **Java 11 ou supérieur** : Le projet est compatible avec les versions récentes de Java.
- **JavaFX** : Vous devez avoir configuré JavaFX dans votre environnement de développement pour que l'application fonctionne correctement.
- **Bibliothèque audio et vidéo** : Le jeu utilise des fichiers audio et vidéo (format `.mp3` et `.mp4`) pour l'ambiance sonore et visuelle.

## Installation

1. Clonez ce repository :

   ```bash
   git clone https://github.com/Enzozo02/NovaBrick-Super-Smash.git
   ```

2. Ouvrez le projet dans votre IDE préféré (par exemple, IntelliJ IDEA, Eclipse).

3. Assurez-vous que **JavaFX** est bien configuré dans votre projet. Si vous utilisez IntelliJ IDEA, vous pouvez ajouter JavaFX en suivant [ce tutoriel](https://openjfx.io/).

4. Compilez et exécutez le projet avec la commande suivante :

   ```bash
   mvn clean install
   mvn javafx:run
   ```

   Ou directement depuis votre IDE.

## Utilisation

- **Lancement du jeu** : Lorsque vous démarrez le jeu, appuyez sur la touche `ENTER` pour commencer la partie.
- **Déplacement du vaisseau** : Utilisez les touches `Q` et `D` pour déplacer le vaisseau à gauche et à droite.
- **Super Dash** : Appuyez sur `SPACE` pour activer le super dash.
- **Gestion des vies** : Vous perdez une vie à chaque fois que la balle tombe sous l'écran. Le jeu se termine quand vous n'avez plus de vies restantes.
- **Score** : Le score est affiché en haut de l'écran et est mis à jour chaque fois qu'une brique est détruite.

## Structure du Code

Le projet est structuré de manière à faciliter l'extension et la maintenance du jeu :

- **`JeuArkanoid.java`** : Contient la logique du jeu, y compris la gestion des entrées clavier, des collisions, du score, et des bonus.
- **`ArkanoidVaisseau.java`** : Gère le vaisseau du joueur, son déplacement et ses interactions avec les balles et les briques.
- **`ArkanoidBall.java`** : Représente la balle du jeu, gère ses mouvements et ses collisions avec les éléments du jeu.
- **`ArkanoidBrick.java`** : Gère les briques, leur état (casse ou intact), et les interactions avec la balle.
- **`ArkanoidScore.java`** : Gère le score du jeu, y compris l'augmentation du score et la gestion du temps.
- **`ArkanoidBonus.java`** : Représente les bonus que le joueur peut récupérer pour améliorer son vaisseau, ajouter des vies ou multiplier les balles.

## Ressources

Le jeu utilise des ressources multimédia pour l'expérience visuelle et sonore, telles que :

- **`resources/video/background.mp4`** : Vidéo de fond du jeu.
- **`resources/audio/music.mp3`** : Musique de fond du jeu.
- **`resources/audio/bonus_sound.mp3`** : Son lorsque le joueur récupère un bonus.

## Contribution

Les contributions sont les bienvenues. Si vous souhaitez contribuer au projet, suivez ces étapes :

1. Fork ce repository.
2. Créez une branche pour votre fonctionnalité : `git checkout -b ma-fonctionnalite`.
3. Committez vos changements : `git commit -m 'Ajout de ma fonctionnalité'`.
4. Poussez la branche : `git push origin ma-fonctionnalite`.
5. Créez une pull request pour discuter de vos changements.

## Auteurs

- **Nom** : Développeur principal DEHAYE Enzo

## License

Ce projet est sous licence [MIT](https://opensource.org/licenses/MIT).
