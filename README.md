# Keep Compass for ChestCommand

Plugin Minecraft Spigot/Paper 1.21 qui garde une boussole dans le dernier slot de la hotbar et **empêche toute duplication**.

## 📋 Fonctionnalités

- **Boussole automatique** : Une boussole est automatiquement placée dans le slot 8 (dernier de la hotbar)
- **Anti-duplication** : Bloque toute tentative de duplication de la boussole (inventaire, coffre, drag, shift-click)
- **Anti-drop** : Impossible de lâcher la boussole
- **Anti-déplacement** : Si vous posez un item sur la boussole, l'item drop au sol
- **Persistance après mort** : La boussole est conservée dans l'inventaire après la mort
- **Persistance** : La boussole revient après mort, respawn, reconnexion
- **Commande toggle** : Activez/désactivez le plugin avec /compasshotbar

## 🎮 Commandes

| Commande | Description |
|----------|-------------|
| /compasshotbar | Active ou désactive le plugin |
| /compasshotbar reload | Recharge la configuration |
| /compasshotbar give | Redonne une boussole à tous les joueurs |
| /compasshotbar toggle | Alias pour activer/désactiver |
| /compasshotbar status | Affiche le statut du plugin |

## 🔧 Compilation

```bash
mvn clean package
```

Le JAR sera dans `target/CompassHotbar-1.0.0.jar`.

## 📦 Installation

1. Compilez le plugin ou téléchargez le JAR depuis les [Releases](https://github.com/abelliardadresse-alt/Keep-Compass-for-ChestCommand/releases)
2. Placez le JAR dans le dossier `plugins/` de votre serveur Spigot/Paper 1.21
3. Redémarrez le serveur

## 📋 Permissions

- `compasshotbar.use` : Donne la boussole automatiquement (par défaut: true)
- `compasshotbar.command` : Permet d'utiliser les commandes admin (par défaut: op)

## ⚙️ Configuration

Modifiez `plugins/CompassHotbar/config.yml` :

```yaml
enabled: true
```
