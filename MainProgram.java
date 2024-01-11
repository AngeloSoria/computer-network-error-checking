/*
 * 
 *  MADE BY: Angelo Soria
 *  DATE CREATED: January 14, 2024
 *  DESC: Computer Network Data Transmission Checking
 */
 
 public class MainProgram {
     public static void main(String[] args) {
         VerticalRedundancyCheck vrc = new VerticalRedundancyCheck("sIr4UL0");
         vrc.setOdd(1);
         vrc.setEven(0);
         vrc.showParityResult();
         
         LongitudinalRedundancyCheck lrc = new LongitudinalRedundancyCheck("11001110", "00101001", "11010111", "10111011");
         lrc.setEven(0);
         lrc.setOdd(1);
         
         lrc.showParityResult();
     }
 }
 
 class task {
     static void sleep(int millis) throws InterruptedException {
         Thread.sleep(millis);
     }
 
     static void sleep() throws InterruptedException {
         Thread.sleep(0);
     }
 }
 
 class Console {
     public static void println(Object p) {
         System.out.println(p);
     }
     public static void println() {
         System.out.println();
     }
     public static void print(Object p) {
         System.out.print(p);
     }
 }
 
 class Converter {
     public static String charToBinary(char val) {
         return Integer.toBinaryString(val);
     }
 }
 
 class VerticalRedundancyCheck {
     private String value;
     private int Odd, Even;
 
     public VerticalRedundancyCheck(String value) {
         this.value = value == null ? "null" : value;
         
         // Default
         this.Odd = 1;
         this.Even = 0;
     }
     
     public String getValue() {
         return this.value;
     }
 
     public void setOdd(int num) {
         this.Odd = num;
     }
     public int getOdd() {
         return this.Odd;
     }
     public void setEven(int num) {
         this.Even = num;
     }
     public int getEven() {
         return this.Even;
     }
 
     public void showParityResult() {
         int LENGTH = getValue().length();
         
         // Letters
         char[] letters = getValue().toCharArray();
 
         // ASCII
         String[] ascii = new String[LENGTH];
         for (int i = 0; i < letters.length; i++) {
             ascii[i] = Converter.charToBinary(letters[i]);
         }
 
         // Number of bits
         int[] numBits = new int[LENGTH];
         for (int i = 0; i < ascii.length; i++) {
             char[] binary = ascii[i].toCharArray();
             int count = 0;
             for (int j = 0; j < binary.length; j++) {
                 if (binary[j] == '1') {
                     count++;
                 }
             }
             numBits[i] = count;
         }
 
         // Parity
         String[] parity = new String[LENGTH];
         for (int i = 0; i < numBits.length; i++) {
             parity[i] = (numBits[i] % 2 == 0) ? "even" : "odd";
         }
 
         // Parity bit set
         int[] parityBitSet = new int[LENGTH];
         for (int i = 0; i < parity.length; i++) {
             parityBitSet[i] = (parity[i] == "odd") ? getOdd() : getEven();
         }
 
         // ASCII with Parity bit set
         String[] ASCII_PBS = new String[LENGTH];
         for (int i = 0; i < ascii.length; i++) {
             ASCII_PBS[i] = ascii[i].toString() + String.valueOf(parityBitSet[i]);
         }
 
         // display
         Console.println("\n\t\t\t\t   ==| VERTICAL REDUNDANCY CHECK |==");
         Console.println("+" + "-".repeat(104) + "+");
         Console.println("|  CHARS   |   ASCII   |   NUM OF BITS   |   PARITY   |   PARITY BIT SET   |   ASCII w/ PARITY BIT SET   |");
         Console.println("+" + "-".repeat(104) + "+");
         for (int i = 0; i < getValue().length(); i++) {
             String newAscii = (ascii[i].length() == 6) ? ("0" + ascii[i]) : ascii[i];
             String newParity = (parity[i].length() == 3) ? (" " + parity[i]) : parity[i];
             String newASCIIPBS = (ASCII_PBS[i].length() == 7) ? ("0" + ASCII_PBS[i]) : ASCII_PBS[i];
             Console.println(String.format("|    %s     |  %s  |        %d        |    %s    |         %s          |          %s           |", letters[i], newAscii, numBits[i], newParity, parityBitSet[i], newASCIIPBS));
             Console.println("+" + "-".repeat(104) + "+");
         }
     }
 }
 
 class LongitudinalRedundancyCheck {
     private String[] blocks;
     private int Odd, Even;
     private int LENGTH;
 
     public LongitudinalRedundancyCheck(String... blocks) {
         this.blocks = new String[blocks.length];
         for (int i = 0; i < this.blocks.length; i++) {
             this.blocks[i] = blocks[i];
         }
 
         // Set LENGTH according to the length of each blocks. (must have same length each block)
         for (String block : blocks) {
             if (block.length() > this.LENGTH) {
                 this.LENGTH = block.length();
             }
         }
 
         // Default
         this.Odd = 1;
         this.Even = 0;
     }
 
     public String[] getBlocks() {
         return this.blocks;
     }
 
     public void setOdd(int num) {
         this.Odd = num;
     }
     public int getOdd() {
         return this.Odd;
     }
     public void setEven(int num) {
         this.Even = num;
     }
     public int getEven() {
         return this.Even;
     }
 
     public void showParityResult() {
         // decompile each block into array of character.
         /* // Sample
             {
                 block1: {1, 1, 1, 0, 0, 1, 1, 1}
                 block2: {1, 1, 0, 1, 1, 1, 0, 1}
                 ...
             }
         */
         char[][] decBlocks = new char[getBlocks().length][LENGTH];
         for (int i = 0; i < getBlocks().length; i++) {
             char[] stripBits = getBlocks()[i].toCharArray();
             for (int j = 0; j < stripBits.length; j++) {
                 decBlocks[i][j] = stripBits[j];
             }
         }
 
         // compile as columns per index of block's contents...
         // [column_index][column_values]
         /*
             {
                 col1 : {b1, b2, b3, b4, ...}
                 col2 : {b1, b2, b3, b4, ...}
                 col3 : {b1, b2, b3, b4, ...}
                 ...
             }
         */
         // Get the number of columns
         char[][] colValues = new char[this.LENGTH][decBlocks.length];
         // Iterate over columns
         for (int col = 0; col < this.LENGTH; col++) {
             // Iterate over rows
             for (int row = 0; row < decBlocks.length; row++) {
                 colValues[col][row] = decBlocks[row][col];
             }
         }
 
         // Parity
         int[] parity = new int[colValues.length];
         for (int i = 0; i < parity.length; i++) {
             int parityCount = 0;
             for (int j = 0; j < colValues[i].length; j++) {
                 if (colValues[i][j] == '1') {
                     parityCount++;
                 }
             }
             parity[i] = parityCount;
         }
 
         // LRC
         int[] lrc = new int[parity.length];
         for (int i = 0; i < lrc.length; i++) {
             lrc[i] = (parity[i] % 2 == 0) ? getEven() : getOdd();
         }
 
         // Display
         Console.println("\n\n   ==| LONGITUDINAL REDUNDANCY CHECK |==");
         Console.println("+" + "-".repeat(41) + "+");
         for (int i = 0; i < decBlocks.length; i++) {
             char[] block = decBlocks[i];
             Console.print(String.format("| Block-%d |", i+1));
             for (char value : block) {
                 Console.print(String.format(" %c |", value));
             }
             Console.println();
             Console.println("+" + "-".repeat(41) + "+");
         }
 
         Console.print("| Parity  |");
         for (int i : parity) {
             Console.print(String.format(" %d |", i));
         }
         Console.println();
         Console.println("+" + "-".repeat(41) + "+");
         
         Console.print("|   LRC   |");
         for (int i : lrc) {
             Console.print(String.format(" %d |", i));
         }
         Console.println();
         Console.println("+" + "-".repeat(41) + "+");
     }
 }
 
 
 