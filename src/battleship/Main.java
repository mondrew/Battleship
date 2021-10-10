package battleship;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    private static final int FIELD_SIZE = 10;

    private static final int AIRCRAFT_CARRIER_SIZE = 5;
    private static final int BATTLESHIP_SIZE = 4;
    private static final int SUBMARINE_SIZE = 3;
    private static final int CRUISER_SIZE = 3;
    private static final int DESTROYER_SIZE = 2;

    private static final int TOTAL_SHIP_CELLS = AIRCRAFT_CARRIER_SIZE + BATTLESHIP_SIZE + SUBMARINE_SIZE +
            CRUISER_SIZE + DESTROYER_SIZE;
    private static final String AIRCRAFT_CARRIER_NAME = "Aircraft carrier";
    private static final String BATTLESHIP_NAME = "Battleship";
    private static final String SUBMARINE_NAME = "Submarine";
    private static final String CRUISER_NAME = "Cruiser";
    private static final String DESTROYER_NAME = "Destroyer";

    private static String[][] firstPlayerField = new String[FIELD_SIZE + 1][FIELD_SIZE + 1];
    private static String[][] firstPlayerFogField = new String[FIELD_SIZE + 1][FIELD_SIZE + 1];

    private static String[][] secondPlayerField = new String[FIELD_SIZE + 1][FIELD_SIZE + 1];
    private static String[][] secondPlayerFogField = new String[FIELD_SIZE + 1][FIELD_SIZE + 1];

    private static Integer firstPlayerScore = 0;
    private static Integer secondPlayerScore = 0;

    private static Scanner scanner = new Scanner(System.in);

    public static void createEmptyField(String[][] field) {
        field[0][0] = " ";
        field[0][FIELD_SIZE] = "10";
        char a = 'A';
        for (int i = 1; i < FIELD_SIZE; i++) {
            field[0][i] = String.valueOf((char) (i + '0'));
        }
        for (int i = 1; i < FIELD_SIZE + 1; i++) {
            field[i][0] = String.valueOf(a++);
        }
        for (int i = 1; i < FIELD_SIZE + 1; i++) {
            for (int j = 1; j < FIELD_SIZE + 1; j++) {
                field[i][j] = "~";
            }
        }
    }

    public static void printField(String[][] field) {
        for (int i = 0; i < FIELD_SIZE + 1; i++) {
            for (int j = 0; j < FIELD_SIZE + 1; j++) {
                System.out.print(field[i][j]);
                if (j < FIELD_SIZE) {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }

    public static boolean hasNoNeighbours(String[][] playerField, int y, int x) {
        if (x - 1 >= 0) {
            if (playerField[y][x - 1].equals("O")) {
                return false;
            }
        }
        if (x + 1 <= FIELD_SIZE) {
            if (playerField[y][x + 1].equals("O")) {
                return false;
            }
        }
        if (y - 1 >= 0) {
            if (playerField[y - 1][x].equals("O")) {
                return false;
            }
        }
        if (y + 1 <= FIELD_SIZE) {
            if (playerField[y + 1][x].equals("O")) {
                return false;
            }
        }
        return true;
    }

    public static boolean isValidShipCoordinates(String[][] playerField, String[] coordinates,
                                                 int size, String name, int[] coordX, int[] coordY) {
        if (coordinates.length != 2) {
            System.out.println("Error! Wrong number of coordinates. Try again:\n");
            return false;
        }
        for (int i = 0; i < 2; i++) {
            int length = coordinates[i].length();
            if ((length == 2 || length == 3) &&
                    "ABCDEFGHIJ".indexOf(coordinates[i].charAt(0)) != -1) {
                coordY[i] = coordinates[i].charAt(0) - 'A' + 1;
                for (int j = 1; j < length; j++) {
                    if ("0123456789".indexOf(coordinates[i].charAt(j)) == -1) {
                        System.out.println("Error! Wrong coordinates. Try again:\n");
                        return false;
                    }
                    coordX[i] = coordX[i] * 10 + coordinates[i].charAt(j) - '0';
                }
            } else {
                System.out.println("Error! Wrong coordinates. Try again:\n");
                return false;
            }
        }
        if (coordX[0] < 11 && coordX[1] < 11 && coordY[0] < 11 && coordY[1] < 11 &&
                (coordX[0] == coordX[1] || coordY[0] == coordY[1])) {
            if (Math.max(Math.abs(coordX[0] - coordX[1]) + 1, Math.abs(coordY[0] - coordY[1]) + 1) != size) {
                System.out.println("Error! Wrong length of the " + name + "! Try again:\n");
                return false;
            }
            if (coordX[0] == coordX[1]) {
                for (int i = Math.min(coordY[0], coordY[1]);
                     i < Math.max(coordY[0], coordY[1]) + 1; i++) {
                    if (!playerField[i][coordX[0]].equals("~") ||
                            !hasNoNeighbours(playerField, i, coordX[0])) {
                        System.out.println("Error! You placed it too close to another one. Try again:\n");
                        return false;
                    }
                }
            } else {
                for (int i = Math.min(coordX[0], coordX[1]);
                     i < Math.max(coordX[0], coordX[1]) + 1; i++) {
                    if (!playerField[coordY[0]][i].equals("~") ||
                            !hasNoNeighbours(playerField, coordY[0], i)) {
                        System.out.println("Error! You placed it too close to another one. Try again:\n");
                        return false;
                    }
                }
            }
        } else {
            System.out.println("Error! Wrong ship location! Try again:\n");
            return false;
        }
        return true;
    }

    public static void placeShip(String[][] playerField, String shipName, int shipSize) {
        int[] coordX = new int[2];
        int[] coordY = new int[2];
        System.out.printf("Enter the coordinates of the %s (%d cells):\n\n", shipName, shipSize);
        String[] coordinates = scanner.nextLine().split(" ");
        System.out.println();

        while (!isValidShipCoordinates(playerField, coordinates, shipSize, shipName, coordX, coordY)) {
            coordX[0] = 0;
            coordX[1] = 0;
            coordY[0] = 0;
            coordY[1] = 0;
            coordinates = scanner.nextLine().split(" ");
        }
        if (coordX[0] == coordX[1]) {
            for (int i = Math.min(coordY[0], coordY[1]);
                 i < Math.max(coordY[0], coordY[1]) + 1; i++) {
                playerField[i][coordX[0]] = "O";
            }
        } else {
            for (int i = Math.min(coordX[0], coordX[1]);
                 i < Math.max(coordX[0], coordX[1]) + 1; i++) {
                playerField[coordY[0]][i] = "O";
            }
        }
        printField(playerField);
    }

    public static void fillField(String[][] playerField) {
        int n = playerField == firstPlayerField ? 1 : 2;
        System.out.printf("Player %d, place your ships on the game field\n\n", n);
        printField(playerField);
        System.out.println();
        placeShip(playerField, AIRCRAFT_CARRIER_NAME, AIRCRAFT_CARRIER_SIZE);
        placeShip(playerField, BATTLESHIP_NAME, BATTLESHIP_SIZE);
        placeShip(playerField, SUBMARINE_NAME, SUBMARINE_SIZE);
        placeShip(playerField, CRUISER_NAME, CRUISER_SIZE);
        placeShip(playerField, DESTROYER_NAME, DESTROYER_SIZE);
    }

    public static int[] convertAlphaNumToNumbersCoords(String alphaNumCoords) {
        int length = alphaNumCoords.length();
        int[] numCoords = new int[2];

        if ((length == 2 || length == 3) &&
                "ABCDEFGHIJ".indexOf(alphaNumCoords.charAt(0)) != -1) {
            numCoords[0] = alphaNumCoords.charAt(0) - 'A' + 1;
            for (int j = 1; j < length; j++) {
                if ("0123456789".indexOf(alphaNumCoords.charAt(j)) == -1) {
                    System.out.println("Error! You entered the wrong coordinates! Try again:\n");
                    return null;
                }
                numCoords[1] = numCoords[1] * 10 + alphaNumCoords.charAt(j) - '0';
            }
        } else {
            System.out.println("Error! You entered the wrong coordinates! Try again:\n");
            return null;
        }
        if (numCoords[0] > FIELD_SIZE || numCoords[1] > FIELD_SIZE) {
            System.out.println("Error! You entered the wrong coordinates! Try again:\n");
            return null;
        }
        return numCoords;
    }

    public static boolean isSunk(String[][] playerField, int y, int x) {

        if (x - 1 >= 0 && !playerField[y][x - 1].equals("~")) {
            int i = x;
            while (i >= 0 && playerField[y][i].equals("X")) {
                i--;
            }
            if (i >= 0 && playerField[y][i].equals("O")) {
                return false;
            }
        }
        if (x + 1 <= FIELD_SIZE && !playerField[y][x + 1].equals("~")) {
            int i = x;
            while (i <= FIELD_SIZE && playerField[y][i].equals("X")) {
                i++;
            }
            if (i <= FIELD_SIZE && playerField[y][i].equals("O")) {
                return false;
            }
        }
        if (y - 1 >= 0 && !playerField[y - 1][x].equals("~")) {
            int i = y;
            while (i >= 0 && playerField[i][x].equals("X")) {
                i--;
            }
            if (i >= 0 && playerField[i][x].equals("O")) {
                return false;
            }
        }
        if (y + 1 <= FIELD_SIZE && !playerField[y + 1][x].equals("~")) {
            int i = y;
            while (i <= FIELD_SIZE && playerField[i][x].equals("X")) {
                i++;
            }
            if (i <= FIELD_SIZE && playerField[i][x].equals("O")) {
                return false;
            }
        }
        return true;
    }

    public static void promptEnterKey() {
        System.out.println("Press Enter and pass the move to another player");
        try {
            System.in.read();
            System.out.println();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean takeShot(int playerNumber) {
        String[][] playerField = playerNumber == 1 ? firstPlayerField : secondPlayerField;
        String[][] enemyField = playerNumber == 1 ? secondPlayerField : firstPlayerField;
        String[][] enemyFogField = playerNumber == 1 ? secondPlayerFogField : firstPlayerFogField;

        int[] numCoords;
        printField(enemyFogField);
        System.out.println("---------------------");
        printField(playerField);
        System.out.println();
        System.out.printf("Player %d, it's your turn:\n\n", playerNumber);

        while (true) {
            String alphaNumCoords = scanner.nextLine();
            numCoords = convertAlphaNumToNumbersCoords(alphaNumCoords);

            if (numCoords == null) {
                continue;
            }
            else if (enemyField[numCoords[0]][numCoords[1]].equals("O")) {
                enemyField[numCoords[0]][numCoords[1]] = "X";
                enemyFogField[numCoords[0]][numCoords[1]] = "X";
                if (playerNumber == 1) {
                    firstPlayerScore++;
                } else {
                    secondPlayerScore++;
                }
                Integer playerScore = playerNumber == 1 ? firstPlayerScore : secondPlayerScore;
                if (playerScore == TOTAL_SHIP_CELLS) {
                    System.out.println("You sank the last ship. You won. Congratulations!");
                    return true;
                }
                String msg = isSunk(enemyField, numCoords[0], numCoords[1]) ?
                        "You sank a ship!" : "You hit a ship!";
                System.out.println(msg);
                return false;
            } else if (enemyField[numCoords[0]][numCoords[1]].equals("X")) {
                System.out.println("You missed!\n");
                return false;
            } else {
                enemyField[numCoords[0]][numCoords[1]] = "M";
                enemyFogField[numCoords[0]][numCoords[1]] = "M";
                System.out.println("You missed!\n");
                return false;
            }
        }
    }

    public static void startGame() {
        int playerNumber = 1;

        while (!takeShot(playerNumber)) {
            promptEnterKey();
            playerNumber = playerNumber == 1 ? 2 : 1;
        }
    }

    public static void createFields() {
        createEmptyField(firstPlayerField);
        createEmptyField(firstPlayerFogField);
        createEmptyField(secondPlayerField);
        createEmptyField(secondPlayerFogField);
    }

    public static void main(String[] args) {

        createFields();
        fillField(firstPlayerField);
        promptEnterKey();
        fillField(secondPlayerField);
        promptEnterKey();
        startGame();
        scanner.close();
    }
}
