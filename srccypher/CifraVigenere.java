import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CifraVigenere extends JFrame {
    private JTextArea areaTextoEntrada;
    private JTextArea areaTextoResultado;
    private JTextField campoChave;
    private JButton botaoCifrar;
    private JButton botaoDecifrar;
    private JButton botaoCopiar;

    public CifraVigenere() {
        setTitle("Cifrador/Decifrador de Vigenère");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        // Painel principal
        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Painel de entrada
        JPanel painelEntrada = new JPanel(new BorderLayout());
        painelEntrada.setBorder(BorderFactory.createTitledBorder("Texto de Entrada"));
        areaTextoEntrada = new JTextArea(5, 20);
        areaTextoEntrada.setLineWrap(true);
        JScrollPane rolagemEntrada = new JScrollPane(areaTextoEntrada);
        painelEntrada.add(rolagemEntrada, BorderLayout.CENTER);

        // Painel de chave
        JPanel painelChave = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelChave.add(new JLabel("Chave:"));
        campoChave = new JTextField(20);
        painelChave.add(campoChave);

        // Painel de botões
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        botaoCifrar = new JButton("Cifrar");
        botaoDecifrar = new JButton("Decifrar");
        botaoCopiar = new JButton("Copiar Resultado");
        painelBotoes.add(botaoCifrar);
        painelBotoes.add(botaoDecifrar);
        painelBotoes.add(botaoCopiar);

        // Painel de saída
        JPanel painelSaida = new JPanel(new BorderLayout());
        painelSaida.setBorder(BorderFactory.createTitledBorder("Resultado"));
        areaTextoResultado = new JTextArea(5, 20);
        areaTextoResultado.setLineWrap(true);
        areaTextoResultado.setEditable(false);
        JScrollPane rolagemSaida = new JScrollPane(areaTextoResultado);
        painelSaida.add(rolagemSaida, BorderLayout.CENTER);

        // Adicionando componentes ao painel principal
        painelPrincipal.add(painelEntrada, BorderLayout.NORTH);
        painelPrincipal.add(painelChave, BorderLayout.CENTER);
        painelPrincipal.add(painelBotoes, BorderLayout.SOUTH);

        // Adicionando painel de saída
        add(painelPrincipal, BorderLayout.NORTH);
        add(painelSaida, BorderLayout.CENTER);

        // Configurando listeners
        botaoCifrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cifrarTexto();
            }
        });

        botaoDecifrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                decifrarTexto();
            }
        });

        botaoCopiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                copiarParaAreaTransferencia();
            }
        });
    }

    private void cifrarTexto() {
        String texto = areaTextoEntrada.getText();
        String chave = campoChave.getText();

        if (texto.isEmpty() || chave.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, insira o texto e a chave.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String textoCifrado = aplicarCifraVigenere(texto, chave, true);
        areaTextoResultado.setText(textoCifrado);
    }

    private void decifrarTexto() {
        String texto = areaTextoEntrada.getText();
        String chave = campoChave.getText();

        if (texto.isEmpty() || chave.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, insira o texto e a chave.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String textoDecifrado = aplicarCifraVigenere(texto, chave, false);
        areaTextoResultado.setText(textoDecifrado);
    }

    private String aplicarCifraVigenere(String texto, String chave, boolean cifrar) {
        StringBuilder resultado = new StringBuilder();
        chave = chave.toUpperCase();
        int indiceChave = 0;

        for (int i = 0; i < texto.length(); i++) {
            char caractere = texto.charAt(i);

            if (Character.isLetter(caractere)) {
                // Determina o deslocamento da chave
                char caractereChave = chave.charAt(indiceChave % chave.length());
                int deslocamento = caractereChave - 'A';

                if (!cifrar) {
                    deslocamento = -deslocamento;
                }

                // Aplica a cifra de Vigenère
                char base = Character.isUpperCase(caractere) ? 'A' : 'a';
                int posicaoOriginal = caractere - base;
                int novaPosicao = (posicaoOriginal + deslocamento + 26) % 26;
                char novoCaractere = (char) (base + novaPosicao);
                resultado.append(novoCaractere);

                indiceChave++;
            } else {
                // Mantém caracteres não-alfabéticos como estão (incluindo espaços)
                resultado.append(caractere);
            }
        }

        return resultado.toString();
    }

    private void copiarParaAreaTransferencia() {
        String texto = areaTextoResultado.getText();
        if (!texto.isEmpty()) {
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(
                    new StringSelection(texto), null);
            JOptionPane.showMessageDialog(this, "Texto copiado para a área de transferência!", 
                    "Copiado", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Nada para copiar!", 
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new CifraVigenere().setVisible(true);
            }
        });
    }
}