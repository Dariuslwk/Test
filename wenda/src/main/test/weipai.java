public class Test {
    private static class ChefMaster {
        public void cook() {
            System.out.println("买菜");
            System.out.println("洗菜");
            System.out.println("切菜");
            System.out.println("炒菜");
            System.out.println("上菜");
        }
    }
    private static class ChefExpert {
        public void cook() {
            System.out.println("买菜");
            System.out.println("洗菜");
            System.out.println("切菜");
            System.out.println("炒菜");
            System.out.println("上菜");
        }
    }
    public static void main(String[] args) {
        new Test().new ChefExpert().cook();
        System.out.println("--------------------------");
        new Test().new ChefMaster().cook();
    }
}
