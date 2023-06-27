package calculatrice;

import java.util.Scanner;

public class Calculatrice {
	public static double faireOperation(double nombre1,double nombre2,char operation) {
        double resultat=0;
        double maxDouble = Double.MAX_VALUE;
        switch(operation) {
	        case '+':
	            resultat = nombre1 + nombre2;
	            break;
	        case '-':
	            resultat = nombre1 - nombre2;
	            break;
	        case '*':
	            resultat = nombre1 * nombre2;
	            break;
	        case '/':
	            if (nombre2 != 0) {
	                resultat = nombre1 / nombre2;
	            } else {
	                System.out.println("Erreur! Division par zéro n'est pas possible.");
	                resultat=maxDouble;
	            }
	            break;
	        default:
	            System.out.println("Erreur! Opération invalide.");
        }
        return(resultat);
    }
	public static void main(String[] args) {
		double nombre1=0,nombre2=0,resultat=0;
		String operation = "QUITTER";
		double maxDouble = Double.MAX_VALUE;
		char op;
		Scanner scanner = new Scanner(System.in);
		System.out.println("Bienvenue sur l'outil de calculatrice.\n");
		do {
			try {
				System.out.println("QUITTER pour arrêter");
				System.out.print("Entrez une opération (+, -, *, /):");
				operation = scanner.next();
				op = operation.charAt(0);
				while((!operation.equals("QUITTER")) && (op!='+') && (op !='-') && (op!='/') && (op!='*')) {
					System.out.print("Entrez une opération (+, -, *, /):");
				    operation = scanner.next();
				    op = operation.charAt(0);
				}
				if(!operation.equals("QUITTER")) {
					System.out.print("Entrer un chiffre :");
					nombre1 = scanner.nextDouble();
					System.out.print("Entrer un chiffre :");
					nombre2 = scanner.nextDouble();
					resultat=faireOperation(nombre1,nombre2,op);
					if(resultat!=maxDouble)
						System.out.println(nombre1+operation+nombre2+'='+resultat+"\n");
				}
			}catch (Exception e) {
			    // Gestion des exceptions
				System.out.println("Erreur avec le scanner :"+e);
			} finally {
			    // Code qui serait normalement placé dans le bloc finally
			    // Notez que cette partie sera omise ici
			}
		}while(!operation.equals("QUITTER"));
		System.out.println("Merci d'avoir utilisé la calculatrice.");
		scanner.close();
	}
}
