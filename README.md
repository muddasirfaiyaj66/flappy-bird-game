# Flappy Bird Game

Welcome to my Flappy Bird game project! This game is built using Java, and you can run it locally on your machine. Follow the steps below to get started.

## Prerequisites

- Java Development Kit (JDK)
- Visual Studio Code (VS Code) or IntelliJ IDEA

## Installation

### Step 1: Install Java

1. **Download JDK**: Go to the [Oracle JDK download page](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html) and download the JDK for your operating system.
2. **Install JDK**: Follow the installation instructions for your OS.

   - **Windows**: Run the downloaded installer and follow the prompts.
   - **Mac**: Open the `.dmg` file and drag the JDK to your `Applications` folder.
   - **Linux**: Follow the instructions specific to your distribution (e.g., `sudo apt install openjdk-11-jdk` for Debian-based systems).

3. **Set JAVA_HOME**: Ensure that the `JAVA_HOME` environment variable is set correctly.

   - **Windows**:
     ```cmd
     setx JAVA_HOME "C:\Program Files\Java\jdk-11"
     setx PATH "%JAVA_HOME%\bin;%PATH%"
     ```
   - **Mac/Linux**:
     ```bash
     export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64
     export PATH=$JAVA_HOME/bin:$PATH
     ```

4. **Verify Installation**: Open a terminal or command prompt and run:
   ```sh
   java -version
### Step 2: Set Up Your IDE

### Visual Studio Code (VS Code)

1. **Install VS Code**: Download and install [Visual Studio Code](https://code.visualstudio.com/).
2. **Install Extensions**: Open VS Code and install the following extensions:
    - **Java Extension Pack** (which includes several useful Java tools)
3. **Clone the Repository**: Open a terminal and clone this repository:
    
    ```
    shCopy code
    git clone https://github.com/muddasirfaiyaj66/flappy-bird-game.git
    cd flappy-bird-game
    
    ```
    
4. **Open the Project**: Open VS Code and select `File > Open Folder`, then choose the cloned repository folder.
5. **Run the Game**: Open the main Java file (usually `Main.java`) and click the `Run` button in the top right corner of the editor.

### IntelliJ IDEA

1. **Install IntelliJ IDEA**: Download and install [IntelliJ IDEA](https://www.jetbrains.com/idea/).
2. **Clone the Repository**: Open a terminal and clone this repository:
    
    ```
    shCopy code
    git clone https://github.com/muddasirfaiyaj66/flappy-bird-game.git
    cd flappy-bird-game
    
    ```
    
3. **Open the Project**: Open IntelliJ IDEA and select `File > Open`, then choose the cloned repository folder.
4. **Run the Game**: Right-click the main Java file (usually `Main.java`) and select `Run 'Main'`.

## Running the Game

Once you have set up your IDE and cloned the repository, you can run the game by executing the main Java file. Follow the steps specific to your IDE above.