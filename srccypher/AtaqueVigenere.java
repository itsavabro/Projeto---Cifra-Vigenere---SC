import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.util.*;
import java.util.List;

public class AtaqueVigenere extends JFrame {
    private JTextArea areaTextoCifrado;
    private JTextArea areaTextoResultado;
    private JButton botaoQuebrar;
    private JButton botaoCopiar;
    private JLabel rotuloTamanhoChave;
    private JLabel rotuloChaveEncontrada;

    public AtaqueVigenere() {
        setTitle("Ataque à Cifra de Vigenère");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);

        
        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        
        JPanel painelEntrada = new JPanel(new BorderLayout());
        painelEntrada.setBorder(BorderFactory.createTitledBorder("Texto Cifrado"));
        areaTextoCifrado = new JTextArea(5, 20);
        areaTextoCifrado.setLineWrap(true);
        JScrollPane rolagemEntrada = new JScrollPane(areaTextoCifrado);
        painelEntrada.add(rolagemEntrada, BorderLayout.CENTER);

        
        JPanel painelInformacoes = new JPanel(new GridLayout(2, 1));
        rotuloTamanhoChave = new JLabel("Tamanho provável da chave: ");
        rotuloChaveEncontrada = new JLabel("Chave encontrada: ");
        painelInformacoes.add(rotuloTamanhoChave);
        painelInformacoes.add(rotuloChaveEncontrada);

        
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        botaoQuebrar = new JButton("Quebrar Cifra");
        botaoCopiar = new JButton("Copiar Resultado");
        painelBotoes.add(botaoQuebrar);
        painelBotoes.add(botaoCopiar);

        
        JPanel painelSaida = new JPanel(new BorderLayout());
        painelSaida.setBorder(BorderFactory.createTitledBorder("Texto Decifrado"));
        areaTextoResultado = new JTextArea(5, 20);
        areaTextoResultado.setLineWrap(true);
        areaTextoResultado.setEditable(false);
        JScrollPane rolagemSaida = new JScrollPane(areaTextoResultado);
        painelSaida.add(rolagemSaida, BorderLayout.CENTER);

        
        painelPrincipal.add(painelEntrada, BorderLayout.NORTH);
        painelPrincipal.add(painelInformacoes, BorderLayout.CENTER);
        painelPrincipal.add(painelBotoes, BorderLayout.SOUTH);

        
        add(painelPrincipal, BorderLayout.NORTH);
        add(painelSaida, BorderLayout.CENTER);

        
        botaoQuebrar.addActionListener(e -> quebrarCifra());
        botaoCopiar.addActionListener(e -> copiarParaAreaTransferencia());
    }

    private void quebrarCifra() {
        String textoCifrado = areaTextoCifrado.getText().replaceAll("[^a-zA-Z]", "").toUpperCase();
        
        if (textoCifrado.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, insira um texto cifrado.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        
        int tamanhoChave = determinarTamanhoChave(textoCifrado);
        rotuloTamanhoChave.setText("Tamanho provável da chave: " + tamanhoChave);

        
        String chaveEncontrada = encontrarChave(textoCifrado, tamanhoChave);
        rotuloChaveEncontrada.setText("Chave encontrada: " + chaveEncontrada);

       
        String textoDecifrado = decifrarTexto(areaTextoCifrado.getText(), chaveEncontrada);
        areaTextoResultado.setText(textoDecifrado);
    }

    private int determinarTamanhoChave(String texto) {
        Map<String, List<Integer>> sequencias = new HashMap<>();
        
       
        for (int i = 0; i < texto.length() - 2; i++) {
            String sequencia = texto.substring(i, i + 3);
            sequencias.putIfAbsent(sequencia, new ArrayList<>());
            sequencias.get(sequencia).add(i);
        }
        
        
        List<Integer> distancias = new ArrayList<>();
        for (List<Integer> posicoes : sequencias.values()) {
            if (posicoes.size() > 1) {
                for (int i = 1; i < posicoes.size(); i++) {
                    distancias.add(posicoes.get(i) - posicoes.get(0));
                }
            }
        }
        
       
        if (distancias.isEmpty()) {
            return 1; // Se não encontrou sequências repetidas, assume chave de tamanho 1 (César)
        }
        
        int mdc = distancias.get(0);
        for (int distancia : distancias) {
            mdc = calcularMDC(mdc, distancia);
        }
        
        return mdc > 0 ? mdc : 1;
    }

    private int calcularMDC(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    private String encontrarChave(String texto, int tamanhoChave) {
        StringBuilder chave = new StringBuilder();
        
        
        double[] frequenciasIngles = {
            0.08167, 0.01492, 0.02782, 0.04253, 0.12702, 0.02228, 0.02015, // A-G
            0.06094, 0.06966, 0.00153, 0.00772, 0.04025, 0.02406, 0.06749, // H-N
            0.07507, 0.01929, 0.00095, 0.05987, 0.06327, 0.09056, 0.02758, // O-U
            0.00978, 0.02360, 0.00150, 0.01974, 0.00074                     // V-Z
        };
        
        
        for (int pos = 0; pos < tamanhoChave; pos++) {
            
            StringBuilder grupo = new StringBuilder();
            for (int i = pos; i < texto.length(); i += tamanhoChave) {
                grupo.append(texto.charAt(i));
            }
            
            
            int melhorDeslocamento = 0;
            double melhorScore = Double.NEGATIVE_INFINITY;
            
             deslocamento (A-Z)
            for (int deslocamento = 0; deslocamento < 26; deslocamento++) {
                double score = 0;
                
                
                int[] contagens = new int[26];
                int totalLetras = 0;
                
                for (int i = 0; i < grupo.length(); i++) {
                    char c = grupo.charAt(i);
                    char decifrado = (char) (((c - 'A' - deslocamento + 26) % 26 + 'A'));
                    contagens[decifrado - 'A']++;
                    totalLetras++;
                }
                
               
                for (int i = 0; i < 26; i++) {
                    double frequenciaObservada = (double) contagens[i] / totalLetras;
                    score += frequenciaObservada * frequenciasIngles[i];
                }
                
                if (score > melhorScore) {
                    melhorScore = score;
                    melhorDeslocamento = deslocamento;
                }
            }
            
            chave.append((char) ('A' + melhorDeslocamento));
        }
        
        return chave.toString();
    }

    private String decifrarTexto(String texto, String chave) {
        StringBuilder resultado = new StringBuilder();
        chave = chave.toUpperCase();
        int indiceChave = 0;
        
        for (int i = 0; i < texto.length(); i++) {
            char c = texto.charAt(i);
            
            if (Character.isLetter(c)) {
                char base = Character.isUpperCase(c) ? 'A' : 'a';
                char chaveChar = chave.charAt(indiceChave % chave.length());
                int deslocamento = chaveChar - 'A';
                
                
                int originalPos = c - base;
                int newPos = (originalPos - deslocamento + 26) % 26;
                char newChar = (char) (base + newPos);
                resultado.append(newChar);
                
                indiceChave++;
            } else {
               
                resultado.append(c);
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
        SwingUtilities.invokeLater(() -> new AtaqueVigenere().setVisible(true));
    }
}