import java.util.Scanner;

public class TicTacToe {
    public static void main(String[] args) {
        char[][] board = {
            {' ', ' ', ' '},
            {' ', ' ', ' '},
            {' ', ' ', ' '}
        };

        char currentPlayer = 'X';
        boolean gameOngoing = true;

        Scanner scanner = new Scanner(System.in);

        while (gameOngoing) {
            printBoard(board);
            System.out.println("Joueur " + currentPlayer + ", entrez votre coup (ligne et colonne, séparés par un espace) : ");
            int row = scanner.nextInt() - 1; // L'utilisateur entre une ligne (1-3)
            int col = scanner.nextInt() - 1; // L'utilisateur entre une colonne (1-3)

            // Vérifie si la position est valide
            if (row < 0 || row > 2 || col < 0 || col > 2 || board[row][col] != ' ') {
                System.out.println("Coup invalide. Réessayez.");
                continue;
            }

            board[row][col] = currentPlayer;

            
            if (checkWin(board, currentPlayer)) {
                printBoard(board);
                System.out.println("Félicitations ! Le joueur " + currentPlayer + " a gagné !");
                gameOngoing = false;
            } else if (isBoardFull(board)) {
                printBoard(board);
                System.out.println("Match nul !");
                gameOngoing = false;
            } else {
                // Passe au joueur suivant
                currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
            }
        }

        scanner.close();
    }

    // Affiche le tableau
    public static void printBoard(char[][] board) {
        System.out.println("  1 2 3");
        for (int i = 0; i < 3; i++) {
            System.out.print((i + 1) + " ");
            for (int j = 0; j < 3; j++) {
                System.out.print(board[i][j]);
                if (j < 2) System.out.print("|");
            }
            System.out.println();
            if (i < 2) System.out.println("  -----");
        }
    }

    
    public static boolean checkWin(char[][] board, char player) {
        // Vérifie les lignes
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == player && board[i][1] == player && board[i][2] == player) {
                return true;
            }
        }

        // Vérifie les colonnes
        for (int i = 0; i < 3; i++) {
            if (board[0][i] == player && board[1][i] == player && board[2][i] == player) {
                return true;
            }
        }

        // Vérifie les diagonales
        if (board[0][0] == player && board[1][1] == player && board[2][2] == player) {
            return true;
        }
        if (board[0][2] == player && board[1][1] == player && board[2][0] == player) {
            return true;
        }

        return false;
    }

    // Vérifie si le tableau est plein (match nul)
    public static boolean isBoardFull(char[][] board) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }
}
