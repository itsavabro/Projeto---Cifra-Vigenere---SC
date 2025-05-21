import javax.swing.JOptionPane;

public class CifraVigenere {

    public static String repetirChave(String chave, int tamanho) {
        StringBuilder chaveRepetida = new StringBuilder();
        for (int i = 0; i < tamanho; i++) {
            chaveRepetida.append(chave.charAt(i % chave.length()));
        }
        return chaveRepetida.toString();
    }

    public static String cifrar(String mensagem, String chave) {
        StringBuilder resultado = new StringBuilder();
        mensagem = mensagem.toUpperCase().replaceAll("[^A-Z]", "");
        chave = chave.toUpperCase();

        String chaveRepetida = repetirChave(chave, mensagem.length());

        for (int i = 0; i < mensagem.length(); i++) {
            int letraMensagem = mensagem.charAt(i) - 'A';
            int letraChave = chaveRepetida.charAt(i) - 'A';
            char letraCifrada = (char) ('A' + (letraMensagem + letraChave) % 26);
            resultado.append(letraCifrada);
        }

        return resultado.toString();
    }

    public static String decifrar(String mensagemCifrada, String chave) {
        StringBuilder resultado = new StringBuilder();
        mensagemCifrada = mensagemCifrada.toUpperCase().replaceAll("[^A-Z]", "");
        chave = chave.toUpperCase();

        String chaveRepetida = repetirChave(chave, mensagemCifrada.length());

        for (int i = 0; i < mensagemCifrada.length(); i++) {
            int letraCifrada = mensagemCifrada.charAt(i) - 'A';
            int letraChave = chaveRepetida.charAt(i) - 'A';
            char letraOriginal = (char) ('A' + (letraCifrada - letraChave + 26) % 26);
            resultado.append(letraOriginal);
        }

        return resultado.toString();
    }

    public static void main(String[] args) {
        String[] opcoes = {"Cifrar", "Decifrar"};
        int escolha = JOptionPane.showOptionDialog(null,
                "Escolha uma opção:",
                "Cifra de Vigenère",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                opcoes,
                opcoes[0]);

        if (escolha == -1) return; // Usuário cancelou

        String entrada = JOptionPane.showInputDialog("Digite a mensagem:");
        if (entrada == null) return;

        String chave = JOptionPane.showInputDialog("Digite a chave:");
        if (chave == null) return;

        String resultado;
        if (escolha == 0) {
            resultado = cifrar(entrada, chave);
            JOptionPane.showMessageDialog(null, "Mensagem Cifrada:\n" + resultado);
        } else {
            resultado = decifrar(entrada, chave);
            JOptionPane.showMessageDialog(null, "Mensagem Decifrada:\n" + resultado);
        }
    }
}

