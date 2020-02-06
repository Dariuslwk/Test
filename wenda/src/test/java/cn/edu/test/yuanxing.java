public class Test {

    public interface Dishe {
        String taste();

        double getPrice();

        String getName();
    }

    private static class Cabbage implements Dishe {
        @Override
        public String taste() {
            return "这是" + this.getName() + "的味道";
        }

        @Override
        public double getPrice() {
            return 10.0d;
        }

        @Override
        public String getName() {
            return "白菜";
        }
    }

    private static class CabbageWithVinegar extends Cabbage implements Dishe {
        @Override
        public String taste() {
            return super.taste() + "，加了盐";
        }

        @Override
        public double getPrice() {
            return super.getPrice() + 1.0d;
        }

        @Override
        public String getName() {
            return "醋" + super.getName();
        }
    }

    private static class CabbageWithVinegarAndSugar extends CabbageWithVinegar implements Dishe {
        @Override
        public String taste() {
            return super.taste() + "，加了糖，" + this.getName() + "好吃！";
        }

        @Override
        public double getPrice() {
            return super.getPrice() + 1.0d;
        }

        @Override
        public String getName() {
            return "糖" + super.getName();
        }
    }

    /**
     * 必定修改main函数的代码，至于其他地方自行思考
     */
    public static void main(String[] args) {
        System.out.println(new CabbageWithVinegarAndSugar().taste());
    }
}
