package Test;

public class Test16 {
    //利用冒泡法实现一维数组元素的排序
    public static void main(String[] args) {
        int score[] = {1, 2, 3, 4, 5};
        for (int i = 0; i < score.length - 1; i++) {
            for (int j = i + 1; j < score.length; j++) {
                if (score[i] < score[j]) {
                    int temp = score[i];
                    score[i] = score[j];
                    score[j] = temp;
                }
            }
            System.out.println("第" + (i + 1) + "排序：");
            for (int j = 0; j < score.length; j++) {
                System.out.print(score[j] + "   ");
            }
            System.out.println("");
        }
        for (int i = 0; i < score.length; i++) {
            System.out.print(score[i]+ "    ");
        }
    }
}
