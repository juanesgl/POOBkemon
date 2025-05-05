# POOBkemon

## Descripción

Este es un proyecto de gestión de Pokemones, donde se gestionan atributos de diferentes tipos de Pokémon y se realizan simulaciones de batallas entre ellos.

## Requisitos

- **Java JDK 17** o superior
- **PowerShell** o terminal compatible
- **JUnit 5** para ejecutar las pruebas


## Cómo Compilar el Proyecto

1. Abre **PowerShell** en la raíz del proyecto.
2. Ejecuta el siguiente comando para compilar el código:

```powershell
javac -d bin -cp "libs/*" @(Get-ChildItem -Recurse -Filter *.java -Path src).FullName





