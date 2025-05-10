# POOBkemon

## Description

POOBkemon is a Pokémon management project where various Pokémon attributes are managed, and battles between them can be simulated.

## Requirements

* **Java JDK 17** or higher.
* **PowerShell** (Windows) or a compatible terminal (macOS/Linux).

## Compilation

1.  Open your **terminal** (PowerShell on Windows, Terminal on macOS/Linux) in the project's **root** directory.
2.  Navigate to the `src` directory:
    ```bash
    cd src
    ```
3.  Compile the Java code, generating `.class` files in the `bin` folder:
    ```bash
    javac -d ../bin POOBkemonGUI.java
    ```
4.  If you have external libraries in a `libs` folder, include them during compilation:
    ```bash
    javac -d ../bin -cp "../libs/*" POOBkemonGUI.java
    ```

## Execution

From the project's **root** directory, run the application:

```bash
java -cp bin POOBkemonGUI
