import javax.swing.*;
import java.awt.*;
import java.util.Random;

    public class SortingVisualizerFull extends JFrame {

        private int[] array;
    private JPanel panel;
    private final int SIZE = 100;
    private final int WIDTH = 8;
    private final int HEIGHT = 400;
    private JComboBox<String> algorithmSelector;
    private JSlider speedSlider;

    public SortingVisualizerFull() {
        setTitle("Sorting Visualizer - All Algorithms");
        setSize(900, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        array = new int[SIZE];
        generateArray();

        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawArray(g);
            }
        };
        panel.setBackground(Color.black);
        add(panel, BorderLayout.CENTER);

        JButton generateButton = new JButton("New Array");
        JButton sortButton = new JButton("Sort");

        algorithmSelector = new JComboBox<>(new String[]{"Bubble Sort", "Selection Sort", "Insertion Sort", "Merge Sort", "Quick Sort"});

        speedSlider = new JSlider(1, 100, 25);
        speedSlider.setPaintTicks(true);
        speedSlider.setPaintLabels(true);
        speedSlider.setMajorTickSpacing(25);
        speedSlider.setMinorTickSpacing(5);

        generateButton.addActionListener(e -> {
            generateArray();
            panel.repaint();
        });

        sortButton.addActionListener(e -> new Thread(() -> {
            try {
                String selected = (String) algorithmSelector.getSelectedItem();
                switch (selected) {
                    case "Bubble Sort": bubbleSort(); break;
                    case "Selection Sort": selectionSort(); break;
                    case "Insertion Sort": insertionSort(); break;
                    case "Merge Sort": mergeSort(0, array.length - 1); break;
                    case "Quick Sort": quickSort(0, array.length - 1); break;
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }).start());

        JPanel controls = new JPanel();
        controls.add(generateButton);
        controls.add(sortButton);
        controls.add(new JLabel("Algorithm:"));
        controls.add(algorithmSelector);
        controls.add(new JLabel("Speed:"));
        controls.add(speedSlider);

        add(controls, BorderLayout.SOUTH);
        setVisible(true);
    }

    private void generateArray() {
        Random rand = new Random();
        for (int i = 0; i < SIZE; i++) {
            array[i] = rand.nextInt(HEIGHT);
        }
    }

    private void drawArray(Graphics g) {
        g.setColor(Color.white);
        for (int i = 0; i < array.length; i++) {
            g.fillRect(i * WIDTH, HEIGHT - array[i], WIDTH - 1, array[i]);
        }
    }

    private void sleep() throws InterruptedException {
        int delay = 101 - speedSlider.getValue();
        Thread.sleep(delay);
    }

    private void bubbleSort() throws InterruptedException {
        for (int i = 0; i < array.length - 1; i++) {
            for (int j = 0; j < array.length - i - 1; j++) {
                if (array[j] > array[j + 1]) {
                    int temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                }
                panel.repaint();
                sleep();
            }
        }
    }

    private void selectionSort() throws InterruptedException {
        for (int i = 0; i < array.length - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < array.length; j++) {
                if (array[j] < array[minIdx]) {
                    minIdx = j;
                }
            }
            int temp = array[minIdx];
            array[minIdx] = array[i];
            array[i] = temp;
            panel.repaint();
            sleep();
        }
    }

    private void insertionSort() throws InterruptedException {
        for (int i = 1; i < array.length; i++) {
            int key = array[i];
            int j = i - 1;
            while (j >= 0 && array[j] > key) {
                array[j + 1] = array[j];
                j--;
                panel.repaint();
                sleep();
            }
            array[j + 1] = key;
            panel.repaint();
            sleep();
        }
    }

    private void mergeSort(int low, int high) throws InterruptedException {
        if (low < high) {
            int mid = (low + high) / 2;
            mergeSort(low, mid);
            mergeSort(mid + 1, high);
            merge(low, mid, high);
        }
    }

    private void merge(int low, int mid, int high) throws InterruptedException {
        int[] temp = new int[high - low + 1];
        int i = low, j = mid + 1, k = 0;

        while (i <= mid && j <= high) {
            if (array[i] < array[j]) {
                temp[k++] = array[i++];
            } else {
                temp[k++] = array[j++];
            }
        }
        while (i <= mid) temp[k++] = array[i++];
        while (j <= high) temp[k++] = array[j++];

        for (i = low, k = 0; i <= high; i++, k++) {
            array[i] = temp[k];
            panel.repaint();
            sleep();
        }
    }

    private void quickSort(int low, int high) throws InterruptedException {
        if (low < high) {
            int pi = partition(low, high);
            quickSort(low, pi - 1);
            quickSort(pi + 1, high);
        }
    }

    private int partition(int low, int high) throws InterruptedException {
        int pivot = array[high];
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (array[j] < pivot) {
                i++;
                int temp = array[i];
                array[i] = array[j];
                array[j] = temp;
                panel.repaint();
                sleep();
            }
        }
        int temp = array[i + 1];
        array[i + 1] = array[high];
        array[high] = temp;
        panel.repaint();
        sleep();
        return i + 1;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SortingVisualizerFull::new);
    }
}