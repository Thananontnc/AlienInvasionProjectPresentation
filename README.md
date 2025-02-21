# Alien Invasion Game - A Java-based Side-Scrolling Action Game

Alien Invasion is a dynamic side-scrolling action game where players control an alien character navigating through obstacles, battling soldiers, and collecting coins. The game features character customization, an in-game shop system, and progressive difficulty scaling.

The game combines classic platformer mechanics with shooting elements, offering players multiple character choices and upgradeable content. Players can double jump to avoid obstacles, shoot at enemies with a cooldown-based weapon system, and collect coins to unlock new alien characters. The game's difficulty increases over time through speed adjustments and spawn rates, providing an engaging and challenging experience.

## Repository Structure
```
.
├── README.md                   # Project documentation
└── src/                       # Source code directory
    ├── AlienInvasionHome.java # Main entry point and home screen
    ├── Bullet.java           # Bullet mechanics and rendering
    ├── CharacterSelection.java # Character selection screen and logic
    ├── Coin.java             # Coin collectible implementation
    ├── Game.java             # Core game loop and mechanics
    ├── Obstacle.java         # Obstacle generation and behavior
    ├── Player.java           # Player character controls and physics
    ├── Shop.java             # In-game shop system
    ├── Soldier.java          # Enemy soldier behavior and AI
    └── Utils/                # Utility functions directory
        └── GameUtils.java    # Game helper functions
```

## Usage Instructions
### Prerequisites
- Java Development Kit (JDK) 8 or higher
- Java Runtime Environment (JRE)
- Java Swing support (included in standard JDK)

### Installation
1. Clone the repository:
```bash
git clone <repository-url>
```

2. Navigate to the project directory:
```bash
cd alien-invasion
```

3. Compile the Java files:
```bash
javac src/*.java
```

### Quick Start
1. Run the game:
```bash
java -cp src AlienInvasionHome
```

2. From the home screen, you can:
   - Click "Start Game" to begin playing
   - Select "Character Selection" to choose your alien
   - Visit the "Shop" to purchase new characters

### More Detailed Examples
1. Character Movement:
```java
Space - Jump (double jump available)
Down Arrow - Crouch
Enter - Shoot
```

2. Shop System:
- Collect coins during gameplay
- Visit the shop to purchase new characters:
  - Alien Green (500 coins)
  - Alien Red (700 coins)

### Troubleshooting
1. Game Not Starting
- Error: "Error loading image files"
  - Verify all asset files are present in the src/Assets/ directory
  - Check file permissions
  - Ensure correct file paths in the code

2. Performance Issues
- Symptom: Game running slowly
  - Solution: Close other applications
  - Check system requirements
  - Verify Java version compatibility

3. Character Selection Issues
- Error: "Selected character not loading"
  - Verify character assets exist
  - Check character name spelling in selection
  - Ensure proper asset file permissions

## Data Flow
The game operates on a continuous loop that handles player input, updates game state, and renders the display.

```ascii
[User Input] -> [Player Controller] -> [Game State]
                                           |
[Renderer] <- [Collision Detection] <- [Physics Engine]
     |
     v
[Display]
```

Component Interactions:
1. Player input is captured through KeyListener events
2. Game loop updates player position and state
3. Collision detection checks for interactions between:
   - Player and obstacles
   - Player and coins
   - Bullets and enemies
   - Player and enemy bullets
4. Physics engine handles:
   - Gravity effects
   - Jump mechanics
   - Projectile movement
5. Game state updates score and coin collection
6. Renderer draws all game elements to the screen
7. Shop system manages character unlocks and purchases
