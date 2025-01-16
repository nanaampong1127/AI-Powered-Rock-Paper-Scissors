# AI Powered Rock, Paper, Scissors

## Overview
This project is an interactive **Rock, Paper, Scissors** game powered by an **AI system** that uses a **Markov Chain** to predict the player's moves and improve its strategy over time. The AI adapts based on your gameplay patterns and saves its learning data for future games.

## Features
- **AI Learning**: The AI predicts player moves using a Markov Chain-based prediction matrix.
- **Data Persistence**: Save and load AI learning data to continue improving the AI across multiple sessions.
- **Interactive Gameplay**: Play against an AI that becomes increasingly challenging as it learns.
- **Customizable**: Easily modify the prediction matrix or file storage settings.

## How It Works
1. The AI uses a **3x3 prediction matrix** to track transitions between player moves (Rock, Paper, Scissors).
2. Each round, the AI updates the matrix based on your moves.
3. Using the matrix, the AI predicts your next move and selects a counter-strategy.
4. Learning data is stored in a file (`predictionData.txt`) and can be loaded for future sessions.

## Technologies Used
- **Java**: Core programming language.
- **Markov Chain**: For AI learning and move prediction.
- **File I/O**: To save and load the prediction matrix.

## Getting Started
### Prerequisites
- Java Development Kit (JDK) 8 or higher.
- A Java IDE or command-line environment.

### Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/rock-paper-scissors-ai.git
   ```
2. Navigate to the project directory:
   ```bash
   cd rock-paper-scissors-ai
   ```
3. Open the project in your preferred Java IDE or compile and run it using the command line.

### Running the Game
1. Compile the main application:
   ```bash
   javac H12CustomApp.java
   ```
2. Run the application:
   ```bash
   java H12CustomApp
   ```
3. Follow the on-screen prompts to play the game.

### Testing
To test the application:
1. Compile the test file:
   ```bash
   javac TestH12CustomApp.java
   ```
2. Run the test cases:
   ```bash
   java TestH12CustomApp
   ```

## Project Structure
```
rock-paper-scissors-ai/
|-- H12CustomApp.java          # Main application file
|-- TestH12CustomApp.java      # Test cases for the application
|-- predictionData.txt         # File for saving/loading AI learning data
```

## Key Methods
### H12CustomApp.java
- `getAiMove(int[][], int, Random)`: Determines the AI's move based on the prediction matrix.
- `updateMatrix(int[][], int, int)`: Updates the prediction matrix with the player's moves.
- `loadPredictionMatrix(String)`: Loads the prediction matrix from a file.
- `savePredictionMatrix(int[][], String)`: Saves the prediction matrix to a file.

### TestH12CustomApp.java
- `testH12CustomApp()`: Runs test cases for the application's core methods.

## Future Enhancements
- **Graphical User Interface (GUI)**: Add a visual interface for better user experience.
- **Advanced AI**: Incorporate more sophisticated algorithms to enhance prediction accuracy.
- **Multiplayer Mode**: Allow two players to compete against each other.
- **Mobile Compatibility**: Adapt the game for mobile platforms.

## Author
**Nana Ampong**  
Email: [nampong@wisc.edu](mailto:nampong@wisc.edu)

## License
This project is licensed under the MIT License. See the `LICENSE` file for details.

---

Enjoy the game and watch the AI get smarter with every round you play!
