public class PerformanceTest {

    public static void main(String[] args){
        int count = 1000;

        int featureId1 = 0;
        int featureId2 = 0;
        while (featureId1 <= 1000) {
            featureId2 = featureId1 + 1;
            while (featureId2 <= 1000) {
                System.out.println(featureId1 +" --- "+ featureId2++);
            }
            featureId1++;
        }
    }
}
